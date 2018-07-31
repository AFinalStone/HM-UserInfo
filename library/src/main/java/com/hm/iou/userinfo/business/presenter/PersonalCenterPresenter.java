package com.hm.iou.userinfo.business.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.hm.iou.base.mvp.MvpFragmentPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.logger.Logger;
import com.hm.iou.router.Router;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.event.BindBankSuccessEvent;
import com.hm.iou.sharedata.event.CommBizEvent;
import com.hm.iou.sharedata.event.RealNameEvent;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.sharedata.model.IncomeEnum;
import com.hm.iou.sharedata.model.SexEnum;
import com.hm.iou.sharedata.model.UserExtendInfo;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.sharedata.model.UserThirdPlatformInfo;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.api.PersonApi;
import com.hm.iou.userinfo.bean.UserCenterStatisticBean;
import com.hm.iou.userinfo.business.PersonalCenterContract;
import com.hm.iou.userinfo.event.UpdateAvatarEvent;
import com.hm.iou.userinfo.event.UpdateIncomeEvent;
import com.hm.iou.userinfo.event.UpdateLocationEvent;
import com.hm.iou.userinfo.event.UpdateMobileEvent;
import com.hm.iou.userinfo.event.UpdateNicknameAndSexEvent;
import com.hm.iou.userinfo.event.UpdateWeixinEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.disposables.Disposable;

/**
 * Created by hjy on 2018/5/23.
 */

public class PersonalCenterPresenter extends MvpFragmentPresenter<PersonalCenterContract.View> implements PersonalCenterContract.Presenter {

    private Disposable mStatisticDisposable;
    private Disposable mThirdPlatformInfoDisposable;

