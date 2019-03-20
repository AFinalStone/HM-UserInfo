package com.hm.iou.userinfo.business.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.userinfo.business.UserBankCardInfoContract;
import com.hm.iou.userinfo.business.UserEmailInfoContract;

/**
 * @author syl
 * @time 2019/3/19 1:41 PM
 */

public class UserEmailnfoPresenter extends MvpActivityPresenter<UserEmailInfoContract.View> implements UserEmailInfoContract.Presenter {


    public UserEmailnfoPresenter(@NonNull Context context, @NonNull UserEmailInfoContract.View view) {
        super(context, view);
    }

    @Override
    public void getUserEmailInfo() {

    }
}
