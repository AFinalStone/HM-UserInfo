package com.hm.iou.userinfo.business.view;

import android.os.Bundle;
import android.view.View;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.utils.TraceUtil;
import com.hm.iou.userinfo.NavigationHelper;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;
import com.hm.iou.userinfo.business.presenter.CloseAccountPresenter;

import butterknife.OnClick;

/**
 * Created by hjy on 2018/6/4.
 */

public class ApplyForeverUnRegisterActivity extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.person_activity_applay_forever_unregister;
    }

    @Override
    protected CloseAccountPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        TraceUtil.onEvent(this, "my_close_account_count");
    }

    @OnClick(value = {R2.id.btn_submit_reason})
    void onClick(View view) {
        if (view.getId() == R.id.btn_submit_reason) {
            NavigationHelper.toForeverUnRegisterCheckUserInfo(mContext);
        }
    }

}
