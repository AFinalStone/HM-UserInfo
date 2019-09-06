package com.hm.iou.userinfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.hm.iou.base.BaseBizAppLike;
import com.hm.iou.router.Router;
import com.hm.iou.userinfo.business.view.ApplyForeverUnRegisterActivity;
import com.hm.iou.userinfo.business.view.ApplyForeverUnRegisterCheckUserInfoActivity;
import com.hm.iou.userinfo.business.view.BlackNameListActivity;
import com.hm.iou.userinfo.business.view.ChangeAliPayActivity;
import com.hm.iou.userinfo.business.view.ChangeEmailVerifyActivity;
import com.hm.iou.userinfo.business.view.HideContractListActivity;
import com.hm.iou.userinfo.business.view.MoreSettingActivity;
import com.hm.iou.userinfo.business.view.TellNoAgreeReasonActivity;
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
        Intent intent = new Intent(context, ApplyForeverUnRegisterCheckUserInfoActivity.class);
        context.startActivity(intent);
    }

    /**
     * 更新银行卡信息
     *
     * @param context
     */
    public static void toUpdateBankInfo(Context context) {
        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/pay/update_bind_bank?source=updatebank")
                .navigation(context);
    }

    /**
     * 黑名单页面
     *
     * @param context
     */
    public static void toBlackNamePage(Activity context) {
        Intent intent = new Intent(context, BlackNameListActivity.class);
        context.startActivity(intent);
    }

    /**
     * 隐藏合同页面
     *
     * @param context
     */
    public static void toHideContractPage(Activity context) {
        Intent intent = new Intent(context, HideContractListActivity.class);
        context.startActivity(intent);
    }

    /**
     * 设置被添加好友的方式
     *
     * @param context
     */
    public static void toSetAddFriendByOtherType(Activity context) {
        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/set_type_of_add_friend_by_other")
                .navigation(context);
    }

    /**
     * 用户进入反馈历史页面
     *
     * @param context
     */
    public static void toFeedbackListPage(Context context) {
        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/webview/index")
                .withString("url", BaseBizAppLike.getInstance().getH5Server() + ConstantsKt.URL_USER_FEEDBACK_LIST)
                .withString("showtitlebar", "false")
                .navigation(context);
    }


    /**
     * 进入客服反馈入口页面
     *
     * @param context
     */
    public static void toCustomerFeedbackList(Context context) {
        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/webview/index")
                .withString("url", BaseBizAppLike.getInstance().getH5Server() + ConstantsKt.URL_CUSTOMER_FEEDBACK_INDEX)
                .withString("showtitlebar", "false")
                .navigation(context);
    }

}
