package com.hm.iou.userinfo.business.view;

import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;

import com.hm.iou.router.Router;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.uikit.dialog.IOSAlertDialog;

/**
 * Created by syl on 2018/7/30.
 */

public class PersonalDialogHelper {

    private Context mContext;

    IOSAlertDialog mDialogNoAuthWhenSetSignature;
    IOSAlertDialog mDialogHaveAuthentication;
    IOSAlertDialog mDialogBinkBankInfo;
    IOSAlertDialog mDialogBinkBankNeedAuthen;

    public PersonalDialogHelper(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 当设置签名时，如果没有实名，弹出实名提醒
     */
    public void showNoAuthWhenSetSignature() {
        if (mDialogNoAuthWhenSetSignature == null) {
            mDialogNoAuthWhenSetSignature = new IOSAlertDialog.Builder(mContext)
                    .setTitle("设置手写签名")
                    .setMessage("通过实名认证后的账户，才能设置手写签名，是否立即认证实名？")
                    .setGravity(Gravity.LEFT)
                    .setPositiveButton("立即认证", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Router.getInstance()
                                    .buildWithUrl("hmiou://m.54jietiao.com/facecheck/authentication")
                                    .navigation(mContext);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        } else {
            mDialogNoAuthWhenSetSignature.show();
        }

    }

    /**
     * 提示用户已经实名认证
     */
    public void showHaveAuthtication() {
        if (mDialogHaveAuthentication == null) {
            String userName = UserManager.getInstance(mContext).getUserInfo().getName();
            String msg = String.format("当前账号已实名(%S)，如果该账号姓名并非你本人信息，你可以向客服举报或者尽快退出该账户。", userName);
            mDialogHaveAuthentication = new IOSAlertDialog.Builder(mContext)
                    .setTitle("实名认证")
                    .setMessage(msg)
                    .setGravity(Gravity.LEFT)
                    .setNegativeButton("知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        } else {
            mDialogHaveAuthentication.show();
        }

    }

    /**
     * 显示绑定的银行卡信息
     *
     * @param bankCardCode 银行卡号
     * @param phoneCode    手机号
     */
    public void showBinkBankInfo(String bankCardCode, String phoneCode) {
        if (mDialogBinkBankInfo == null) {
            String msg = String.format("当前绑定的银行卡为（中国银行/%S/借记卡），手机号尾号%S，如需更换信息，请联系客服，服务费¥2。", bankCardCode, phoneCode);
            mDialogBinkBankInfo = new IOSAlertDialog.Builder(mContext)
                    .setTitle("银行卡认证")
                    .setMessage(msg)
                    .setGravity(Gravity.LEFT)
                    .setNegativeButton("知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        } else {
            mDialogBinkBankInfo.show();
        }

    }

    /**
     * 提示绑定银行卡需要进行实名认证
     */
    public void showBinkBankNeedAuthen() {
        if (mDialogBinkBankNeedAuthen == null) {
            mDialogBinkBankNeedAuthen = new IOSAlertDialog.Builder(mContext)
                    .setTitle("银行卡认证")
                    .setMessage("通过银行卡认证，您可以免费获得1次签章的机会，目前您未通过实名认证，是否立即认证实名信息？")
                    .setGravity(Gravity.LEFT)
                    .setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("立即认证", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Router.getInstance()
                                    .buildWithUrl("hmiou://m.54jietiao.com/facecheck/authentication")
                                    .navigation(mContext);
                            dialog.dismiss();
                        }
                    })
                    .show();
        } else {
            mDialogBinkBankNeedAuthen.show();
        }

    }
}
