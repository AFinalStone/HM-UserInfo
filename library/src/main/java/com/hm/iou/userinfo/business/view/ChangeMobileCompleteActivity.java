package com.hm.iou.userinfo.business.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.constants.HMConstants;
import com.hm.iou.tools.StringUtil;
import com.hm.iou.uikit.HMCountDownTextView;
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
public class ChangeMobileCompleteActivity extends BaseActivity<ChangeMobilePresenter> implements ChangeMobileContract.View {

    @BindView(R2.id.topbar)
    HMTopBarView mTopBarView;
    @BindView(R2.id.et_phone)
    EditText mEtPhone;
    @BindView(R2.id.et_code)
    EditText mEtCode;
    @BindView(R2.id.btn_finishChange)
    Button mBtnFinishChange;
    @BindView(R2.id.tv_getcode)
    HMCountDownTextView mCountDownView;

    private String mUserPwd;

    private String mUserPhone;
    private String mStrCode;

    @Override
    protected int getLayoutId() {
        return R.layout.person_activity_change_mobile_complete;
    }

    @Override
    protected ChangeMobilePresenter initPresenter() {
        return new ChangeMobilePresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
        Intent data = getIntent();
        mUserPwd = data.getStringExtra("pwd");
        if (savedInstanceState != null) {
            mUserPwd = savedInstanceState.getString("pwd");
        }

        mTopBarView.showDivider(false);

        RxTextView.textChanges(mEtPhone).subscribe(s -> {
            mUserPhone = String.valueOf(s);
            checkValue();
        });

        RxTextView.textChanges(mEtCode).subscribe(s -> {
            mStrCode = String.valueOf(s);
            checkValue();
        });

        mEtPhone.postDelayed(new Runnable() {
            @Override
            public void run() {
                showSoftKeyboard();
            }
        }, 300);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("pwd", mUserPwd);
    }

    private void checkValue() {
        mBtnFinishChange.setEnabled(false);
        if (StringUtil.matchRegex(mUserPhone, HMConstants.REG_MOBILE)) {
            if (mStrCode.length() > 0) {
                mBtnFinishChange.setEnabled(true);
            }
        }
    }

    @OnClick({R2.id.btn_finishChange, R2.id.tv_getcode})
    public void onClick(View view) {
        if (view.getId() == R.id.btn_finishChange) {
            mPresenter.changeMobile(mUserPhone, mStrCode, mUserPwd);
        } else if (view.getId() == R.id.tv_getcode) {
            mPresenter.sendVerifyCode(mUserPhone);
        }
    }

    @Override
    public void startCountDown() {
        mCountDownView.startCountDown();
    }
}
