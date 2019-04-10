package com.hm.iou.userinfo.business.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.BaseBizAppLike;
import com.hm.iou.base.utils.TraceUtil;
import com.hm.iou.router.Router;
import com.hm.iou.tools.SystemUtil;
import com.hm.iou.uikit.HMGrayDividerItemDecoration;
import com.hm.iou.uikit.dialog.HMAlertDialog;
import com.hm.iou.userinfo.NavigationHelper;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;
import com.hm.iou.userinfo.business.AboutUsContract;
import com.hm.iou.userinfo.business.presenter.AboutUsPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hjy on 2018/6/4.
 */

public class AboutUsActivity extends BaseActivity<AboutUsPresenter> implements AboutUsContract.View {

    @BindView(R2.id.iv_logo)
    ImageView mIvLogo;
    @BindView(R2.id.tv_about_version)
    TextView mTvVersion;
    @BindView(R2.id.recycler_about)
    RecyclerView mRecyclerView;

    private long mTimeActionDown;

    @Override
    protected int getLayoutId() {
        return R.layout.person_activity_about_us;
    }

    @Override
    protected AboutUsPresenter initPresenter() {
        return new AboutUsPresenter(this, this);
    }


    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
        mTvVersion.setText("V" + SystemUtil.getCurrentAppVersionName(this));
        mIvLogo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    mTimeActionDown = System.currentTimeMillis();
                    return true;
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    long timeActionUp = System.currentTimeMillis();
                    if (timeActionUp - mTimeActionDown > 5000) {
                        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/terminate_service")
                                .navigation(AboutUsActivity.this);
                    }
                    return true;
                }
                return false;
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new HMGrayDividerItemDecoration(this, LinearLayout.VERTICAL));
        List<String> list = new ArrayList<>();
        list.add("已同意：《用户注册协议》");
        list.add("已同意：《用户隐私协议》");
        list.add("已同意：《用户支付协议》");
        list.add("已同意：《CFCA数字证书服务协议》");
        list.add("不同意：请告知原因，并手动选择退出。");
        list.add("检测更新");

        if (BaseBizAppLike.getInstance().isDebug()) {
            list.add("切换软件服务器地址");
        }
        AboutMenuAdapter adapter = new AboutMenuAdapter(list);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == 0) {
                    toWebPage("https://app.54jietiao.com/xieyi/1.html");
                } else if (position == 1) {
                    toWebPage("https://app.54jietiao.com/xieyi/2.html");
                } else if (position == 2) {
                    toWebPage("https://app.54jietiao.com/xieyi/3.html");
                } else if (position == 3) {
                    toWebPage("https://app.54jietiao.com/xieyi/4.html");
                } else if (position == 4) {
                    NavigationHelper.toTellNoAgreeReasonPage(mContext);
                } else if (position == 5) {
                    mPresenter.checkVersion();
                } else {
                    Router.getInstance()
                            .buildWithUrl("hmiou://m.54jietiao.com/environment_switch/index")
                            .navigation(mContext);
                }
            }
        });
    }

    @Override
    public void showNewestVersionDialog() {
        new HMAlertDialog.Builder(this)
                .setMessage("已是最新版本")
                .setMessageGravity(Gravity.CENTER)
                .setPositiveButton("我知道了")
                .create()
                .show();
    }

    private void toWebPage(String url) {
        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/webview/index")
                .withString("url", url)
                .navigation(this);
    }

    private void toPrivacyPage() {
        TraceUtil.onEvent(this, "web_privacy");
        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/webview/index")
                .withString("url", BaseBizAppLike.getInstance().getH5Server() + "/PrivacyAgreement/PrivacyAgreement.html")
                .navigation(this);
    }

    private void toUserAgreementPage() {
        TraceUtil.onEvent(this, "web_useragreement");
        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/webview/index")
                .withString("url", BaseBizAppLike.getInstance().getH5Server() + "/IOUAgreement/IOUAgreement.html")
                .navigation(this);
    }

    class AboutMenuAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public AboutMenuAdapter(@Nullable List<String> data) {
            super(R.layout.person_item_about_us, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.tv_item_title, item);
        }
    }

}
