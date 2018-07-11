package com.hm.iou.userinfo.business;

import com.hm.iou.base.mvp.BaseContract;

/**
 * Created by hjy on 2018/5/23.
 */

public interface ProfileContract {

    interface View extends BaseContract.BaseView {

        /**
         * 显示资料进度
         *
         * @param progress
         */
        void showProfileProgress(int progress);

        /**
         * 显示资料完成度
         *
         * @param progressTxt
         */
        void showProgressTips(String progressTxt);

        /**
         * 显示头像
         *
         * @param url 头像地址
         * @param defIconResId 默认头像图片资源id
         */
        void showAvatar(String url, int defIconResId);

        /**
         * 显示昵称
         *
         * @param nickname
         */
        void showNickname(String nickname);

        /**
         * 显示真实姓名
         *
         * @param realName
         * @param textColor 字体颜色
         */
        void showRealName(String realName, int textColor);

        /**
         * 是否显示实名认证加V标记
         *
         * @param visibility
         */
        void showRealNameFlag(int visibility);

        /**
         * 设置实名认证是否可点击
         *
         * @param enable
         */
        void enableRealNameClick(boolean enable);

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

        void getUserProfile();

        void updateLocation(String location);

        void toBindWeixin();

        void logout();
    }

}
