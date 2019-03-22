package com.hm.iou.userinfo.business.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.sharedata.event.RealNameEvent;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.userinfo.api.PersonApi;
import com.hm.iou.userinfo.bean.UserAuthenticationInfoResBean;
import com.hm.iou.userinfo.business.AuthenticationInfoContract;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * @author syl
 * @time 2019/3/19 7:45 PM
 */
public class AuthenticationInfoPresenter extends MvpActivityPresenter<AuthenticationInfoContract.View> implements AuthenticationInfoContract.Presenter {

    private boolean mNeedRefresh = true;

    public AuthenticationInfoPresenter(@NonNull Context context, @NonNull AuthenticationInfoContract.View view) {
        super(context, view);
    }

    @Override
    public void getAuthenticationInfo() {
        if (mNeedRefresh) {
            mView.showLoadingView();
            PersonApi.getRealNameInfo()
                    .compose(getProvider().<BaseResponse<UserAuthenticationInfoResBean>>bindUntilEvent(ActivityEvent.DESTROY))
                    .map(RxUtil.<UserAuthenticationInfoResBean>handleResponse())
                    .subscribeWith(new CommSubscriber<UserAuthenticationInfoResBean>(mView) {
                        @Override
                        public void handleResult(UserAuthenticationInfoResBean infoBean) {
                            mView.dismissLoadingView();
                            mNeedRefresh = false;
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

    /**
     * 实名认证成功
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRealName(RealNameEvent event) {
        mNeedRefresh = true;
    }

}