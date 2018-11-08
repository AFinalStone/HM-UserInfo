package com.hm.iou.userinfo.business.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.constants.HMConstants;
import com.hm.iou.router.Router;
import com.hm.iou.tools.StringUtil;
import com.hm.iou.uikit.HMCountDownTextView;
import com.hm.iou.uikit.HMTopBarView;
import com.hm.iou.uikit.dialog.IOSAlertDialog;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;
import com.hm.iou.userinfo.business.ChangeEmailContract;
import com.hm.iou.userinfo.business.presenter.ChangeEmailPresenter;
import com.jakewharton.rxbinding2.widget.RxTextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * 更换邮箱
 *
 * @author AFinalStone
 * @time 2018/3/20 下午5:39
 */
public class ChangeEmailCompleteActivity extends BaseActivity<ChangeEmailPresenter> implements ChangeEmailContract.View {

    @BindView(R2.id.topBar)
    HMTopBarView mTopBar;
    @BindView(R2.id.et_phone)
    EditText mEtEmail;
    @BindView(R2.id.et_code)
    EditText mEtCode;
    @BindView(R2.id.btn_finishChange)
    Button mBtnFinishChange;
    @BindView(R2.id.tv_getEmailCode)
    HMCountDownTextView mTvGetEmailCode;

    private String mOldEmail;
    private String mVerifySn;

    private String mUserEmail;
    private String mStrCode;

    @Override
    protected int getLayoutId() {
        return R.layout.person_activity_change_email_complete;
    }

    @Override
    protected ChangeEmailPresenter initPresenter() {
        return new ChangeEmailPresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
        Intent data = getIntent();
        mVerifySn = data.getStringExtra("sn");
        mOldEmail = data.getStringExtra("email");

        if (savedInstanceState != null) {
            mVerifySn = savedInstanceState.getString("sn");
            mOldEmail = savedInstanceState.getString("email");
        }

        mTopBar.setOnMenuClickListener(new HMTopBarView.OnTopBarMenuClickListener() {
            @Override
            public void onClickTextMenu() {
                Router.getInstance()
                        .buildWithUrl("hmiou://m.54jietiao.com/login/customer_service")
                        .navigation(mContext);
            }

            @Override
            public void onClickImageMenu() {

            }
        });
        RxTextView.textChanges(mEtEmail).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence charSequence) throws Exception {
                mUserEmail = String.valueOf(charSequence);
                checkValue();
            }
        });

        RxTextView.textChanges(mEtCode).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence charSequence) throws Exception {
                mStrCode = String.valueOf(charSequence);
                checkValue();
            }
        });

        mEtEmail.postDelayed(new Runnable() {
            @Override
            public void run() {
                showSoftKeyboard();
            }
        }, 300);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("sn", mVerifySn);
        outState.putString("email", mOldEmail);
    }

    private void checkValue() {
        mBtnFinishChange.setEnabled(false);
        if (StringUtil.matchRegex(mUserEmail, HMConstants.REG_EMAIL_NUMBER)) {
            if (mStrCode.length() > 0) {
                mBtnFinishChange.setEnabled(true);
            }
        }
    }

    @OnClick({R2.id.btn_finishChange, R2.id.tv_getcode})
    public void onClick(View view) {
        if (view.getId() == R.id.btn_finishChange) {
            mPresenter.changeEmail(mOldEmail, mUserEmail, mStrCode, mVerifySn);
        } else if (view.getId() == R.id.tv_getcode) {
            mPresenter.sendVerifyCode(mUserEmail);
        }
    }

    @Override
    public void startCountDown() {
        mTvGetEmailCode.startCountDown();
    }

}
