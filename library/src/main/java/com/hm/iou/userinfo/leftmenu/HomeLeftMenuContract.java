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
         * 显示顶部模块列表
         *
         * @param list
         */
        void showTopMenus(List<ITopMenuItem> list);

        /**
         * 更新顶部menu的icon颜色
         *
         * @param menuId
         * @param iconColor
         */
        void updateTopMenuIcon(String menuId, int iconColor);

        /**
         * 显示菜单列表
         *
         * @param list
         */
        void showListMenus(List<IListMenuItem> list);

        /**
         * 更新列表菜单里的描述信息
         *
         * @param menuId
         * @param desc
         * @param redMsg 不为空，则显示红色背景
         */
        void updateListMenu(String menuId, String desc, String redMsg);

        /**
         * 显示是否VIP
         *
         * @param vipStatus
         */
        void updateVipStatus(String vipStatus);
    }

    interface Presenter {

        /**
         * 获取用户信息
         */
        void init();

        void onResume();

        /**
         * 刷新数据
         */
        void refreshData();
    }
}
