package com.hm.iou.userinfo.business.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.router.Router;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.model.CustomerTypeEnum;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.uikit.dialog.HMAlertDialog;
import com.hm.iou.userinfo.NavigationHelper;

/**
 * Created by hjy on 2019/2/11.
 */

public class AboutEmailActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected MvpActivityPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        UserInfo userInfo = UserManager.getInstance(this).getUserInfo();
        int type = userInfo.getType();

        //以前绑定邮箱前，需要先做实名认证
        if (type == CustomerTypeEnum.CSub.getValue() || type == CustomerTypeEnum.CPlus.getValue()) {
            showAuthDialog();
            return;
        }

        //绑定过邮箱
        if ((type == CustomerTypeEnum.APlus.getValue() || type == CustomerTypeEnum.ASub.getValue())
                && !TextUtils.isEmpty(userInfo.getMailAddr())) {
            NavigationHelper.toUserEmailInfoPage(this);
        } else {
            NavigationHelper.toBindEmail(this);
        }
        finish();
    }

    private void showAuthDialog() {
        HMAlertDialog dialog = new HMAlertDialog.Builder(mContext)
                .setTitle("绑定邮箱")
                .setMessage("为了保障账号安全，绑定邮箱需要实名认证，目前您尚未通过实名认证，是否立即认证实名信息 ？")
                .setNegativeButton("以后再说")
                .setPositiveButton("立即认证")
                .setOnClickListener(new HMAlertDialog.OnClickListener() {
                    @Override
                    public void onPosClick() {
                        Router.getInstance()
                                .buildWithUrl("hmiou://m.54jietiao.com/facecheck/authentication")
                                .navigation(mContext);
                    }

                    @Override
                    public void onNegClick() {

                    }
                })
                .create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
        dialog.show();
    }

}