    public PersonalCenterPresenter(@NonNull Context context, @NonNull PersonalCenterContract.View view) {
        super(context, view);
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void init() {
        getUserThirdPlatformInfo();
        UserManager userManager = UserManager.getInstance(mContext);
        UserInfo userInfo = userManager.getUserInfo();

        String id = userInfo.getShowId();
        mView.showUserId("ID：" + id);

        int customerTypeEnum = userInfo.getType();
        if (UserDataUtil.isCClass(customerTypeEnum)) {
            mView.showAuthenticationImg(R.mipmap.person_ic_authentication_not_have);
            mView.showAuthName("定制签名");
        } else {
            String name = userInfo.getName();
            mView.showAuthName(name);
            mView.showAuthenticationImg(R.mipmap.person_ic_authentication_have);
        }

        String nickname = userInfo.getNickName();
        mView.showUserNickname(TextUtils.isEmpty(nickname) ? "无" : nickname);
        showUserAvatar(userInfo);

        getProfileProgress();
    }


    @Override
    public void getProfileProgress() {
        UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        int count = 0;
        //头像
        if (!TextUtils.isEmpty(userInfo.getAvatarUrl())) {
            count += 10;
        }
        //性别
        int sex = userInfo.getSex();
        if (sex != SexEnum.UNKNOWN.getValue()) {
            count += 10;
        }
        //实名认证
        if (!UserDataUtil.isCClass(userInfo.getType())) {
            count += 20;
        }
        //银行卡绑定
        UserThirdPlatformInfo thirdPlatformInfo = UserManager.getInstance(mContext).getUserExtendInfo().getThirdPlatformInfo();
        if (thirdPlatformInfo != null) {
            UserThirdPlatformInfo.BankInfoRespBean bankInfoRespBean = thirdPlatformInfo.getBankInfoResp();
            if (bankInfoRespBean != null && 1 == bankInfoRespBean.getIsBinded()) {
                count += 30;
            }
        }

        //手机号
        if (!TextUtils.isEmpty(userInfo.getMobile())) {
            count += 10;
        }
        //绑定微信号
        if (UserDataUtil.isPlusClass(userInfo.getType())) {
            count += 10;
        }
        //城市
        if (!TextUtils.isEmpty(userInfo.getLocation())) {
            count += 5;
        }
        //收入
        int income = userInfo.getMainIncome();
        if (income >= IncomeEnum.None.getValue()) {
            count += 5;
        }
        if (count == 100) {
            //全部完成
            mView.showProfileProgress("100%");
        } else {
            int progress = count;
            mView.showProfileProgress(progress + "%");
        }


    }

    @Override
    public void getStatisticData() {
        if (mStatisticDisposable != null && !mStatisticDisposable.isDisposed()) {
            mStatisticDisposable.dispose();
            mStatisticDisposable = null;
        }
        mStatisticDisposable = PersonApi.getUserCenterStatistic()
                .compose(getProvider().<BaseResponse<UserCenterStatisticBean>>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .map(RxUtil.<UserCenterStatisticBean>handleResponse())
                .subscribeWith(new CommSubscriber<UserCenterStatisticBean>(mView) {
                    @Override
                    public void handleResult(UserCenterStatisticBean data) {
                        mView.showNewsFavoriteCount(data.getMyCollect() == 0 ? "" : data.getMyCollect() + "篇");
                        mView.showCloudSpace(UserDataUtil.formatUserCloudSpace(data.getUserSpaceSize()));
                        mView.showHelpAndFeedbackCount(data.getNoReadComplain() == 0 ? "" : data.getNoReadComplain() + "");
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                    }

                    @Override
                    public boolean isShowCommError() {
                        return false;
                    }

                    @Override
                    public boolean isShowBusinessError() {
                        return false;
                    }
                });
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
     * 获取第三方平台的认证信息
     */
    private void getUserThirdPlatformInfo() {

        UserThirdPlatformInfo userThirdPlatformInfo = UserManager.getInstance(mContext).getUserExtendInfo().getThirdPlatformInfo();
        if (userThirdPlatformInfo != null) {
            UserThirdPlatformInfo.BankInfoRespBean bankInfoRespBean = userThirdPlatformInfo.getBankInfoResp();
            if (bankInfoRespBean != null && 1 == bankInfoRespBean.getIsBinded()) {
                mView.showBindBankImg(R.mipmap.person_ic_bind_bank_have);
                return;
            } else {
                mView.showBindBankImg(R.mipmap.person_ic_bind_bank_not_have);
            }
        }
        if (mThirdPlatformInfoDisposable != null && !mThirdPlatformInfoDisposable.isDisposed()) {
            mThirdPlatformInfoDisposable.dispose();
            mThirdPlatformInfoDisposable = null;
        }
        mThirdPlatformInfoDisposable = PersonApi.getUserThirdPlatformInfo()
                .compose(getProvider().<BaseResponse<UserThirdPlatformInfo>>bindUntilEvent(FragmentEvent.DESTROY))
                .map(RxUtil.<UserThirdPlatformInfo>handleResponse())
                .subscribeWith(new CommSubscriber<UserThirdPlatformInfo>(mView) {
                    @Override
                    public void handleResult(UserThirdPlatformInfo thirdInfo) {
                        Logger.d("user" + thirdInfo.getBankInfoResp().toString());
                        UserThirdPlatformInfo.BankInfoRespBean bankInfoRespBean = thirdInfo.getBankInfoResp();
                        if (bankInfoRespBean != null && 1 == bankInfoRespBean.getIsBinded()) {
                            mView.showBindBankImg(R.mipmap.person_ic_bind_bank_have);
                        } else {
                            mView.showBindBankImg(R.mipmap.person_ic_bind_bank_not_have);
                        }
                        //存储绑定银行卡信息
                        UserExtendInfo extendInfo = new UserExtendInfo();
                        extendInfo.setThirdPlatformInfo(thirdInfo);
                        UserManager.getInstance(mContext).updateOrSaveUserExtendInfo(extendInfo);
                        //更新进度
                        getProfileProgress();
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {

                    }

                    @Override
                    public boolean isShowBusinessError() {
                        return false;
                    }

                    @Override
                    public boolean isShowCommError() {
                        return false;
                    }
                });
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
        showUserAvatar(userInfo);
        String name = userInfo.getName();
        mView.showAuthName(name);
        mView.showAuthenticationImg(R.mipmap.person_ic_authentication_have);
        getProfileProgress();
    }

    /**
     * @param commBizEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvenBusCommBiz(CommBizEvent commBizEvent) {
        if ("feedback_read_detail".equals(commBizEvent.key)) {
            //阅读过反馈

        }
    }

    /**
     * 银行卡绑定成功
     *
     * @param bindBankSuccessEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvenBusBindBankSuccess(BindBankSuccessEvent bindBankSuccessEvent) {
        getUserThirdPlatformInfo();
    }
}
