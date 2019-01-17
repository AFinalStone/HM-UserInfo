package com.hm.iou.userinfo.leftmenu;

/**
 * @author syl
 * @time 2018/5/30 上午11:51
 */
public interface ITopMenuItem {

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
     * 获取模块颜色
     *
     * @return
     */
    int getIModuleColor();


    /**
     * 获取模块图片
     *
     * @return
     */
    String getIModuleImage();

    /**
     * 获取模块路由
     *
     * @return
     */
    String getIModuleRouter();

}