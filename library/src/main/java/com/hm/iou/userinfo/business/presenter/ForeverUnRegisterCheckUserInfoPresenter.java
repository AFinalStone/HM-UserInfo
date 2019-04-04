package com.hm.iou.userinfo.business.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.userinfo.business.ForeverUnRegisterCheckUserInfoContract;


/**
 * @author syl
 * @time 2018/7/23 下午5:51
 */
public class ForeverUnRegisterCheckUserInfoPresenter extends MvpActivityPresenter<ForeverUnRegisterCheckUserInfoContract.View> implements ForeverUnRegisterCheckUserInfoContract.Presenter {

    public ForeverUnRegisterCheckUserInfoPresenter(@NonNull Context context, @NonNull ForeverUnRegisterCheckUserInfoContract.View view) {
        super(context, view);
    }

    @Override
    public void foreverUnRegister() {

    }
}