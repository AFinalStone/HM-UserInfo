package com.hm.iou.userinfo.business.view;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.hm.iou.base.ActivityManager;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.network.HttpReqManager;
import com.hm.iou.router.Router;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.event.LogoutEvent;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.uikit.dialog.HMAlertDialog;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;
import com.hm.iou.userinfo.api.PersonApi;

import org.greenrobot.eventbus.EventBus;

import butterknife.OnClick;

/**
 * Created by hjy on 2019/2/21.
 */

public class MoreSettingActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.person_activity_more_setting;
    }

    @Override
    protected MvpActivityPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initEventAndData(Bundle bundle) {

    }

    @OnClick(value = {R2.id.ll_more_feedback, R2.id.ll_more_exit})
    void onClick(View v) {
        if (v.getId() == R.id.ll_more_exit) {
            showDialogLogoutSafely();
        } else if (v.getId() == R.id.ll_more_feedback) {
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/helper_center")
                    .navigation(mContext);
        }
    }


    private void showDialogLogoutSafely() {
        new HMAlertDialog.Builder(this)
                .setMessage("是否退出当前账号？")
                .setMessageGravity(Gravity.CENTER)
                .setPositiveButton("取消")
                .setNegativeButton("退出")
                .setOnClickListener(new HMAlertDialog.OnClickListener() {
                    @Override
                    public void onPosClick() {
                    }

                    @Override
                    public void onNegClick() {
                        logout();
                    }
                }).create().show();
    }

    private void logout() {
        showLoadingView("安全退出中...");
        UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        PersonApi.logout(userInfo.getMobile())
                .map(RxUtil.<Object>handleResponse())
                .subscribeWith(new CommSubscriber<Object>(this) {
                    @Override
                    public void handleResult(Object data) {
                        dismissLoadingView();
                        exitApp();
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        dismissLoadingView();
                        exitApp();
                    }

                    @Override
                    public boolean isShowCommError() {
                        return false;
                    }

                    @Override
                    public boolean isShowBusinessError() {
                        return false;
                    }
                });
    }

    private void exitApp() {
        EventBus.getDefault().post(new LogoutEvent());
        HttpReqManager.getInstance().setUserId("");
        HttpReqManager.getInstance().setToken("");
        UserManager.getInstance(mContext).logout();
        ActivityManager.getInstance().exitAllActivities();
        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/login/selecttype")
                .navigation(mContext);
    }
}
