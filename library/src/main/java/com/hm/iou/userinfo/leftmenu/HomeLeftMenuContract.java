package com.hm.iou.userinfo.leftmenu;


import java.util.List;

/**
 * @author syl
 * @time 2019/1/10 4:56 PM
 */
public interface HomeLeftMenuContract {

    interface View {
        /**
         * 显示资料完成进度
         *
         * @param progress
         */
        void showProfileProgress(int progress);

        /**
         * 显示头像
         *
         * @param url          头像地址
         * @param defIconResId 默认头像图片资源id
         */
        void showAvatar(String url, int defIconResId);

        /**
         * 显示用户id
         *
         * @param id
         */
        void showUserId(String id);

        /**
         * 显示昵称
         *
         * @param nickname
         */
        void showNickname(String nickname);

        /**
         * 显示真实姓名
         */
        void showHaveAuthentication();

        /**
         * 显示已经绑定过银行卡
         */
        void showHaveBindBank();

        /**
         * 显示已经绑定过邮箱
         */
        void showHaveBindEmail();

        /**
         * 显示已经设置过职业
         */
        void showHaveSetWork();

        /**
         * 显示我的收藏篇数
         *
         * @param myCollectCount
         */
        void showMyCollectCount(String myCollectCount);

        /**
         * 显示云存储空间
         *
         * @param space
         */
        void showCloudSpace(String space);

        /**
         * 显示顶部模块列表
         *
         * @param list
         */
        void showTopMenus(List<ITopMenuItem> list);

        /**
         * 显示菜单列表
         *
         * @param list
         */
        void showListMenus(List<IListMenuItem> list);
    }

    interface Presenter {

        /**
         * 获取用户信息
         */
        void init();

        /**
         * 刷新数据
         */
        void refreshData();
    }
}
