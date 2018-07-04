package com.hm.iou.userinfo.business;

import com.hm.iou.base.mvp.BaseContract;

/**
 * Created by hjy on 2018/5/23.
 */

public class ChangeEmailContract {

    public interface View extends BaseContract.BaseView {

        /**
         * 开始倒计时
         */
        void startCountDown();

        void showVerifyCodeSendSuccDialog(String msg);
    }

    public interface Presenter extends BaseContract.BasePresenter {

        /**
         * 校验原邮箱、密码
         *
         * @param email 手机号
         * @param pwd 密码
         */
        void verifyPassword(String email, String pwd);

        /**
         * 发送验证码
         *
         * @param email
         */
        void sendVerifyCode(String email);

        /**
         * 更换邮箱
         *
         * @param oldEmail 老邮箱
         * @param newEmail 新邮箱
         * @param verifyCode 验证码
         * @param sn 校验流水号
         */
        void changeEmail(String oldEmail, String newEmail, String verifyCode, String sn);

    }

}