package com.hm.iou.userinfo.business.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hm.iou.base.mvp.MvpFragmentPresenter;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.event.CommBizEvent;
import com.hm.iou.sharedata.event.RealNameEvent;
import com.hm.iou.sharedata.model.IncomeEnum;
import com.hm.iou.sharedata.model.SexEnum;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.business.PersonalCenterContract;
import com.hm.iou.userinfo.event.UpdateAvatarEvent;
import com.hm.iou.userinfo.event.UpdateIncomeEvent;
import com.hm.iou.userinfo.event.UpdateLocationEvent;
import com.hm.iou.userinfo.event.UpdateMobileEvent;
import com.hm.iou.userinfo.event.UpdateNicknameAndSexEvent;
import com.hm.iou.userinfo.event.UpdateWeixinEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by hjy on 2018/5/23.
 */

public class PersonalCenterPresenter extends MvpFragmentPresenter<PersonalCenterContract.View> implements PersonalCenterContract.Presenter {

    private static final String EVENT_KEY_BIND_WX = "person_bind_weixin";

    public PersonalCenterPresenter(@NonNull Context context, @NonNull PersonalCenterContract.View view) {
        super(context, view);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void init() {
        UserManager userManager = UserManager.getInstance(mContext);
        UserInfo userInfo = userManager.getUserInfo();

        String id = userInfo.getShowId();
        mView.showUserId("ID：" + id);

        int customerTypeEnum = userInfo.getType();
        if (UserDataUtil.isCClass(customerTypeEnum)) {
            mView.showAuthenticationImg(R.mipmap.icon_authentication_not_have);
            mView.showAuthName("定制签名");
        } else {
            String name = userInfo.getName();
            mView.showAuthName(name);
            mView.showAuthenticationImg(R.mipmap.icon_authentication_have);
        }

        String nickname = userInfo.getNickName();
        mView.showUserNickname(TextUtils.isEmpty(nickname) ? "无" : nickname);
        showUserAvatar(userInfo);

        mView.showNewsFavoriteCount("10篇");
        mView.showCloudSpace("1024GB");
        mView.showHelpAndFeedbackCount("28");

        getProfileProgress();
    }

    @Override
    public void getProfileProgress() {
        UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        int count = 0;
        //头像
        if (!TextUtils.isEmpty(userInfo.getAvatarUrl())) {
            count++;
        }
        //性别
        int sex = userInfo.getSex();
        if (sex != SexEnum.UNKNOWN.getValue()) {
            count++;
        }
        //实名认证
        if (!UserDataUtil.isCClass(userInfo.getType())) {
            count++;
        }
        //手机号
        if (!TextUtils.isEmpty(userInfo.getMobile())) {
            count++;
        }
        //绑定微信号
        if (UserDataUtil.isPlusClass(userInfo.getType())) {
            count++;
        }
        //城市
        if (!TextUtils.isEmpty(userInfo.getLocation())) {
            count++;
        }
        //收入
        int income = userInfo.getMainIncome();
        if (income > IncomeEnum.None.getValue()) {
            count++;
        }
        if (count == 7) {
            //全部完成
            mView.showProfileProgress("100%");
        } else {
            int progress = count * 15;
            mView.showProfileProgress(progress + "%");
        }
    }

    /**
     * 显示用户头像
     *
     * @param userInfo
     */
    private void showUserAvatar(UserInfo userInfo) {
        String avatarUrl = userInfo.getAvatarUrl();
        //头像
        int sex = userInfo.getSex();
        int defaultAvatarResId;
        if (sex == SexEnum.MALE.getValue()) {
            defaultAvatarResId = R.mipmap.uikit_icon_header_man;
        } else if (sex == SexEnum.FEMALE.getValue()) {
            defaultAvatarResId = R.mipmap.uikit_icon_header_wuman;
        } else {
            defaultAvatarResId = R.mipmap.uikit_icon_header_unknow;
        }
        mView.showUserAvatar(avatarUrl, defaultAvatarResId);
    }


    /**
     * 更新头像
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdateAvatar(UpdateAvatarEvent event) {
        UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        showUserAvatar(userInfo);
        getProfileProgress();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdateNicknameAndSex(UpdateNicknameAndSexEvent event) {
        UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        mView.showUserNickname(userInfo.getNickName());
        getProfileProgress();
    }

    /**
     * 更新手机号
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdateMobile(UpdateMobileEvent event) {
        getProfileProgress();
    }

    /**
     * 更新我的主要收入
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdateIncome(UpdateIncomeEvent event) {
        getProfileProgress();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventWeixinBind(UpdateWeixinEvent event) {
        getProfileProgress();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdateLocation(UpdateLocationEvent event) {
        getProfileProgress();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRealName(RealNameEvent event) {
        UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        getProfileProgress();
    }

    /**
     *
     * @param commBizEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvenBusCommBiz(CommBizEvent commBizEvent) {
        if ("feedback_read_detail".equals(commBizEvent.key)) {
            //阅读过反馈

        }
    }

}
