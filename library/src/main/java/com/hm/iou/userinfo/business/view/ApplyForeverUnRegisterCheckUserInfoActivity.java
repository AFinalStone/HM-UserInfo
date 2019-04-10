package com.hm.iou.userinfo.business.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.constants.HMConstants;
import com.hm.iou.tools.StringUtil;
import com.hm.iou.uikit.ClearEditText;
import com.hm.iou.uikit.HMCountDownTextView;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;
import com.hm.iou.userinfo.business.ApplyForeverUnRegisterCheckUserInfoContract;
import com.hm.iou.userinfo.business.presenter.ApplyForeverUnRegisterCheckUserInfoPresenter;
import com.hm.iou.userinfo.common.HMTextWatcher;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author syl
 * @time 2019/4/4 7:08 PM
 */

public class ApplyForeverUnRegisterCheckUserInfoActivity extends BaseActivity<ApplyForeverUnRegisterCheckUserInfoPresenter> implements ApplyForeverUnRegisterCheckUserInfoContract.View {


    @BindView(R2.id.et_phone)
    ClearEditText mEtPhone;
    @BindView(R2.id.et_psd)
    ClearEditText mEtPsd;
    @BindView(R2.id.et_check_code)
    ClearEditText mEtCheckCode;
    @BindView(R2.id.tv_get_code)
    HMCountDownTextView mTvGetCode;
    @BindView(R2.id.btn_quit_and_unregister)
    Button mBtnQuitAndUnregister;
    @BindView(R2.id.btn_forever_delete)
    Button mBtnForeverDelete;

    private String mMobile;
    private String mPwd;
    private String mCheckCode;

    @Override
    protected int getLayoutId() {
        return R.layout.person_activity_apply_forever_unregister_check_userinfo;
    }

    @Override
    protected ApplyForeverUnRegisterCheckUserInfoPresenter initPresenter() {
        return new ApplyForeverUnRegisterCheckUserInfoPresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mEtPhone.addTextChangedListener(new HMTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                mMobile = String.valueOf(charSequence);
                if (!StringUtil.matchRegex(mMobile, HMConstants.REG_MOBILE)) {
                    mTvGetCode.setEnabled(false);
                    mBtnForeverDelete.setEnabled(false);
                    return;
                }
                mTvGetCode.setEnabled(true);
                checkValue();
            }
        });
        mEtPsd.addTextChangedListener(new HMTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                mPwd = String.valueOf(charSequence);
                checkValue();
            }
        });
        mEtCheckCode.addTextChangedListener(new HMTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                mCheckCode = String.valueOf(charSequence);
                checkValue();
            }
        });
    }

    @OnClick({R2.id.tv_get_code, R2.id.btn_quit_and_unregister, R2.id.btn_forever_delete})
    public void onClick(View view) {
        int id = view.getId();
        if (R.id.tv_get_code == id) {
            mPresenter.getCheckCode(mMobile);
        } else if (R.id.btn_quit_and_unregister == id) {
            mPresenter.loginOut();
        } else if (R.id.btn_forever_delete == id) {
            mPresenter.foreverUnRegister(mMobile, mPwd, mCheckCode);
        }
    }

    @Override
    public void startCountDown() {
        mTvGetCode.startCountDown();
    }

    private void checkValue() {
        if (StringUtil.matchRegex(mMobile, HMConstants.REG_MOBILE) && !TextUtils.isEmpty(mPwd)
                && mPwd.length() >= 6
                && !TextUtils.isEmpty(mCheckCode)
                && mCheckCode.length() >= 6) {
            mBtnForeverDelete.setEnabled(true);
            return;
        }
        mBtnForeverDelete.setEnabled(false);
    }
}
