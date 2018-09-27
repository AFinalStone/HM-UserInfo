package com.hm.iou.userinfo.business.view;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.hm.iou.base.webview.BaseWebviewActivity;
import com.hm.iou.network.HttpReqManager;
import com.hm.iou.network.HttpRequestConfig;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.tools.NetStateUtil;
import com.hm.iou.tools.StringUtil;

/**
 * Created by hjy on 2018/9/12.
 */

public class TucaoActivity extends BaseWebviewActivity {

    private static final String TUCAO_URL = "https://support.qq.com/product/36590?d-wx-push=1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HttpRequestConfig config = HttpReqManager.getInstance().getRequestConfig();

        UserInfo userInfo = UserManager.getInstance(this).getUserInfo();
        String nickname = StringUtil.getUnnullString(userInfo.getNickName());
        if (TextUtils.isEmpty(nickname)) {
            nickname = "小管家";
        }
        String headimgurl = userInfo.getAvatarUrl();
        if (TextUtils.isEmpty(headimgurl)) {
            //在百度上的条管家logo图片
            headimgurl = "https://gss3.bdstatic.com/7Po3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=5f3b3f0941086e067ea5371963611091/8435e5dde71190ef916431e7c31b9d16fdfa606f.jpg";
        }
        String openid = userInfo.getUserId();
        StringBuilder sb = new StringBuilder();
        sb.append("nickname=").append(nickname)
                .append("&avatar=").append(headimgurl)
                .append("&openid=").append(openid)
                .append("&clientVersion=").append(config.getAppVersion())
                .append("&os=").append("Android")
                .append("&osVersion=").append(Build.VERSION.RELEASE)
                .append("&clientInfo=").append(Build.BRAND + " " + Build.MODEL)
                .append("&imei=").append(config.getDeviceId())
                .append("&netType=").append(NetStateUtil.isMobileNetworkConnected(this) ? "移动网络" : "WIFI或其他非移动网络");
        mWebView.postUrl(TUCAO_URL, sb.toString().getBytes());
    }
}