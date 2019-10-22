package com.hm.iou.userinfo.business.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.uikit.HMTopBarView;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;
import com.hm.iou.userinfo.business.UnbindWeixinContract;
import com.hm.iou.userinfo.business.presenter.UnbindWeixinPresenter;
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
public class UnbindWeixinActivity extends BaseActivity<UnbindWeixinPresenter> implements UnbindWeixinContract.View {

    @BindView(R2.id.topbar)
    HMTopBarView mTopBarView;
    @BindView(R2.id.et_password)
    EditText mEtPassword;
    @BindView(R2.id.btn_checkPassword)
    Button mBtnCheckPassword;

    private String mUserPassword;

    @Override
    protected int getLayoutId() {
        return R.layout.person_activity_unbind_weixin;
    }

    @Override
    protected UnbindWeixinPresenter initPresenter() {
        return new UnbindWeixinPresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
        mTopBarView.showDivider(false);

        RxTextView.textChanges(mEtPassword).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence charSequence) throws Exception {
                mUserPassword = String.valueOf(charSequence);
                if (mUserPassword.length() > 5) {
                    mBtnCheckPassword.setEnabled(true);
                } else {
                    mBtnCheckPassword.setEnabled(false);
                }
            }
        });
        mEtPassword.requestFocus();
        mEtPassword.postDelayed(new Runnable() {
            @Override
            public void run() {
                showSoftKeyboard();
            }
        }, 300);
    }

    @OnClick({R2.id.btn_checkPassword})
    public void onClick(View view) {
        if (view.getId() == R.id.btn_checkPassword) {
            mPresenter.unbindWeixin(mUserPassword);
        }
    }
}
