package com.hm.iou.userinfo.business.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.BaseBizAppLike;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.base.version.CheckVersionResBean;
import com.hm.iou.base.version.VersionApi;
import com.hm.iou.router.Router;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.tools.SystemUtil;
import com.hm.iou.uikit.HMGrayDividerItemDecoration;
import com.hm.iou.uikit.dialog.IOSAlertDialog;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;
import com.hm.iou.userinfo.business.AboutUsContract;
import com.hm.iou.userinfo.business.presenter.AboutUsPresenter;
import com.trello.rxlifecycle2.android.ActivityEvent;

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
        list.add("检测更新");
        list.add("隐私条款");
        list.add("注册与使用协议");
//        list.add("终止服务协议");
        AboutMenuAdapter adapter = new AboutMenuAdapter(list);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == 0) {
                    mPresenter.checkVersion();
                } else if (position == 1) {
                    toPrivacyPage();
                } else if (position == 2) {
                    toUserAgreementPage();
                }
            }
        });
    }

    @Override
    public void showNewestVersionDialog() {
        new IOSAlertDialog.Builder(this)
                .setMessage("已是最新版本")
                .setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void toPrivacyPage() {
        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/webview/index")
                .withString("url", BaseBizAppLike.getInstance().getH5Server() + "/PrivacyAgreement/PrivacyAgreement.html")
                .navigation(this);
    }

    private void toUserAgreementPage() {
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
