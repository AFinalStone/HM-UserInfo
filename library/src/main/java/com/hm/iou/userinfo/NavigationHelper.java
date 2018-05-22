package com.hm.iou.userinfo;

import android.content.Context;

/**
 * @author syl
 * @time 2018/5/19 下午2:59
 */
public class NavigationHelper {

    /**
     * 进行实名认证
     *
     * @param context
     */
    public static void toAuthentication(Context context) {
//        startNewActivity(AuthenticationActivity.class);
    }


    /**
     * 跳转到我的收藏列表
     *
     * @param context
     */
    public static void toNewsFavorite(Context context) {
//        startActivity(new Intent(PersonalActivity.this, NewsFavoriteActivity.class));
    }

    /**
     * 跳转到我的名片和扫码模块
     *
     * @param context
     */
    public static void toQRCode(Context context) {
//        startActivity(new Intent(PersonalActivity.this, NewsFavoriteActivity.class));
    }
}
