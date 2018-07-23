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
import com.hm.iou.userinfo.business.ChangeEmailContract;
import com.hm.iou.userinfo.event.UpdateEmailEvent;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by hjy on 2018/5/23.
 */

public class ChangeEmailPresenter extends MvpActivityPresenter<ChangeEmailContract.View> implements ChangeEmailContract.Presenter {

    private static final int VERIFY_CODE_PURPOSE_CHANGE_EMAIL = 5;

    public ChangeEmailPresenter(@NonNull Context context, @NonNull ChangeEmailContract.View view) {
        super(context, view);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void verifyPassword(final String email, String pwd) {
        mView.showLoadingView();
        PersonApi.verifyEmailAndPwd(email, Md5Util.getMd5ByString(pwd))
                .compose(getProvider().<BaseResponse<String>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<String>handleResponse())
                .subscribeWith(new CommSubscriber<String>(mView) {
                    @Override
                    public void handleResult(String data) {
                        mView.dismissLoadingView();
                        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/change_email_complete")
                                .withString("sn", data)
                                .withString("email", email)
                                .navigation(mContext);
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });
    }

    @Override
    public void sendVerifyCode(final String email) {
        if (!StringUtil.matchRegex(email, HMConstants.REG_EMAIL_NUMBER)) {
            mView.toastMessage("请输入正确的邮箱");
            return;
        }
        String oldEmail = UserManager.getInstance(mContext).getUserInfo().getMailAddr();
        if (email.equals(oldEmail)) {
            mView.toastMessage("新老邮箱不能相同");
            return;
        }
        mView.showLoadingView();
        PersonApi.sendMessage(VERIFY_CODE_PURPOSE_CHANGE_EMAIL, email)
                .compose(getProvider().<BaseResponse<String>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<String>handleResponse())
                .subscribeWith(new CommSubscriber<String>(mView) {
                    @Override
                    public void handleResult(String data) {
                        mView.dismissLoadingView();
                        String msg = "邮箱验证码已发送到您的" + email + "邮箱里。\n请到邮箱里查看";
                        mView.showVerifyCodeSendSuccDialog(msg);
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });
    }

    @Override
    public void changeEmail(String oldEmail, final String newEmail, String verifyCode, String sn) {
        mView.showLoadingView();
        PersonApi.changeEmail(oldEmail, newEmail, sn, verifyCode)
                .compose(getProvider().<BaseResponse<Object>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<Object>handleResponse())
                .subscribeWith(new CommSubscriber<Object>(mView) {
                    @Override
                    public void handleResult(Object data) {
                        mView.dismissLoadingView();

                        UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
                        userInfo.setMailAddr(newEmail);
                        UserManager.getInstance(mContext).updateOrSaveUserInfo(userInfo);

                        EventBus.getDefault().post(new UpdateEmailEvent());

                        mView.toastMessage("绑定成功");
                        mView.closeCurrPage();
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdateEmail(UpdateEmailEvent event) {
        mView.closeCurrPage();
    }

}
