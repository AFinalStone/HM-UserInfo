package com.hm.iou.userinfo;

import android.content.Context;
import android.content.Intent;

import com.hm.iou.router.Router;
import com.hm.iou.userinfo.business.view.ChangeAliPayActivity;
import com.hm.iou.userinfo.business.view.ChangeEmailVerifyActivity;
import com.hm.iou.userinfo.business.view.ApplyForeverUnRegisterActivity;
import com.hm.iou.userinfo.business.view.ForeverUnRegisterCheckUserInfoActivity;
import com.hm.iou.userinfo.business.view.MoreSettingActivity;
import com.hm.iou.userinfo.business.view.TellNoAgreeReasonActivity;
import com.hm.iou.userinfo.business.view.UserEmailInfoActivity;

/**
 * @author syl
 * @time 2018/5/19 下午2:59
 */
public class NavigationHelper {

    public static void toModifyEmailPage(Context context) {
        Intent intent = new Intent(context, ChangeEmailVerifyActivity.class);
        context.startActivity(intent);
    }

    public static void toUserEmailInfoPage(Context context) {
        Intent intent = new Intent(context, UserEmailInfoActivity.class);
        context.startActivity(intent);
    }

    public static void toBindEmail(Context context) {
        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/login/bindemail")
                .navigation(context);
    }

    /**
     * 去修改支付宝
     *
     * @param context
     * @param aliPayAccount
     */
    public static void toChangeAliPay(Context context, String aliPayAccount) {
        Intent intent = new Intent(context, ChangeAliPayActivity.class);
        intent.putExtra(ChangeAliPayActivity.EXTRA_KEY_ALIPAY_ACCOUNT, aliPayAccount);
        context.startActivity(intent);
    }

    public static void toMoreSettingPage(Context context) {
        Intent intent = new Intent(context, MoreSettingActivity.class);
        context.startActivity(intent);
    }

    /**
     * 告知不同意的原因
     *
     * @param context
     */
    public static void toTellNoAgreeReasonPage(Context context) {
        Intent intent = new Intent(context, TellNoAgreeReasonActivity.class);
        context.startActivity(intent);
    }

    /**
     * 去更新实名信息
     *
     * @param context
     */
    public static void toUpdateAuthentication(Context context) {
        Router.getInstance()
                .buildWithUrl("hmiou://m.54jietiao.com/facecheck/update_idcard")
                .navigation(context);
    }

    /**
     * 申请永久消号
     *
     * @param context
     */
    public static void toApplyForeverUnRegister(Context context) {
        Intent intent = new Intent(context, ApplyForeverUnRegisterActivity.class);
        context.startActivity(intent);
    }

    /**
     * 客户注销账号校验用户信息
     *
     * @param context
     */
    public static void toForeverUnRegisterCheckUserInfo(Context context) {
        Intent intent = new Intent(context, ForeverUnRegisterCheckUserInfoActivity.class);
        context.startActivity(intent);
    }

}
