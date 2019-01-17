package com.hm.iou.userinfo.demo;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.userinfo.business.view.PersonalCenterFragment;
import com.hm.iou.userinfo.leftmenu.HomeLeftMenuView;

/**
 * Created by hjy on 2018/7/3.
 */

public class PersonalActivity extends BaseActivity {

    DrawerLayout mDrawerLayout;
    HomeLeftMenuView mHomeLeftMenuView;

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
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mHomeLeftMenuView = findViewById(R.id.homeLeftMenuView);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mHomeLeftMenuView.refreshView();
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        getSupportFragmentManager().beginTransaction()
                .add(R.id.ll_content, new PersonalCenterFragment())
                .commit();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mHomeLeftMenuView.onResume();
    }

    @Override
    protected void onDestroy() {
        mHomeLeftMenuView.onDestroy();
        super.onDestroy();
    }
}
