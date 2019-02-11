package com.hm.iou.userinfo.business.view;

import android.os.Bundle;
import android.text.TextUtils;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.model.CustomerTypeEnum;
import com.hm.iou.sharedata.model.UserInfo;
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
        //绑定过邮箱
        if ((type == CustomerTypeEnum.APlus.getValue() || type == CustomerTypeEnum.ASub.getValue())
                && !TextUtils.isEmpty(userInfo.getMailAddr())) {
            NavigationHelper.toModifyEmailPage(this);
        } else {
            NavigationHelper.toBindEmail(this);
        }

        finish();
    }
}
