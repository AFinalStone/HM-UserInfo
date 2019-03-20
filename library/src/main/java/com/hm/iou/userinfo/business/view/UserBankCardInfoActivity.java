package com.hm.iou.userinfo.business.view;

import android.os.Bundle;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.userinfo.bean.UserBankCardInfoResBean;
import com.hm.iou.userinfo.business.UserBankCardInfoContract;
import com.hm.iou.userinfo.business.presenter.UserBankCardInfoPresenter;

/**
 * 我的银行卡
 *
 * @author syl
 * @time 2019/3/19 7:46 PM
 */
public class UserBankCardInfoActivity extends BaseActivity<UserBankCardInfoPresenter> implements UserBankCardInfoContract.View {


    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected UserBankCardInfoPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initEventAndData(Bundle bundle) {

    }

    @Override
    public void showBankCardInfo(UserBankCardInfoResBean userBankCardInfoResBean) {

    }
}
