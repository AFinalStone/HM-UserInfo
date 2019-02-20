package com.hm.iou.userinfo.leftmenu;

import android.content.Context;
import android.text.TextUtils;

import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.tools.SystemUtil;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.bean.HomeLeftMenuBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by syl on 2019/1/16.
 */

public class DataUtil {


    /**
     * 把HomeModuleBean转化成IModuleData
     *
     * @param list
     * @return
     */
    public static List<ITopMenuItem> convertHomeModuleBeanToIModuleData(final Context context, List<HomeLeftMenuBean.HomeModuleBean> list) {
        List<ITopMenuItem> iTopMenuItemList = new ArrayList<>();
        if (list == null || list.isEmpty()) {
            return iTopMenuItemList;
        }
        for (final HomeLeftMenuBean.HomeModuleBean bean : list) {
            ITopMenuItem iTopMenuItem = new ITopMenuItem() {

                Integer newColor = null;

                @Override
                public String getIModuleName() {
                    return bean.getName();
                }

                @Override
                public String getIModuleId() {
                    return bean.getId();
                }

                @Override
                public int getIModuleColor() {
                    if (newColor != null) {
                        return newColor;
                    }
                    return context.getResources().getColor(R.color.uikit_text_sub_content);
                }

                @Override
                public void setIMenuColor(int newColor) {
                    this.newColor = newColor;
                }

                @Override
                public String getIModuleImage() {
                    return bean.getImage();
                }

                @Override
                public String getIModuleRouter() {
                    return bean.getUrl();
                }
            };
            iTopMenuItemList.add(iTopMenuItem);
        }
        return iTopMenuItemList;
    }

    /**
     * 把HomeModuleBean转化成IModuleData
     *
     * @param list
     * @return
     */
    public static List<IListMenuItem> convertHomeModuleBeanToIMenuItem(final Context context, List<HomeLeftMenuBean.HomeModuleBean> list) {
        List<IListMenuItem> iListMenuItemList = new ArrayList<>();
        if (list == null || list.isEmpty()) {
            return iListMenuItemList;
        }

        for (final HomeLeftMenuBean.HomeModuleBean bean : list) {

            IListMenuItem iListMenuItem = new IListMenuItem() {

                private String menuRedMsg;
                private String newDesc;

                @Override
                public String getIMenuRedMsg() {
                    return menuRedMsg;
                }

                @Override
                public void setIMenuRedMsg(String menuRedMsg) {
                    this.menuRedMsg = menuRedMsg;
                }

                @Override
                public String getIMenuDesc() {
                    if (!TextUtils.isEmpty(newDesc)) {
                        return newDesc;
                    }
                    if (ModuleType.AUTHENTICATION.getValue().equals(bean.getId())) {
                        return "已实名";
                    }
                    if (ModuleType.MY_COLLECT.getValue().equals(bean.getId())) {
                        return "共0篇";
                    }
                    if (ModuleType.MY_SIGNATURE.getValue().equals(bean.getId())) {
                        return "充值";
                    }
                    if (ModuleType.MY_CLOUD_SPACE.getValue().equals(bean.getId())) {
                        return "0MB";
                    }
                    if (ModuleType.MY_WALLET.getValue().equals(bean.getId())) {
                        return "奖励";
                    }
                    if (ModuleType.ABOUT_SOFT.getValue().equals(bean.getId())) {
                        return "版本" + SystemUtil.getCurrentAppVersionName(context);
                    }
                    return "";
                }

                @Override
                public void setIMenuDesc(String newDesc) {
                    this.newDesc = newDesc;
                }

                @Override
                public String getIModuleName() {
                    return bean.getName();
                }

                @Override
                public String getIModuleId() {
                    return bean.getId();
                }

                @Override
                public String getIModuleRouter() {
                    return bean.getUrl();
                }
            };
            iListMenuItemList.add(iListMenuItem);
        }
        return iListMenuItemList;
    }

}
