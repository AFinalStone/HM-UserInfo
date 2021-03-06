package com.hm.iou.userinfo.demo;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.hm.iou.base.BaseBizAppLike;
import com.hm.iou.create.IOUCreateAppLike;
import com.hm.iou.jietiao.JietiaoAppLike;
import com.hm.iou.logger.Logger;
import com.hm.iou.msg.MsgCenterAppLike;
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
        BaseBizAppLike appLike = new BaseBizAppLike();
        appLike.onCreate(this);
//        appLike.initServer("http://192.168.1.107:3000", "http://192.168.1.107:3000",
//                "http://192.168.1.107:3000");
        appLike.initServer("http://dev.54jietiao.com", "http://dev.54jietiao.com",
                "http://dev.54jietiao.com");
        appLike.setDebug(BuildConfig.DEBUG);

        initNetwork();

        MsgCenterAppLike msgCenterAppLike = new MsgCenterAppLike();
        msgCenterAppLike.onCreate(this);

        SocialShareAppLike shareAppLike = new SocialShareAppLike();
        shareAppLike.onCreate(this);

        JietiaoAppLike jietiaoAppLike = new JietiaoAppLike();
        jietiaoAppLike.onCreate(this);

        IOUCreateAppLike iouCreateAppLike = new IOUCreateAppLike();
        iouCreateAppLike.onCreate(this);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initNetwork() {
        System.out.println("init-----------");
        HttpRequestConfig config = new HttpRequestConfig.Builder(this)
                .setDebug(true)
                .setAppChannel("guanfang")
                .setAppVersion("2.8.0")
                .setDeviceId("123abc123")
                .setBaseUrl(BaseBizAppLike.getInstance().getApiServer())
                .setUserId(UserManager.getInstance(this).getUserId())
                .setToken(UserManager.getInstance(this).getToken())
                .build();
        HttpReqManager.init(config);
    }

}