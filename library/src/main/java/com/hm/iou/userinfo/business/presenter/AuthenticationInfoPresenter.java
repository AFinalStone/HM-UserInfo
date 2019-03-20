package com.hm.iou.userinfo.business.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.base.version.CheckVersionResBean;
import com.hm.iou.base.version.VersionApi;
import com.hm.iou.router.Router;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.userinfo.api.PersonApi;
import com.hm.iou.userinfo.bean.UserAuthenticationInfoBean;
import com.hm.iou.userinfo.business.AboutUsContract;
import com.hm.iou.userinfo.business.AuthenticationInfoContract;
import com.trello.rxlifecycle2.android.ActivityEvent;


/**
 * @author syl
 * @time 2019/3/19 7:45 PM
 */
public class AuthenticationInfoPresenter extends MvpActivityPresenter<AuthenticationInfoContract.View> implements AuthenticationInfoContract.Presenter {

    public AuthenticationInfoPresenter(@NonNull Context context, @NonNull AuthenticationInfoContract.View view) {
        super(context, view);
    }

    @Override
    public void getAuthenticationInfo() {
        mView.showLoadingView();
        PersonApi.getRealNameInfo()
                .compose(getProvider().<BaseResponse<UserAuthenticationInfoBean>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<UserAuthenticationInfoBean>handleResponse())
                .subscribeWith(new CommSubscriber<UserAuthenticationInfoBean>(mView) {
                    @Override
                    public void handleResult(UserAuthenticationInfoBean infoBean) {
                        mView.dismissLoadingView();
                        mView.showAuthenticationInfo(infoBean);
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                        mView.closeCurrPage();
                    }
                });
    }
}