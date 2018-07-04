package com.hm.iou.userinfo.business.presenter;

import com.hm.iou.sharedata.model.CustomerTypeEnum;
import com.hm.iou.sharedata.model.IncomeEnum;
import com.hm.iou.sharedata.model.SexEnum;
import com.hm.iou.userinfo.R;

/**
 * Created by hjy on 2018/5/23.
 */

public class UserDataUtil {

    /**
     * 获取性别对应的默认icon
     *
     * @param sex
     * @return
     */
    public static int getDefaultAvatarBySex(int sex) {
        if (sex == SexEnum.MALE.getValue()) {
            return R.mipmap.uikit_icon_header_man;
        } else if (sex == SexEnum.FEMALE.getValue()) {
            return R.mipmap.uikit_icon_header_wuman;
        } else {
            return R.mipmap.uikit_icon_header_unknow;
        }
    }

    /**
     *
     * 根据收入类型获取对应的收入名称
     * @param incomeType
     * @return
     */
    public static String getIncomeNameByType(int incomeType) {
        if (incomeType == IncomeEnum.Else.getValue()) {
            return "其他";
        } else if (incomeType == IncomeEnum.Wages.getValue()) {
            return "工资";
        } else if (incomeType == IncomeEnum.Parents.getValue()) {
            return "父母资助";
        } else if (incomeType == IncomeEnum.Business.getValue()) {
            return "生意";
        } else if (incomeType == IncomeEnum.Investment.getValue()) {
            return "投资";
        } else {
            return "无";
        }
    }

    /**
     * 判断用户是否是C级用户
     *
     * @param userType
     * @return
     */
    public static boolean isCClass(int userType) {
        if (userType == CustomerTypeEnum.CSub.getValue() || userType == CustomerTypeEnum.CPlus.getValue())
            return true;
        return false;
    }

    /**
     * 判断用户是否是B级用户
     *
     * @param userType
     * @return
     */
    public static boolean isBClass(int userType) {
        if (userType == CustomerTypeEnum.BSub.getValue() || userType == CustomerTypeEnum.BPlus.getValue())
            return true;
        return false;
    }

    /**
     * 判断用户是否是A级用户
     *
      * @param userType
     * @return
     */
    public static boolean isAClass(int userType) {
        if (userType == CustomerTypeEnum.ASub.getValue() || userType == CustomerTypeEnum.APlus.getValue())
            return true;
        return false;
    }


    /**
     * 是否是A+、B+、C+用户
     *
     * @param userType
     * @return
     */
    public static boolean isPlusClass(int userType) {
        if (userType == CustomerTypeEnum.APlus.getValue() || userType == CustomerTypeEnum.BPlus.getValue()
                || userType == CustomerTypeEnum.CPlus.getValue())
            return true;
        return false;
    }

    /**
     * 是否是A-、B-、C-用户
     *
     * @param userType
     * @return
     */
    public static boolean isSubClass(int userType) {
        if (userType == CustomerTypeEnum.ASub.getValue() || userType == CustomerTypeEnum.BSub.getValue()
                || userType == CustomerTypeEnum.CSub.getValue())
            return true;
        return false;
    }

    /**
     * 解绑微信后，用户类型会降级，例如：从A+降级成A-
     *
     * @param customerType
     * @return
     */
    public static int getDowngradeCustomerTypeAfterUnBindWeixin(int customerType) {
        if (customerType == CustomerTypeEnum.APlus.getValue()) {
            return CustomerTypeEnum.ASub.getValue();
        } else if (customerType == CustomerTypeEnum.BPlus.getValue()) {
            return CustomerTypeEnum.BSub.getValue();
        } else if (customerType == CustomerTypeEnum.CPlus.getValue()) {
            return CustomerTypeEnum.CSub.getValue();
        }
        return 0;
    }

    /**
     * 绑定微信侯，用户类型会升级，例如：从A-升级成A+
     *
     * @param customerType
     * @return
     */
    public static int getUpgradeCustomerTypeAfterBindWeixin(int customerType) {
        int type = customerType;
        if (type == CustomerTypeEnum.CSub.getValue()) {
            type = CustomerTypeEnum.CPlus.getValue();
        } else if (type == CustomerTypeEnum.BSub.getValue()) {
            type = CustomerTypeEnum.BPlus.getValue();
        } else if (type == CustomerTypeEnum.ASub.getValue()) {
            type = CustomerTypeEnum.APlus.getValue();
        }
        return type;
    }

}