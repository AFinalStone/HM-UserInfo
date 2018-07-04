package com.hm.iou.userinfo.business.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.uikit.HMTopBarView;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;
import com.hm.iou.userinfo.business.ModifyPasswordContract;
import com.hm.iou.userinfo.business.presenter.ModifyPasswordPresenter;
import com.jakewharton.rxbinding2.widget.RxTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改登录密码
 *
 * @author AFinalStone
 * @time 2018/3/20 下午5:39
 */
public class ModifyPasswordActivity extends BaseActivity<ModifyPasswordPresenter> implements ModifyPasswordContract.View {

    @BindView(R2.id.topbar)
    HMTopBarView mTopBarView;
    @BindView(R2.id.et_oldPassword)
    EditText mEtOldPassword;
    @BindView(R2.id.iv_eyeOld)
    ImageView mIvEyeOld;
    @BindView(R2.id.et_newPassword)
    EditText mEtNewPassword;
    @BindView(R2.id.iv_eyeNew)
    ImageView mIvEyeNew;
    @BindView(R2.id.btn_queryChange)
    Button mBtnQueryChange;

    private String mOldPassword;
    private String mNewPassword;
    private boolean mIsEyeOldOpen = false;
    private boolean mIsEyeNewOpen = false;

    @Override
    protected int getLayoutId() {
        return R.layout.person_activity_modify_pwd;
    }

    @Override
    protected ModifyPasswordPresenter initPresenter() {
        return new ModifyPasswordPresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
        mTopBarView.showDivider(false);
        RxTextView.textChanges(mEtOldPassword).subscribe(s -> {
            mOldPassword = String.valueOf(s);
            checkValue();
        });

        RxTextView.textChanges(mEtNewPassword).subscribe(s -> {
            mNewPassword = String.valueOf(s);
            checkValue();
        });

        mEtOldPassword.postDelayed(new Runnable() {
            @Override
            public void run() {
                showSoftKeyboard();
            }
        }, 300);

        //默认新密码显示
        changeEyeNew();
    }

    private void checkValue() {
        mBtnQueryChange.setEnabled(false);
        if (mOldPassword.length() > 5 && mNewPassword.length() > 5) {
            mBtnQueryChange.setEnabled(true);
        }
    }

    @OnClick({R2.id.iv_eyeOld, R2.id.iv_eyeNew, R2.id.btn_queryChange})
    public void onClick(View view) {
        if (view.getId() == R.id.iv_eyeOld) {
            changeEyeOld();
        } else if (view.getId() == R.id.iv_eyeNew) {
            changeEyeNew();
        } else if(view.getId() == R.id.btn_queryChange) {
            mPresenter.modifyPwd(mOldPassword, mNewPassword);
        }
    }

    private void changeEyeOld() {
        if (mIsEyeOldOpen) {
            mIsEyeOldOpen = false;
            mIvEyeOld.setImageResource(R.mipmap.uikit_icon_password_eye_close);
            // 显示为普通文本
            mEtOldPassword.setInputType(InputType.TYPE_CLASS_TEXT
                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            // 使光标始终在最后位置
            Editable etable = mEtOldPassword.getText();
            Selection.setSelection(etable, etable.length());
        } else {
            mIsEyeOldOpen = true;
            mIvEyeOld.setImageResource(R.mipmap.uikit_icon_password_eye_open);
            // 显示为密码
            mEtOldPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            // 使光标始终在最后位置
            Editable etable = mEtOldPassword.getText();
            Selection.setSelection(etable, etable.length());
        }
    }

    private void changeEyeNew() {
        if (mIsEyeNewOpen) {
            mIsEyeNewOpen = false;
            mIvEyeNew.setImageResource(R.mipmap.uikit_icon_password_eye_close);
            // 显示为普通文本
            mEtNewPassword.setInputType(InputType.TYPE_CLASS_TEXT
                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            // 使光标始终在最后位置
            Editable etable = mEtNewPassword.getText();
            Selection.setSelection(etable, etable.length());
        } else {
            mIsEyeNewOpen = true;
            mIvEyeNew.setImageResource(R.mipmap.uikit_icon_password_eye_open);
            // 显示为密码
            mEtNewPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            // 使光标始终在最后位置
            Editable etable = mEtNewPassword.getText();
            Selection.setSelection(etable, etable.length());
        }
    }

}
