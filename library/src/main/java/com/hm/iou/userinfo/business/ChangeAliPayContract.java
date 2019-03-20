package com.hm.iou.userinfo.business;

import com.hm.iou.base.mvp.BaseContract;

/**
 * @author syl
 * @time 2019/3/19 1:39 PM
 */

public interface ChangeAliPayContract {

    interface View extends BaseContract.BaseView {

        /**
         * 显示支付宝账号
         */
        void showAliPay(String aliPay);

        /**
         * 保存支付宝账号成功
         *
         */
        void saveAliPaySuccess();
    }

    interface Presenter extends BaseContract.BasePresenter {

        /**
         * 获取支付宝账号
         */
        void getAliPay();

        /**
         * 设置支付宝账号
         */
        void saveAliPay(String aliPay);
    }

}