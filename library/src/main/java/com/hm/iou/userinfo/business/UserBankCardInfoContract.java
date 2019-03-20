package com.hm.iou.userinfo.business;

import com.hm.iou.base.mvp.BaseContract;
import com.hm.iou.userinfo.bean.UserBankCardInfoResBean;

/**
 * @author syl
 * @time 2019/3/20 2:51 PM
 */
public interface UserBankCardInfoContract {

    interface View extends BaseContract.BaseView {

        /**
         * 银行卡信息
         */
        void showBankCardInfo(UserBankCardInfoResBean userBankCardInfoResBean);
    }

    interface Presenter extends BaseContract.BasePresenter {

        /**
         * 获取银行卡信息
         */
        void getBankCardInfo();
    }

}