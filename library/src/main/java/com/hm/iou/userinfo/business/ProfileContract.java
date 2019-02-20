package com.hm.iou.userinfo.business;

import com.hm.iou.base.mvp.BaseContract;

/**
 * change by syl on 2018/7/31.
 */

public interface ProfileContract {

    interface View extends BaseContract.BaseView {

        /**
         * 显示头像
         *
         * @param url          头像地址
         * @param defIconResId 默认头像图片资源id
         */
        void showAvatar(String url, int defIconResId);

        /**
         * 显示昵称和性别
         *
         * @param nickname 昵称
         * @param sexIcon  性别图标
         */
        void showNicknameAndSex(String nickname, int sexIcon);

        /**
         * 显示银行卡
         *
         * @param bankName
         * @param textColor
         */
        void showBindBank(String bankName, int textColor);

        /**
         * 显示是否绑定银行卡的标记
         */
        void showBindBankFlag();

        /**
         * 显示手机号
         *
         * @param mobile
         */
        void showMobile(String mobile);

        /**
         * 设置微信号
         *
         * @param weixin
         * @param textColor 字体颜色
         */
        void showWeixin(String weixin, int textColor);

        /**
         * 显示游戏账号
         *
         * @param visibility
         * @param email
         */
        void showEmail(int visibility, String email);

        /**
         * 显示常住城市
         *
         * @param city
         */
        void showCity(String city);

        /**
         * 显示主要收入
         *
         * @param income
         */
        void showMainIncome(String income);


    }

    interface Presenter extends BaseContract.BasePresenter {

        /**
         * 获取用户信息
         */
        void getUserProfile();

        /**
         * 更新用户地址
         *
         * @param location
         */
        void updateLocation(String location);

        /**
         * 绑定微信
         */
        void toBindWeixin();

        /**
         * 注销账户
         */
        void logout();
    }

}
