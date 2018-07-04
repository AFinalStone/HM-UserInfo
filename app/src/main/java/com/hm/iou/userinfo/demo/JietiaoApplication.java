package com.hm.iou.userinfo.demo;

import android.app.Application;

import com.hm.iou.base.BaseBizAppLike;
import com.hm.iou.logger.Logger;
import com.hm.iou.network.HttpReqManager;
import com.hm.iou.network.HttpRequestConfig;
import com.hm.iou.router.Router;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.socialshare.SocialShareAppLike;

/**
 * Created by hjy on 18/5/11.<br>
 */

public class JietiaoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Router.init(this);

        Logger.init(this, true);
        initNetwork();
        BaseBizAppLike appLike = new BaseBizAppLike();
        appLike.onCreate(this);
        appLike.initServer("http://192.168.1.254", "http://192.168.1.254",
                "http://192.168.1.254");

        SocialShareAppLike shareAppLike = new SocialShareAppLike();
        shareAppLike.onCreate(this);

    }

    private void initNetwork() {
        System.out.println("init-----------");
        HttpRequestConfig config = new HttpRequestConfig.Builder(this)
                .setDebug(true)
                .setAppChannel("guanfang")
                .setAppVersion("1.0.2")
                .setDeviceId("123abc123")
                .setBaseUrl("http://192.168.1.254")
                .setUserId(UserManager.getInstance(this).getUserId())
                .setToken(UserManager.getInstance(this).getToken())
                .build();
        HttpReqManager.init(config);
    }

}