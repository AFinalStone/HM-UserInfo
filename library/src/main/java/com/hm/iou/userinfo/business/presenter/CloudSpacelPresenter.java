package com.hm.iou.userinfo.business.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.userinfo.business.CloudSpaceContract;

/**
 * Created by hjy on 2018/5/23.
 */

public class CloudSpacelPresenter extends MvpActivityPresenter<CloudSpaceContract.View> implements CloudSpaceContract.Presenter {


    public CloudSpacelPresenter(@NonNull Context context, @NonNull CloudSpaceContract.View view) {
        super(context, view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



}
