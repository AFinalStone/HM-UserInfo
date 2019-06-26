package com.hm.iou.userinfo.business.presenter;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;

import com.hm.iou.base.ActivityManager;
import com.hm.iou.base.mvp.BaseContract;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.network.HttpReqManager;
import com.hm.iou.router.Router;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.event.LogoutEvent;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.uikit.dialog.HMAlertDialog;
import com.hm.iou.userinfo.api.PersonApi;

import org.greenrobot.eventbus.EventBus;

public class LogoutUtil {

    public static void showLogoutConfirmDialog(final Activity activity, final BaseContract.BaseView view) {
        new HMAlertDialog.Builder(activity)
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
                        logout(activity, view);
                    }
                }).create().show();
    }

    private static void logout(final Context context, final BaseContract.BaseView view) {
        if (context == null || view == null)
            return;
        view.showLoadingView("安全退出中...");
        UserInfo userInfo = UserManager.getInstance(context).getUserInfo();
        PersonApi.logout(userInfo.getMobile())
                .map(RxUtil.<Object>handleResponse())
                .subscribeWith(new CommSubscriber<Object>(view) {
                    @Override
                    public void handleResult(Object data) {
                        view.dismissLoadingView();
                        exitApp(context);
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        view.dismissLoadingView();
                        exitApp(context);
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

    private static void exitApp(Context context) {
        EventBus.getDefault().post(new LogoutEvent());
        HttpReqManager.getInstance().setUserId("");
        HttpReqManager.getInstance().setToken("");
        UserManager.getInstance(context).logout();
        ActivityManager.getInstance().exitAllActivities();
        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/login/selecttype")
                .navigation(context);
    }

}
