package com.hm.iou.userinfo.business;

import com.hm.iou.base.mvp.BaseContract;
import com.hm.iou.userinfo.bean.UserAuthenticationInfoBean;

/**
 * 我的实名详情
 *
 * @author syl
 * @time 2019/3/19 1:39 PM
 */

public interface AuthenticationInfoContract {

    interface View extends BaseContract.BaseView {

        /**
         * 显示实名信息
         */
        void showAuthenticationInfo(UserAuthenticationInfoBean infoBean);

    }

    interface Presenter extends BaseContract.BasePresenter {

        /**
         * 获取实名的个人信息
         */
        void getAuthenticationInfo();
    }

}