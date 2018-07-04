package com.hm.iou.userinfo.business;

import com.hm.iou.base.mvp.BaseContract;

/**
 * Created by hjy on 2018/5/23.
 */

public interface ChangeMobileContract {

    interface View extends BaseContract.BaseView {

        /**
         * 开始倒计时
         */
        void startCountDown();

    }

    interface Presenter extends BaseContract.BasePresenter {

        /**
         * 校验原手机号、密码
         *
         * @param mobile 手机号
         * @param pwd 密码
         */
        void verifyPassword(String mobile, String pwd);

        /**
         * 发送验证码
         *
         * @param mobile
         */
        void sendVerifyCode(String mobile);

        /**
         * 更换手机号
         *
         * @param newMobile 新手机号
         * @param verifyCode 验证码
         * @param password 原密码
         */
        void changeMobile(String newMobile, String verifyCode, String password);

    }

}