package com.hm.iou.userinfo;

import android.content.Context;
import android.content.Intent;

import com.hm.iou.router.Router;
import com.hm.iou.userinfo.business.view.ChangeAliPayActivity;
import com.hm.iou.userinfo.business.view.ChangeEmailVerifyActivity;
import com.hm.iou.userinfo.business.view.MoreSettingActivity;
import com.hm.iou.userinfo.business.view.UserEmailInfoActivity;
import com.hm.iou.userinfo.business.view.VipStatusActivity;

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
     * 去更新实名信息
     *
     * @param context
     */
    public static void toUpdateAuthentication(Context context) {
        Router.getInstance()
                .buildWithUrl("hmiou://m.54jietiao.com/facecheck/update_idcard")
                .navigation(context);
    }

    public static void toVipStatusPage(Context context) {
        Intent intent = new Intent(context, VipStatusActivity.class);
        context.startActivity(intent);
    }

    public static void toPayVipPage(Context context, String price, String packageId, int reqCode) {
        Router.getInstance()
                .buildWithUrl("hmiou://m.54jietiao.com/pay/pay_vip")
                .withString("time_card_pay_money", price)
                .withString("time_card_name", "VIP会员")
                .withString("package_id", packageId)
                .navigation(context, reqCode);
    }

}
