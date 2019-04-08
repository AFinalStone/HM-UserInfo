package com.hm.iou.userinfo.business;

import com.hm.iou.base.mvp.BaseContract;

/**
 * @author syl
 * @time 2018/7/23 下午5:58
 */
public interface ApplyForeverUnRegisterCheckUserInfoContract {

    interface View extends BaseContract.BaseView {
        /**
         * 开始倒计时
         */
        void startCountDown();
    }

    interface Presenter extends BaseContract.BasePresenter {

        /**
         * 获取验证码
         */
        void getCheckCode(String mobile);

        /**
         * 永久注销账号
         */
        void foreverUnRegister(String mobile, String pwd, String checkCode);

        /**
         * 登出
         */
        void loginOut();

    }

}