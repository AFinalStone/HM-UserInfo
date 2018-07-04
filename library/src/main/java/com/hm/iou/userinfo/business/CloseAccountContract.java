package com.hm.iou.userinfo.business;

import com.hm.iou.base.mvp.BaseContract;

/**
 * Created by hjy on 2018/5/23.
 */

public interface CloseAccountContract {

    interface View extends BaseContract.BaseView {

    }

    interface Presenter extends BaseContract.BasePresenter {

        /**
         * 注销登录
         */
        void doLogout();

        void closeAccount(String mobile, String pwd);

    }

}
