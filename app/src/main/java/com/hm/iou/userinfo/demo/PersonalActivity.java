package com.hm.iou.userinfo.demo;

import android.os.Bundle;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.userinfo.business.view.PersonalCenterFragment;

/**
 * Created by hjy on 2018/7/3.
 */

public class PersonalActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home_test;
    }

    @Override
    protected MvpActivityPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.ll_content, new PersonalCenterFragment())
                .commit();
    }
}
