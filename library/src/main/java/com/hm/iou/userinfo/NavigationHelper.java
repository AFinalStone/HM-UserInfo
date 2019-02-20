package com.hm.iou.userinfo;

import android.content.Context;
import android.content.Intent;

import com.hm.iou.router.Router;
import com.hm.iou.userinfo.business.view.ChangeEmailVerifyActivity;

/**
 * @author syl
 * @time 2018/5/19 下午2:59
 */
public class NavigationHelper {

    public static void toModifyEmailPage(Context context) {
        Intent intent = new Intent(context, ChangeEmailVerifyActivity.class);
        context.startActivity(intent);
    }

    public static void toBindEmail(Context context) {
        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/login/bindemail")
                .navigation(context);
    }

}
