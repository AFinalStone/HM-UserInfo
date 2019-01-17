package com.hm.iou.userinfo.leftmenu;

/**
 * @author syl
 * @time 2018/5/30 上午11:51
 */
public interface IListMenuItem {
    /**
     * 模块名称
     *
     * @return
     */
    String getIModuleName();

    /**
     * 获取模块id
     *
     * @return
     */
    String getIModuleId();

    /**
     * 获取模块路由
     *
     * @return
     */
    String getIModuleRouter();

    /**
     * 获取菜单红色内容
     *
     * @return
     */
    String getIMenuRedMsg();

    /**
     * 菜单描述
     *
     * @return
     */
    String getIMenuDesc();
}