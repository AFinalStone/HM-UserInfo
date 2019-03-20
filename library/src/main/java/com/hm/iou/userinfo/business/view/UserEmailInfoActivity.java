package com.hm.iou.userinfo.business.view;

import android.os.Bundle;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.userinfo.bean.UserEmailInfoResBean;
import com.hm.iou.userinfo.business.UserEmailInfoContract;
import com.hm.iou.userinfo.business.presenter.UserEmailnfoPresenter;

/**
 * 我的邮箱
 *
 * @author syl
 * @time 2019/3/19 7:46 PM
 */
public class UserEmailInfoActivity extends BaseActivity<UserEmailnfoPresenter> implements UserEmailInfoContract.View {


    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected UserEmailnfoPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initEventAndData(Bundle bundle) {

    }

    @Override
    public void showBankCardInfo(UserEmailInfoResBean userEmailInfoResBean) {

    }
}
