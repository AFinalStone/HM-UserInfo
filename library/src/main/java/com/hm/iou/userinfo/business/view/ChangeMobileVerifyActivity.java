package com.hm.iou.userinfo.business.view;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.constants.HMConstants;
import com.hm.iou.tools.StringUtil;
import com.hm.iou.uikit.HMTopBarView;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;
import com.hm.iou.userinfo.business.ChangeMobileContract;
import com.hm.iou.userinfo.business.presenter.ChangeMobilePresenter;
import com.jakewharton.rxbinding2.widget.RxTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改登录密码
 *
 * @author AFinalStone
 * @time 2018/3/20 下午5:39
 */
public class ChangeMobileVerifyActivity extends BaseActivity<ChangeMobilePresenter> implements ChangeMobileContract.View {

    @BindView(R2.id.topbar)
    HMTopBarView mTopBarView;
    @BindView(R2.id.et_phone)
    EditText mEtPhone;
    @BindView(R2.id.et_password)
    EditText mEtPassword;
    @BindView(R2.id.btn_checkPassword)
    TextView mBtnCheckPassword;

    private String mUserPhone;
    private String mUserPassword;

    @Override
    protected int getLayoutId() {
        return R.layout.person_activity_change_mobile_verify;
    }

    @Override
    protected ChangeMobilePresenter initPresenter() {
        return new ChangeMobilePresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
        mTopBarView.showDivider(false);

        RxTextView.textChanges(mEtPhone).subscribe(s -> {
            mUserPhone = String.valueOf(s);
            mBtnCheckPassword.setEnabled(false);
            if (StringUtil.matchRegex(mUserPhone, HMConstants.REG_MOBILE)) {
                if (mUserPassword.length() > 0) {
                    mBtnCheckPassword.setEnabled(true);
                }
            }

        });

        RxTextView.textChanges(mEtPassword).subscribe(s -> {
            mUserPassword = String.valueOf(s);
            mBtnCheckPassword.setEnabled(false);
            if (StringUtil.matchRegex(mUserPhone, HMConstants.REG_MOBILE)) {
                if (mUserPassword.length() > 0) {
                    mBtnCheckPassword.setEnabled(true);
                }
            }
        });

        mEtPhone.postDelayed(new Runnable() {
            @Override
            public void run() {
                showSoftKeyboard();
            }
        }, 300);

    }

    @OnClick({R2.id.btn_checkPassword})
    public void onClick(View view) {
        if (view.getId() == R.id.btn_checkPassword) {
            mPresenter.verifyPassword(mUserPhone, mUserPassword);
        }
    }

    @Override
    public void startCountDown() {

    }
}
