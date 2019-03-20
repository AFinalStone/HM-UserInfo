package com.hm.iou.userinfo.business.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.userinfo.business.ChangeAliPayContract;

/**
 * @author syl
 * @time 2019/3/19 1:41 PM
 */

public class ChangeAliPayPresenter extends MvpActivityPresenter<ChangeAliPayContract.View> implements ChangeAliPayContract.Presenter {


    public ChangeAliPayPresenter(@NonNull Context context, @NonNull ChangeAliPayContract.View view) {
        super(context, view);
    }

    @Override
    public void getAliPay() {
        mView.showAliPay("");
    }

    @Override
    public void saveAliPay(String aliPay) {
        mView.toastMessage("保存成功");
        mView.showAliPay(aliPay);
        mView.saveAliPaySuccess();
    }
}
