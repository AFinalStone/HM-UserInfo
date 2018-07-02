package com.hm.iou.userinfo.business.view;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.constants.HMConstants;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.tools.StringUtil;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;
import com.hm.iou.userinfo.business.ChangeEmailContract;
import com.hm.iou.userinfo.business.presenter.ChangeEmailPresenter;
import com.jakewharton.rxbinding2.widget.RxTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 校验邮箱密码
 *
 * @author AFinalStone
 * @time 2018/3/20 下午5:39
 */
public class ChangeEmailVerifyActivity extends BaseActivity<ChangeEmailPresenter> implements ChangeEmailContract.View {

    @BindView(R2.id.et_phone)
    EditText mEtEmail;
    @BindView(R2.id.et_password)
    EditText mEtPassword;
    @BindView(R2.id.btn_checkPassword)
    TextView mBtnCheckPassword;

    private String mUserEmail;
    private String mUserPassword;

    @Override
    protected int getLayoutId() {
        return R.layout.person_activity_change_email_verify;
    }

    @Override
    protected ChangeEmailPresenter initPresenter() {
        return new ChangeEmailPresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
        String email = StringUtil.getUnnullString(UserManager.getInstance(this).getUserInfo().getMailAddr());
        mEtEmail.setHint(String.format("提示%s***邮箱", email.length() >= 3 ? email.substring(0, 3) : email));
        RxTextView.textChanges(mEtEmail).subscribe(s -> {
            mUserEmail = String.valueOf(s);
            mBtnCheckPassword.setEnabled(false);
            if (StringUtil.matchRegex(mUserEmail, HMConstants.REG_EMAIL_NUMBER)) {
                if (mUserPassword.length() > 0) {
                    mBtnCheckPassword.setEnabled(true);
                }
            }

        });

        RxTextView.textChanges(mEtPassword).subscribe(s -> {
            mUserPassword = String.valueOf(s);
            mBtnCheckPassword.setEnabled(false);
            if (StringUtil.matchRegex(mUserEmail, HMConstants.REG_EMAIL_NUMBER)) {
                if (mUserPassword.length() > 0) {
                    mBtnCheckPassword.setEnabled(true);
                }
            }
        });

        mEtEmail.postDelayed(new Runnable() {
            @Override
            public void run() {
                showSoftKeyboard();
            }
        }, 300);

    }

    @OnClick({R2.id.btn_checkPassword})
    public void onClick(View view) {
        if (view.getId() == R.id.btn_checkPassword) {
            mPresenter.verifyPassword(mUserEmail, mUserPassword);
        }
    }

    @Override
    public void startCountDown() {

    }
}
