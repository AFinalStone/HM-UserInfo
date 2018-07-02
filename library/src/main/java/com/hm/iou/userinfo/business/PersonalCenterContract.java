package com.hm.iou.userinfo.business;

import com.hm.iou.base.mvp.BaseContract;

/**
 * Created by hjy on 2018/5/23.
 */

public class PersonalCenterContract {

    public interface View extends BaseContract.BaseView {

        /**
         * 显示用户头像
         *
         * @param avatarUrl
         * @param defaultAvatarResId
         */
        void showUserAvatar(String avatarUrl, int defaultAvatarResId);

        /**
         * 显示用户昵称
         *
         * @param nickname
         */
        void showUserNickname(String nickname);

        /**
         * 显示用户id
         *
         * @param id
         */
        void showUserId(String id);

        /**
         * 显示实名认证图片
         *
         * @param resId
         */
        void showAuthenticationImg(int resId);

        /**
         * 显示实名名字
         *
         * @param name
         */
        void showAuthName(String name);

        /**
         * 显示用户手机号
         *
         * @param mobile
         */
        void showModuleMobile(String mobile);

        /**
         * 显示用户城市
         *
         * @param city
         */
        void showModuleLocation(String city);

        /**
         * 显示用户性别图标
         *
         * @param resId
         */
        void showModuleSexImage(int resId);

        /**
         * 显示用户收入
         *
         * @param income
         */
        void showModuleMainIncome(String income);

        /**
         * 显示APP版本号
         *
         * @param version
         */
        void showAppVersion(String version);
    }

    public interface Presenter extends BaseContract.BasePresenter {

        void init();

        /**
         * 注销登录
         */
        void doLogout();

        /**
         * 更换城市
         *
         * @param location
         */
        void updateLocation(String location);

        void toBindWeixin();

    }

}