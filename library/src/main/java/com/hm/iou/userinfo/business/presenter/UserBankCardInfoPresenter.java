package com.hm.iou.userinfo.business.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.userinfo.api.PersonApi;
import com.hm.iou.userinfo.bean.UserAuthenticationInfoResBean;
import com.hm.iou.userinfo.bean.UserBankCardInfoResBean;
import com.hm.iou.userinfo.business.ChangeAliPayContract;
import com.hm.iou.userinfo.business.UserBankCardInfoContract;
import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * @author syl
 * @time 2019/3/19 1:41 PM
 */

public class UserBankCardInfoPresenter extends MvpActivityPresenter<UserBankCardInfoContract.View> implements UserBankCardInfoContract.Presenter {


    public UserBankCardInfoPresenter(@NonNull Context context, @NonNull UserBankCardInfoContract.View view) {
        super(context, view);
    }

    @Override
    public void getBankCardInfo() {
        mView.showLoadingView();
        PersonApi.getUserBindBankCardInfo()
                .compose(getProvider().<BaseResponse<UserBankCardInfoResBean>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<UserBankCardInfoResBean>handleResponse())
                .subscribeWith(new CommSubscriber<UserBankCardInfoResBean>(mView) {
                    @Override
                    public void handleResult(UserBankCardInfoResBean infoBean) {
                        mView.dismissLoadingView();
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
