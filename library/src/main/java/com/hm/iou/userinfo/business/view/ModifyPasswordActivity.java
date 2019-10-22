package com.hm.iou.userinfo.business.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.uikit.HMTopBarView;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;
import com.hm.iou.userinfo.business.ModifyPasswordContract;
import com.hm.iou.userinfo.business.presenter.ModifyPasswordPresenter;
import com.jakewharton.rxbinding2.widget.RxTextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

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
    @BindView(R2.id.et_newPassword)
    EditText mEtNewPassword;
    @BindView(R2.id.btn_queryChange)
    Button mBtnQueryChange;

    private String mOldPassword;
    private String mNewPassword;

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
        RxTextView.textChanges(mEtOldPassword).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence charSequence) throws Exception {
                mOldPassword = String.valueOf(charSequence);
                checkValue();
            }
        });

        RxTextView.textChanges(mEtNewPassword).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence charSequence) throws Exception {
                mNewPassword = String.valueOf(charSequence);
                checkValue();
            }
        });

        mEtOldPassword.requestFocus();
        mEtOldPassword.postDelayed(new Runnable() {
            @Override
            public void run() {
                showSoftKeyboard();
            }
        }, 300);

    }

    private void checkValue() {
        if (mOldPassword.length() > 5 && mNewPassword.length() > 5) {
            mBtnQueryChange.setEnabled(true);
        } else {
            mBtnQueryChange.setEnabled(false);
        }
    }

    @OnClick({R2.id.btn_queryChange})
    public void onClick(View view) {
        if (view.getId() == R.id.btn_queryChange) {
            mPresenter.modifyPwd(mOldPassword, mNewPassword);
        }
    }

}
