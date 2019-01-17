package com.hm.iou.userinfo.leftmenu;

import android.content.Context;
import android.text.TextUtils;

import com.hm.iou.base.version.CheckVersionResBean;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.tools.ACache;
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
                    return context.getResources().getColor(R.color.uikit_text_sub_content);
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
        final ACache cache = ACache.get(context, "update");
        final String appVer = SystemUtil.getCurrentAppVersionName(context);
        final CheckVersionResBean versionResBean = (CheckVersionResBean) cache.getAsObject(appVer);
        //真实姓名
        final UserInfo userInfo = UserManager.getInstance(context).getUserInfo();
        final String realName = userInfo.getName();

        for (final HomeLeftMenuBean.HomeModuleBean bean : list) {
            IListMenuItem iListMenuItem = new IListMenuItem() {
                @Override
                public String getIMenuRedMsg() {
                    if (ModuleType.AUTHENTICATION.getValue().equals(bean.getId())) {
                        if (TextUtils.isEmpty(realName)) {
                            return "认证";
                        }
                    }
                    if (ModuleType.ABOUT_SOFT.getValue().equals(bean.getId())) {
                        if (versionResBean != null) {
                            return "更新";
                        }
                    }
                    return "";
                }

                @Override
                public String getIMenuDesc() {
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
