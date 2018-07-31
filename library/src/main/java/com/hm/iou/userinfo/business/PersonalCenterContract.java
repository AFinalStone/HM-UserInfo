package com.hm.iou.userinfo.business;

import android.support.annotation.DrawableRes;

import com.hm.iou.base.mvp.BaseContract;

/**
 * Created by hjy on 2018/5/23.
 */

public interface PersonalCenterContract {

    interface View extends BaseContract.BaseView {

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
        void showAuthenticationImg(@DrawableRes int resId);

        /**
         * 显示绑定银行卡
         *
         * @param resId
         */
        void showBindBankImg(@DrawableRes int resId);

        /**
         * 显示实名名字
         *
         * @param name
         */
        void showAuthName(String name);

        /**
         * 显示"我的资料"进度
         *
         * @param progressStr
         */
        void showProfileProgress(String progressStr);

        /**
         * 显示我的收藏篇数
         *
         * @param favoriteCount
         */
        void showNewsFavoriteCount(String favoriteCount);

        /**
         * 显示云存储空间
         *
         * @param space
         */
        void showCloudSpace(String space);

        /**
         * 显示帮助与反馈里未读消息数
         *
         * @param feedbackUnreadCount
         */
        void showHelpAndFeedbackCount(String feedbackUnreadCount);

    }

    interface Presenter extends BaseContract.BasePresenter {

        void init();

        /**
         * 获取资料完整度
         */
        void getProfileProgress();

        /**
         * 获取个人中心首页收藏数、云存储量、未读帮助数
         */
        void getStatisticData();
    }

}