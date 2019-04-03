package com.hm.iou.userinfo.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hm.iou.network.HttpReqManager;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.tools.ToastUtil;
import com.sina.weibo.sdk.utils.MD5;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_test1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        findViewById(R.id.btn_test2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/index")
                        .navigation(MainActivity.this);*/

                startActivity(new Intent(MainActivity.this, PersonalActivity.class));
            }
        });


    }


    private void login() {
        String pwd = MD5.hexdigest("123456".getBytes());
//        String pwd = MD5.hexdigest("kaka123ac".getBytes());
        MobileLoginReqBean reqBean = new MobileLoginReqBean();
//        reqBean.setMobile("15267163669");
        reqBean.setMobile("15967132742");
//        reqBean.setMobile("18969904897");
        reqBean.setQueryPswd(pwd);
        HttpReqManager.getInstance().getService(LoginService.class)
                .mobileLogin(reqBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse<UserInfo>>() {
                    @Override
                    public void accept(BaseResponse<UserInfo> userInfoBaseResponse) throws Exception {
                        if (userInfoBaseResponse.getErrorCode() == 0) {
                            ToastUtil.showMessage(MainActivity.this, "登录成功");
                            UserInfo userInfo = userInfoBaseResponse.getData();
                            UserManager.getInstance(MainActivity.this).updateOrSaveUserInfo(userInfo);
                            HttpReqManager.getInstance().setUserId(userInfo.getUserId());
                            HttpReqManager.getInstance().setToken(userInfo.getToken());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable t) throws Exception {
                        t.printStackTrace();
                    }
                });
    }
}
