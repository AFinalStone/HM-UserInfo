package com.hm.iou.userinfo.util;

import android.content.Context;
import android.text.TextUtils;

import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.model.IncomeEnum;
import com.hm.iou.sharedata.model.PersonalCenterInfo;
import com.hm.iou.sharedata.model.SexEnum;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.sharedata.model.UserThirdPlatformInfo;
import com.hm.iou.userinfo.business.presenter.UserDataUtil;

/**
 * 获取用户资料完成进度
 *
 * @author syl
 * @time 2019/1/10 5:50 PM
 */
public class UserInfoCompleteProgressUtil {

    public static int getProfileProgress(Context context) {
        UserInfo userInfo = UserManager.getInstance(context).getUserInfo();
        int count = 0;
        //头像
        if (!TextUtils.isEmpty(userInfo.getAvatarUrl())) {
            count += 10;
        }
        //性别
        int sex = userInfo.getSex();
        if (sex != SexEnum.UNKNOWN.getValue()) {
            count += 10;
        }
        //实名认证
        if (!UserDataUtil.isCClass(userInfo.getType())) {
            count += 20;
        }
        //银行卡绑定
        UserThirdPlatformInfo thirdPlatformInfo = UserManager.getInstance(context).getUserExtendInfo().getThirdPlatformInfo();
        if (thirdPlatformInfo != null) {
            UserThirdPlatformInfo.BankInfoRespBean bankInfoRespBean = thirdPlatformInfo.getBankInfoResp();
            if (bankInfoRespBean != null && 1 == bankInfoRespBean.getIsBinded()) {
                count += 25;
            }
        }

        //手机号
        if (!TextUtils.isEmpty(userInfo.getMobile())) {
            count += 10;
        }
        //绑定微信号
        if (UserDataUtil.isPlusClass(userInfo.getType())) {
            count += 10;
        }
        //城市
        if (!TextUtils.isEmpty(userInfo.getLocation())) {
            count += 5;
        }
        //收入
        int income = userInfo.getMainIncome();
        if (income >= IncomeEnum.None.getValue()) {
            count += 5;
        }
        //支付宝
        PersonalCenterInfo personalCenterInfo = UserManager.getInstance(context).getUserExtendInfo().getPersonalCenterInfo();
        if (personalCenterInfo != null) {
            PersonalCenterInfo.AlipayInfoRespBean alipayInfoRespBean = personalCenterInfo.getAlipayInfoResp();
            if (alipayInfoRespBean != null && alipayInfoRespBean.isHasAlipayBinded()) {
                count += 5;
            }
        }
        return count;
    }
}
