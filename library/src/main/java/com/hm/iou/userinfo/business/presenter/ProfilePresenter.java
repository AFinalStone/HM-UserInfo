package com.hm.iou.userinfo.business.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.hm.iou.base.ActivityManager;
import com.hm.iou.base.event.OpenWxResultEvent;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.logger.Logger;
import com.hm.iou.network.HttpReqManager;
import com.hm.iou.router.Router;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.event.BindBankSuccessEvent;
import com.hm.iou.sharedata.event.LogoutEvent;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.sharedata.model.SexEnum;
import com.hm.iou.sharedata.model.UserExtendInfo;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.sharedata.model.UserThirdPlatformInfo;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.api.PersonApi;
import com.hm.iou.userinfo.bean.IsWXExistBean;
import com.hm.iou.userinfo.business.ProfileContract;
import com.hm.iou.userinfo.event.UpdateAvatarEvent;
import com.hm.iou.userinfo.event.UpdateIncomeEvent;
import com.hm.iou.userinfo.event.UpdateLocationEvent;
import com.hm.iou.userinfo.event.UpdateMobileEvent;
import com.hm.iou.userinfo.event.UpdateNicknameAndSexEvent;
import com.hm.iou.userinfo.event.UpdateWeixinEvent;
import com.hm.iou.wxapi.WXEntryActivity;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hjy on 2018/5/23.
 */

public class ProfilePresenter extends MvpActivityPresenter<ProfileContract.View> implements ProfileContract.Presenter {

    private static final String EVENT_KEY_BIND_WX = "person_bind_weixin";

    private IWXAPI mWXApi;

    private int mColorUnBind;           //绑定的颜色
    private int mColorHaveBind;         //未绑定的颜色

    public ProfilePresenter(@NonNull Context context, @NonNull ProfileContract.View view) {
        super(context, view);
        EventBus.getDefault().register(this);
        mColorUnBind = context.getResources().getColor(R.color.uikit_function_exception);
        mColorHaveBind = context.getResources().getColor(R.color.uikit_text_hint);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mWXApi != null) {
            mWXApi.detach();
            mWXApi = null;
            WXEntryActivity.cleanWXLeak();
        }
    }

    @Override
    public void getUserProfile() {
        UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        //显示用户头像
        showUserAvatar(userInfo);
        showNickname(userInfo);
        showAliPay("");
        showMobile(userInfo);
        showWeixin(userInfo);
        showCity(userInfo);
        showMainIncome(userInfo);
    }

    @Override
    public void updateLocation(final String location) {
        mView.showLoadingView();
        final UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        PersonApi.updateLocation(userInfo, location)
                .compose(getProvider().<BaseResponse<Object>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<Object>handleResponse())
                .subscribeWith(new CommSubscriber<Object>(mView) {
                    @Override
                    public void handleResult(Object data) {
                        mView.dismissLoadingView();
                        userInfo.setLocation(location);
                        UserManager.getInstance(mContext).updateOrSaveUserInfo(userInfo);
                        //显示城市名
                        showCity(userInfo);
                        EventBus.getDefault().post(new UpdateLocationEvent());
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });
    }

    @Override
    public void toBindWeixin() {
        if (mWXApi != null) {
            mWXApi.detach();
        }
        mWXApi = WXEntryActivity.openWxAuth(mContext, EVENT_KEY_BIND_WX);
    }

    @Override
    public void logout() {
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
        mView.showAvatar(avatarUrl, defaultAvatarResId);
    }

    /**
     * 显示昵称
     *
     * @param userInfo
     */
    private void showNickname(UserInfo userInfo) {
        String nickname = userInfo.getNickName();
        int sex = userInfo.getSex();
        int sexIcon = 0;
        if (sex == SexEnum.MALE.getValue()) {
            sexIcon = R.mipmap.person_ic_sex_man;
        } else if (sex == SexEnum.FEMALE.getValue()) {
            sexIcon = R.mipmap.person_ic_sex_woman;
        }
        mView.showNicknameAndSex(nickname, sexIcon);
    }

    /**
     * 显示支付宝账号
     *
     * @param aliPay
     */
    private void showAliPay(String aliPay) {
        if (TextUtils.isEmpty(aliPay)) {
            mView.showAliPay("未填写", mColorUnBind);
        } else {
            mView.showAliPay(aliPay, mColorHaveBind);
        }
    }

    /**
     * 显示手机号
     *
     * @param userInfo
     */
    private void showMobile(UserInfo userInfo) {
        String mobile = userInfo.getMobile();
        if (!TextUtils.isEmpty(mobile) && mobile.length() >= 11) {
            mobile = mobile.substring(0, 3) + "****" + mobile.substring(7, 11);
        }
        mView.showMobile(mobile);
    }

    /**
     * 显示微信
     *
     * @param userInfo
     */
    private void showWeixin(UserInfo userInfo) {
        if (UserDataUtil.isPlusClass(userInfo.getType())) {
            mView.showWeixin("已绑定", mColorHaveBind);
        } else {
            mView.showWeixin("未绑定", mColorUnBind);
        }
    }

    /**
     * 显示常住城市
     *
     * @param userInfo
     */
    private void showCity(UserInfo userInfo) {
        mView.showCity(TextUtils.isEmpty(userInfo.getLocation()) ? "无" : userInfo.getLocation());
    }

    /**
     * 显示主要收入
     *
     * @param userInfo
     */
    private void showMainIncome(UserInfo userInfo) {
        String mainIncome = UserDataUtil.getIncomeNameByType(userInfo.getMainIncome());
        mView.showMainIncome(mainIncome);
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
                        mView.toastMessage("绑定成功");
                        EventBus.getDefault().post(new UpdateWeixinEvent(true));
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
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
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdateNicknameAndSex(UpdateNicknameAndSexEvent event) {
        UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        showNickname(userInfo);
    }

    /**
     * 更新手机号
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdateMobile(UpdateMobileEvent event) {
        UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        showMobile(userInfo);
    }

    /**
     * 更新收入情况
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdateIncome(UpdateIncomeEvent event) {
        UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        showMainIncome(userInfo);
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
     * 微信绑定事件通知
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBindWeixin(UpdateWeixinEvent event) {
        UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        showWeixin(userInfo);
    }

}