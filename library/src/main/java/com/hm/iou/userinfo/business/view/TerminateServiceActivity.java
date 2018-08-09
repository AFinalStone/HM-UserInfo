package com.hm.iou.userinfo.business.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.utils.TraceUtil;
import com.hm.iou.router.Router;
import com.hm.iou.uikit.SmoothCheckBox;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;
import com.hm.iou.userinfo.business.presenter.CloseAccountPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hjy on 2018/6/4.
 */

public class TerminateServiceActivity extends BaseActivity {

    @BindView(R2.id.scb_terminate_agree)
    SmoothCheckBox mCbAgree;
    @BindView(R2.id.btn_terminate_delete)
    Button mBtnSubmit;

    @Override
    protected int getLayoutId() {
        return R.layout.person_activity_terminate_service;
    }

    @Override
    protected CloseAccountPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        TraceUtil.onEvent(this, "my_close_account_count");
        mCbAgree.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox smoothCheckBox, boolean b) {
                mBtnSubmit.setEnabled(b);
            }
        });
    }

    @OnClick(value = {R2.id.tv_terminate_agree, R2.id.btn_terminate_delete})
    void onClick(View view) {
        if (view.getId() == R.id.tv_terminate_agree) {
            mCbAgree.setChecked(!mCbAgree.isChecked(), true);
        } else if (view.getId() == R.id.btn_terminate_delete) {
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/close_account")
                    .navigation(this);
        }
    }

}
