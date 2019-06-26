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
import com.hm.iou.userinfo.business.presenter.LogoutUtil;

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
            LogoutUtil.showLogoutConfirmDialog(this, this);
        } else if (v.getId() == R.id.ll_more_feedback) {
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/helper_center")
                    .navigation(mContext);
        }
    }

}
