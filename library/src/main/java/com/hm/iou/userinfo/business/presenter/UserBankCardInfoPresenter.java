package com.hm.iou.userinfo.business.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.userinfo.api.PersonApi;
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

    }
}
