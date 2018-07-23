package com.hm.iou.userinfo.business.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.iou.base.constants.HMConstants;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;

import com.hm.iou.router.Router;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.tools.Md5Util;
import com.hm.iou.tools.StringUtil;
import com.hm.iou.userinfo.api.PersonApi;
import com.hm.iou.userinfo.business.ChangeMobileContract;
import com.hm.iou.userinfo.event.UpdateMobileEvent;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by hjy on 2018/5/23.
 */

public class ChangeMobilePresenter extends MvpActivityPresenter<ChangeMobileContract.View> implements ChangeMobileContract.Presenter {

    private static final int VERIFY_CODE_PURPOSE_CHANGE_MOBILE = 3;

    public ChangeMobilePresenter(@NonNull Context context, @NonNull ChangeMobileContract.View view) {
        super(context, view);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void verifyPassword(String mobile, final String pwd) {
        mView.showLoadingView();
        PersonApi.verifyPwdWhenChangeMobile(mobile, Md5Util.getMd5ByString(pwd))
                .compose(getProvider().<BaseResponse<Object>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<Object>handleResponse())
                .subscribeWith(new CommSubscriber<Object>(mView) {
                    @Override
                    public void handleResult(Object data) {
                        mView.dismissLoadingView();
                        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/change_mobile_complete")
                                .withString("pwd", pwd)
                                .navigation(mContext);
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });
    }

    @Override
    public void sendVerifyCode(String mobile) {
        if (!StringUtil.matchRegex(mobile, HMConstants.REG_MOBILE)) {
            mView.toastMessage("请输入正确的手机号");
            return;
        }
        String oldMobile = UserManager.getInstance(mContext).getUserInfo().getMobile();
        if (mobile.equals(oldMobile)) {
            mView.toastMessage("新老手机号不能相同");
            return;
        }
        mView.showLoadingView();
        PersonApi.sendMessage(VERIFY_CODE_PURPOSE_CHANGE_MOBILE, mobile)
                .compose(getProvider().<BaseResponse<String>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<String>handleResponse())
                .subscribeWith(new CommSubscriber<String>(mView) {
                    @Override
                    public void handleResult(String data) {
                        mView.dismissLoadingView();
                        mView.startCountDown();
                        mView.toastMessage("验证码获取成功");
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });
    }

    @Override
    public void changeMobile(final String newMobile, String verifyCode, String password) {
        mView.showLoadingView();
        PersonApi.changeMobile(newMobile, verifyCode, UserManager.getInstance(mContext).getUserInfo().getMobile(), Md5Util.getMd5ByString(password))
                .compose(getProvider().<BaseResponse<Object>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<Object>handleResponse())
                .subscribeWith(new CommSubscriber<Object>(mView) {
                    @Override
                    public void handleResult(Object data) {
                        mView.dismissLoadingView();

                        UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
                        userInfo.setMobile(newMobile);
                        UserManager.getInstance(mContext).updateOrSaveUserInfo(userInfo);
                        EventBus.getDefault().post(new UpdateMobileEvent());

                        mView.toastMessage("更换成功");
                        mView.closeCurrPage();
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdateMobile(UpdateMobileEvent event) {
        mView.closeCurrPage();
    }

}
