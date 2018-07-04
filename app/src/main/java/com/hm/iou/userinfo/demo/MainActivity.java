package com.hm.iou.userinfo.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.hm.iou.router.Router;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_test1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/login/selecttype")
                        .navigation(MainActivity.this);
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
}
