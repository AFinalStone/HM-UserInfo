package com.hm.iou.userinfo.business;

import com.hm.iou.base.mvp.BaseContract;

/**
 * @author syl
 * @time 2018/7/23 下午5:58
 */
public interface ForeverUnRegisterCheckUserInfoContract {

    interface View extends BaseContract.BaseView {

    }

    interface Presenter extends BaseContract.BasePresenter {

        /**
         * 永久注销账号
         */
        void foreverUnRegister();

    }

}