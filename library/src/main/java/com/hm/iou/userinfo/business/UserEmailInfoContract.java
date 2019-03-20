package com.hm.iou.userinfo.business;

import com.hm.iou.base.mvp.BaseContract;
import com.hm.iou.userinfo.bean.UserEmailInfoResBean;

/**
 * @author syl
 * @time 2019/3/20 2:51 PM
 */
public interface UserEmailInfoContract {

    interface View extends BaseContract.BaseView {

        /**
         * 邮箱信息
         */
        void showBankCardInfo(UserEmailInfoResBean userEmailInfoResBean);
    }

    interface Presenter extends BaseContract.BasePresenter {

        /**
         * 获取邮箱信息
         */
        void getUserEmailInfo();
    }

}