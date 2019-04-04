package com.hm.iou.userinfo.business.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.uikit.ClearEditText;
import com.hm.iou.uikit.HMCountDownTextView;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;
import com.hm.iou.userinfo.business.ForeverUnRegisterCheckUserInfoContract;
import com.hm.iou.userinfo.business.presenter.ForeverUnRegisterCheckUserInfoPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author syl
 * @time 2019/4/4 7:08 PM
 */

public class ForeverUnRegisterCheckUserInfoActivity extends BaseActivity<ForeverUnRegisterCheckUserInfoPresenter> implements ForeverUnRegisterCheckUserInfoContract.View {


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

    @Override
    protected int getLayoutId() {
        return R.layout.person_activity_forever_unregister_check_userinfo;
    }

    @Override
    protected ForeverUnRegisterCheckUserInfoPresenter initPresenter() {
        return new ForeverUnRegisterCheckUserInfoPresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
    }

    @OnClick({R2.id.tv_get_code, R2.id.btn_quit_and_unregister})
    public void onClick(View view) {
        int id = view.getId();
        if (R.id.tv_get_code == id) {

        } else if (R.id.btn_quit_and_unregister == id) {
        }
    }
}
