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
import com.hm.iou.network.HttpReqManager;
import com.hm.iou.router.Router;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.event.LogoutEvent;
import com.hm.iou.sharedata.event.RealNameEvent;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.sharedata.model.IncomeEnum;
import com.hm.iou.sharedata.model.SexEnum;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.api.PersonApi;
import com.hm.iou.userinfo.bean.IsWXExistBean;
import com.hm.iou.userinfo.business.ProfileContract;
import com.hm.iou.userinfo.event.UpdateAvatarEvent;
import com.hm.iou.userinfo.event.UpdateEmailEvent;
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

/**
 * Created by hjy on 2018/5/23.
 */

public class ProfilePresenter extends MvpActivityPresenter<ProfileContract.View> implements ProfileContract.Presenter {

    private static final String EVENT_KEY_BIND_WX = "person_bind_weixin";

    private IWXAPI mWXApi;

    public ProfilePresenter(@NonNull Context context, @NonNull ProfileContract.View view) {
        super(context, view);
        EventBus.getDefault().register(this);
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
        showRealName(userInfo);
        showMobile(userInfo);
        showWeixin(userInfo);
        showEmail(userInfo);
        showCity(userInfo);
        showMainIncome(userInfo);
        updateProfileProgress(userInfo);
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
                        //更新进度
                        updateProfileProgress(userInfo);
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
        mView.showNickname(nickname);
    }

    /**
     * 显示实名状态
     *
     * @param userInfo
     */
    private void showRealName(UserInfo userInfo) {
        if (UserDataUtil.isCClass(userInfo.getType())) {
            //未实名
            mView.showRealNameFlag(View.GONE);
            mView.showRealName("未实名", 0xffff3c4b);
            mView.enableRealNameClick(true);
        } else {
            mView.showRealNameFlag(View.VISIBLE);
            mView.showRealName(userInfo.getName(), 0xffa3a3a3);
            mView.enableRealNameClick(false);
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
            mView.showWeixin("已绑定", 0xffa3a3a3);
        } else {
            mView.showWeixin("未绑定", 0xffff3c4b);
        }
    }

    /**
     * 显示邮箱
     *
     * @param userInfo
     */
    private void showEmail(UserInfo userInfo) {
        String email = userInfo.getMailAddr();
        if (TextUtils.isEmpty(email)) {
            mView.showEmail(View.GONE, "");
        } else {
            String s = email.length() >= 3 ? email.substring(0, 3) + "***" : email;
            mView.showEmail(View.VISIBLE, s);
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

    /**
     * 更新我的资料完成度
     *
     * @param userInfo
     */
    private void updateProfileProgress(UserInfo userInfo) {
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
        if (income >= IncomeEnum.None.getValue()) {
            count++;
        }
        if (count == 7) {
            //全部完成
            mView.showProfileProgress(100);
            mView.showProgressTips("太棒了，资料很完整！");
        } else {
            int progress = count * 15;
            mView.showProfileProgress(progress);
            mView.showProgressTips("已经完成" + progress + "%，加把劲！");
        }
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
        updateProfileProgress(userInfo);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdateNicknameAndSex(UpdateNicknameAndSexEvent event) {
        UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        showNickname(userInfo);
        updateProfileProgress(userInfo);
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
        updateProfileProgress(userInfo);
    }

    /**
     * 更新我的主要收入
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdateIncome(UpdateIncomeEvent event) {
        UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        showMainIncome(userInfo);
        updateProfileProgress(userInfo);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdateEmail(UpdateEmailEvent event) {
        UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        showEmail(userInfo);
        updateProfileProgress(userInfo);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventWeixinBind(UpdateWeixinEvent event) {
        UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        showWeixin(userInfo);
        updateProfileProgress(userInfo);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRealName(RealNameEvent event) {
        UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        showRealName(userInfo);
        updateProfileProgress(userInfo);
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
}