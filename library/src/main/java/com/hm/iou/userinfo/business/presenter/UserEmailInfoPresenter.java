package com.hm.iou.userinfo.business.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.userinfo.api.PersonApi;
import com.hm.iou.userinfo.bean.UserBankCardInfoResBean;
import com.hm.iou.userinfo.bean.UserEmailInfoResBean;
import com.hm.iou.userinfo.business.UserBankCardInfoContract;
import com.hm.iou.userinfo.business.UserEmailInfoContract;
import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * @author syl
 * @time 2019/3/19 1:41 PM
 */

public class UserEmailInfoPresenter extends MvpActivityPresenter<UserEmailInfoContract.View> implements UserEmailInfoContract.Presenter {


    public UserEmailInfoPresenter(@NonNull Context context, @NonNull UserEmailInfoContract.View view) {
        super(context, view);
    }

    @Override
    public void getUserEmailInfo() {
        mView.showLoadingView();
        PersonApi.getUserBindEmailInfo()
                .compose(getProvider().<BaseResponse<UserEmailInfoResBean>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<UserEmailInfoResBean>handleResponse())
                .subscribeWith(new CommSubscriber<UserEmailInfoResBean>(mView) {
                    @Override
                    public void handleResult(UserEmailInfoResBean infoBean) {
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
