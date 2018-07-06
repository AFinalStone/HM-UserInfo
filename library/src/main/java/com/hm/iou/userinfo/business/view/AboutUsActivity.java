package com.hm.iou.userinfo.business.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
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
import com.hm.iou.tools.SystemUtil;
import com.hm.iou.uikit.HMGrayDividerItemDecoration;
import com.hm.iou.uikit.dialog.IOSAlertDialog;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hjy on 2018/6/4.
 */

public class AboutUsActivity extends BaseActivity {

    @BindView(R2.id.tv_about_version)
    TextView mTvVersion;
    @BindView(R2.id.recycler_about)
    RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.person_activity_about_us;
    }

    @Override
    protected MvpActivityPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
        mTvVersion.setText("V" + SystemUtil.getCurrentAppVersionName(this));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new HMGrayDividerItemDecoration(this, LinearLayout.VERTICAL));
        List<String> list = new ArrayList<>();
        list.add("检测更新");
        list.add("隐私条款");
        list.add("注册与使用协议");
        list.add("终止服务");
        AboutMenuAdapter adapter = new AboutMenuAdapter(list);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == 0) {
                    checkUpdate();
                } else if (position == 1) {
                    toPrivacyPage();
                } else if (position == 2) {
                    toUserAgreementPage();
                } else if (position == 3) {
                    Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/terminate_service")
                            .navigation(AboutUsActivity.this);
                }
            }
        });
    }

    private void checkUpdate() {
        showLoadingView();
        VersionApi.checkVersion()
                .map(RxUtil.handleResponse())
                .subscribeWith(new CommSubscriber<CheckVersionResBean>(this) {
                    @Override
                    public void handleResult(CheckVersionResBean data) {
                        dismissLoadingView();
                        if (data == null || TextUtils.isEmpty(data.getDownloadUrl())) {
                            showNewestVersionDialog();
                            return;
                        }
                        if (data.getType() != 2 && data.getType() != 3) {
                            showNewestVersionDialog();
                            return;
                        }
                        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/homedialog")
                                .withString("dialog_type", data.getType() + "")
                                .withString("dialog_title", data.getTitile())
                                .withString("dialog_content", data.getContent())
                                .withString("dialog_sub_content", data.getSubContent())
                                .withString("dialog_file_down_url", data.getDownloadUrl())
                                .withString("dialog_file_md5", data.getFileMD5())
                                .navigation(mContext);
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        dismissLoadingView();
                    }
                });
    }

    private void showNewestVersionDialog() {
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
