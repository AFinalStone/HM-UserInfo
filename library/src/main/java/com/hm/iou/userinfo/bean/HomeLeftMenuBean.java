package com.hm.iou.userinfo.bean;


import java.util.List;

import lombok.Data;

/**
 * Created by syl on 2019/1/16.
 */
@Data
public class HomeLeftMenuBean {

    List<HomeModuleBean> topModules;//顶部五个模块入口
    List<HomeModuleBean> listMenus;//底部模块入口

    @Data

    public static class HomeModuleBean {

        /**
         * name : 备份
         * id : 0001
         * image : file:///android_asset/loginmodule_background_guide_03.png
         * url : hmiou://m.54jietiao.com/iou_create/select_collect_type
         */

        private String name;
        private String id;
        private String image;
        private String url;

    }
}
