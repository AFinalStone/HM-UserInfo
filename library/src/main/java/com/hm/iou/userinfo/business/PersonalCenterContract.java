package com.hm.iou.userinfo.business;

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
        void showAuthenticationImg(int resId);

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
    }

}