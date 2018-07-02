package com.hm.iou.userinfo.business.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.constants.HMConstants;
import com.hm.iou.tools.StringUtil;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;
import com.hm.iou.userinfo.business.CloseAccountContract;
import com.hm.iou.userinfo.business.presenter.CloseAccountPresenter;
import com.jakewharton.rxbinding2.widget.RxTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hjy on 2018/6/4.
 */

public class CloseAccountActivity extends BaseActivity<CloseAccountPresenter> implements CloseAccountContract.View {

    @BindView(R2.id.et_phone)
    EditText mEtPhone;
    @BindView(R2.id.et_password)
    EditText mEtPassword;
    @BindView(R2.id.btn_close_account)
    Button mBtnCloseAccount;

    private String mUserPhone;
    private String mUserPassword;

    @Override
    protected int getLayoutId() {
        return R.layout.person_activity_close_account;
    }

    @Override
    protected CloseAccountPresenter initPresenter() {
        return new CloseAccountPresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        RxTextView.textChanges(mEtPhone).subscribe(s -> {
            mUserPhone = String.valueOf(s);
            checkValue();
        });

        RxTextView.textChanges(mEtPassword).subscribe(s -> {
            mUserPassword = String.valueOf(s);
            checkValue();
        });

        mEtPhone.postDelayed(new Runnable() {
            @Override
            public void run() {
                showSoftKeyboard();
            }
        }, 300);
    }

    private void checkValue() {
        mBtnCloseAccount.setEnabled(false);
        if (StringUtil.matchRegex(mUserPhone, HMConstants.REG_MOBILE)) {
            if (mUserPassword.length() > 5) {
                mBtnCloseAccount.setEnabled(true);
            }
        }
    }

    @OnClick({R2.id.btn_close_account, R2.id.btn_logout})
    public void onClick(View view) {
        if (view.getId() == R.id.btn_close_account) {
            mPresenter.closeAccount(mUserPhone, mUserPassword);
        } else if (view.getId() == R.id.btn_logout) {
            mPresenter.doLogout();
        }
    }

}
