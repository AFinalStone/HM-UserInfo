package com.hm.iou.userinfo.business.view;

import android.os.Bundle;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.userinfo.business.CloudSpaceContract;
import com.hm.iou.userinfo.business.presenter.CloudSpacelPresenter;

/**
 * Created by hjy on 2018/7/4.
 */

public class CloudSpaceActivity extends BaseActivity<CloudSpacelPresenter> implements CloudSpaceContract.View {

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected CloudSpacelPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initEventAndData(Bundle bundle) {

    }
}
