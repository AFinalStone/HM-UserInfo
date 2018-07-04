package com.hm.iou.userinfo.business.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hm.iou.base.ActivityManager;
import com.hm.iou.base.event.OpenWxResultEvent;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.network.HttpReqManager;
import com.hm.iou.router.Router;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.event.CommBizEvent;
import com.hm.iou.sharedata.event.LogoutEvent;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.sharedata.model.SexEnum;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.api.PersonApi;
import com.hm.iou.userinfo.bean.IsWXExistBean;
import com.hm.iou.userinfo.business.PersonalCenterContract;
import com.hm.iou.userinfo.event.UpdateAvatarEvent;
import com.hm.iou.userinfo.event.UpdateIncomeEvent;
import com.hm.iou.userinfo.event.UpdateMobileEvent;
import com.hm.iou.userinfo.event.UpdateNicknameAndSexEvent;
import com.hm.iou.wxapi.WXEntryActivity;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by hjy on 2018/5/23.
 */

public class PersonalCenterPresenter extends MvpActivityPresenter<PersonalCenterContract.View> implements PersonalCenterContract.Presenter {

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

        showUserAvatarAndSex(userInfo);

        String id = userInfo.getShowId();
        mView.showUserId("ID：" + id);

        int customerTypeEnum = userInfo.getType();
        if (UserDataUtil.isCClass(customerTypeEnum)) {
            mView.showAuthenticationImg(R.mipmap.icon_authentication_not_have);
            mView.showAuthName("定制");
        } else {
            String name = userInfo.getName();
            mView.showAuthName(name);
            mView.showAuthenticationImg(R.mipmap.icon_authentication_have);
        }


        String nickname = userInfo.getNickName();
        mView.showUserNickname(TextUtils.isEmpty(nickname) ? "无" : nickname);

        String mobile = userInfo.getMobile();
        mView.showModuleMobile(TextUtils.isEmpty(mobile) ? "无" : mobile);

        String city = userInfo.getLocation();
        mView.showModuleLocation(TextUtils.isEmpty(city) ? "无" : city);

        int mainIncome = userInfo.getMainIncome();
        String incomeStr = UserDataUtil.getIncomeNameByType(mainIncome);
        mView.showModuleMainIncome(incomeStr);

    }

    private void showUserAvatarAndSex(UserInfo userInfo) {
        String avatarUrl = userInfo.getAvatarUrl();
        //头像
        int sex = userInfo.getSex();
        int defaultAvatarResId;
        if (sex == SexEnum.MALE.getValue()) {
            defaultAvatarResId = R.mipmap.uikit_icon_header_man;
            mView.showModuleSexImage(R.mipmap.icon_personal_name_sex_man);
        } else if (sex == SexEnum.FEMALE.getValue()) {
            defaultAvatarResId = R.mipmap.uikit_icon_header_wuman;
            mView.showModuleSexImage(R.mipmap.icon_personal_name_sex_wuman);
        } else {
            defaultAvatarResId = R.mipmap.uikit_icon_header_unknow;
            mView.showModuleSexImage(R.mipmap.icon_personal_name_sex_unkonw);
        }
        mView.showUserAvatar(avatarUrl, defaultAvatarResId);
    }

    @Override
    public void doLogout() {
        mView.showLoadingView("安全退出中...");
        UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        PersonApi.logout(userInfo.getMobile())
                .compose(getProvider().<BaseResponse<Object>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<Object>handleResponse())
                .subscribeWith(new CommSubscriber<Object>(mView) {
                    @Override
                    public void handleResult(Object data) {
                        mView.dismissLoadingView();
                        exitApp();
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                        exitApp();
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

    private void exitApp() {
        EventBus.getDefault().post(new LogoutEvent());
        HttpReqManager.getInstance().setUserId("");
        HttpReqManager.getInstance().setToken("");
        UserManager.getInstance(mContext).logout();
        ActivityManager.getInstance().exitAllActivities();
        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/login/selecttype")
                .navigation(mContext);
    }

    @Override
    public void updateLocation(String location) {
        mView.showLoadingView();
        final UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        PersonApi.updateLocation(userInfo, location)
                .compose(getProvider().<BaseResponse<Object>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<Object>handleResponse())
                .subscribeWith(new CommSubscriber<Object>(mView) {
                    @Override
                    public void handleResult(Object data) {
                        mView.dismissLoadingView();
                        mView.showModuleLocation(location);
                        userInfo.setLocation(location);
                        UserManager.getInstance(mContext).updateOrSaveUserInfo(userInfo);
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });

    }

    @Override
    public void toBindWeixin() {
        WXEntryActivity.openWx(mContext, EVENT_KEY_BIND_WX);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdateNicknameAndSex(UpdateNicknameAndSexEvent event) {
        UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        showUserAvatarAndSex(userInfo);
        mView.showUserNickname(userInfo.getNickName());
    }

    private void toBindWeixin(String code) {
        mView.showLoadingView();
        PersonApi.isWXExist(code)
                .compose(getProvider().<BaseResponse<IsWXExistBean>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<IsWXExistBean>handleResponse())
                .subscribeWith(new CommSubscriber<IsWXExistBean>(mView) {
                    @Override
                    public void handleResult(IsWXExistBean data) {
                        if (data.getCount() > 0) {
                            mView.dismissLoadingView();
                            mView.toastMessage("当前微信已经绑定过手机号");
                        } else {
                            String sn = data.getSn();
                            bindWeixinByWxSn(sn);
                        }
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });

    }

    private void bindWeixinByWxSn(String wxSn) {
        PersonApi.bindWXAfterLogin(wxSn)
                .compose(getProvider().<BaseResponse<Object>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<Object>handleResponse())
                .subscribeWith(new CommSubscriber<Object>(mView) {
                    @Override
                    public void handleResult(Object data) {
                        mView.dismissLoadingView();
                        UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
                        int type = UserDataUtil.getUpgradeCustomerTypeAfterBindWeixin(userInfo.getType());
                        userInfo.setType(type);
                        UserManager.getInstance(mContext).updateOrSaveUserInfo(userInfo);
                        mView.toastMessage("成功绑定微信");
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });
    }

    /**
     * 更新我的主要收入
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdateIncome(UpdateIncomeEvent event) {
        UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        int mainIncome = userInfo.getMainIncome();
        String incomeStr = UserDataUtil.getIncomeNameByType(mainIncome);
        mView.showModuleMainIncome(incomeStr);
    }

    /**
     * 更新头像
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdateAvatar(UpdateAvatarEvent event) {
        UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        showUserAvatarAndSex(userInfo);
    }

    /**
     * 更新手机号
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdateMobile(UpdateMobileEvent event) {
        String mobile = UserManager.getInstance(mContext).getUserInfo().getMobile();
        mView.showModuleMobile(TextUtils.isEmpty(mobile) ? "无" : mobile);
    }

    /**
     * 微信获取到的Code
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventWeixin(OpenWxResultEvent event) {
        if (EVENT_KEY_BIND_WX.equals(event.getKey())) {
            String code = event.getCode();
            toBindWeixin(code);
        }
    }

    /**
     * 签约密码校验结果
     *
     * @param commBizEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvenBusCheckSignPsdResult(CommBizEvent commBizEvent) {
        if ("Signature_checkSignPsdResult".equals(commBizEvent.key)) {
            if ("true".equals(commBizEvent.content)) {
                Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/signature/signature_list")
                        .navigation(mContext);
            } else if ("false".equals(commBizEvent.content)) {

            }
        }
    }

}
