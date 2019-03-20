package com.hm.iou.userinfo.business.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.sharedata.event.BindBankSuccessEvent;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.userinfo.api.PersonApi;
import com.hm.iou.userinfo.bean.UserAuthenticationInfoResBean;
import com.hm.iou.userinfo.bean.UserBankCardInfoResBean;
import com.hm.iou.userinfo.business.ChangeAliPayContract;
import com.hm.iou.userinfo.business.UserBankCardInfoContract;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author syl
 * @time 2019/3/19 1:41 PM
 */

public class UserBankCardInfoPresenter extends MvpActivityPresenter<UserBankCardInfoContract.View> implements UserBankCardInfoContract.Presenter {

    private boolean mNeedRefresh = true;

    public UserBankCardInfoPresenter(@NonNull Context context, @NonNull UserBankCardInfoContract.View view) {
        super(context, view);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void getBankCardInfo() {
        if (mNeedRefresh) {
            mView.showLoadingView();
            PersonApi.getUserBindBankCardInfo()
                    .compose(getProvider().<BaseResponse<UserBankCardInfoResBean>>bindUntilEvent(ActivityEvent.DESTROY))
                    .map(RxUtil.<UserBankCardInfoResBean>handleResponse())
                    .subscribeWith(new CommSubscriber<UserBankCardInfoResBean>(mView) {
                        @Override
                        public void handleResult(UserBankCardInfoResBean infoBean) {
                            mView.dismissLoadingView();
                            mNeedRefresh = false;
                            mView.showBankCardInfo(infoBean);
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
     * 银行卡绑定成功
     *
     * @param bindBankSuccessEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvenBindBankSuccess(BindBankSuccessEvent bindBankSuccessEvent) {
        mNeedRefresh = true;
    }

}
