package com.hm.iou.userinfo.business.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.constants.HMConstants;
import com.hm.iou.tools.StringUtil;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;
import com.hm.iou.userinfo.business.ChangeAliPayContract;
import com.hm.iou.userinfo.business.presenter.ChangeAliPayPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改支付宝账号
 *
 * @author AFinalStone
 * @time 2018/3/20 下午5:39
 */
public class ChangeAliPayActivity extends BaseActivity<ChangeAliPayPresenter> implements ChangeAliPayContract.View {

    public static final String EXTRA_KEY_ALIPAY_ACCOUNT = "alipay_account";

    @BindView(R2.id.et_alipay)
    EditText mEtAliPay;
    @BindView(R2.id.btn_save)
    Button mBtnSave;

    private String mOldAliPay;

    @Override
    protected int getLayoutId() {
        return R.layout.person_activity_change_alipay;
    }

    @Override
    protected ChangeAliPayPresenter initPresenter() {
        return new ChangeAliPayPresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mOldAliPay = getIntent().getStringExtra(EXTRA_KEY_ALIPAY_ACCOUNT);
        if (bundle != null) {
            mOldAliPay = bundle.getString(EXTRA_KEY_ALIPAY_ACCOUNT);
        }
        if (!TextUtils.isEmpty(mOldAliPay)) {
            mEtAliPay.setText(mOldAliPay);
            saveAliPaySuccess();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_KEY_ALIPAY_ACCOUNT, mOldAliPay);
    }

    @Override
    public void saveAliPaySuccess() {
        mEtAliPay.setEnabled(false);
        mBtnSave.setBackgroundResource(R.drawable.uikit_selector_btn_minor_small);
        mBtnSave.setText("编辑");
    }

    @OnClick(R2.id.btn_save)
    public void onClick() {
        if ("编辑".equals(mBtnSave.getText().toString())) {
            mEtAliPay.setEnabled(true);
            mBtnSave.setBackgroundResource(R.drawable.uikit_selector_btn_main_small);
            mBtnSave.setText("保存");
            return;
        }
        String account = mEtAliPay.getText().toString();
        if (TextUtils.isEmpty(account)) {
            toastErrorMessage("支付宝账号不能为空");
            return;
        }
        if (account.equals(mOldAliPay)) {
            toastErrorMessage("当前账号未做任何修改，无法保存");
            return;
        }
        if (StringUtil.matchRegex(String.valueOf(account), HMConstants.REG_EMAIL_NUMBER)
                || StringUtil.matchRegex(String.valueOf(account), HMConstants.REG_MOBILE)) {
            mPresenter.saveAliPay(mEtAliPay.getText().toString());
        } else {
            toastErrorMessage("请输入正确的支付宝账号");
        }
    }
}
