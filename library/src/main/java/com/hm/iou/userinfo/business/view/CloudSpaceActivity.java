package com.hm.iou.userinfo.business.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.uikit.HMGrayDividerItemDecoration;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;
import com.hm.iou.userinfo.business.CloudSpaceContract;
import com.hm.iou.userinfo.business.presenter.CloudSpacePresenter;

import java.util.List;

import butterknife.BindView;

/**
 * Created by hjy on 2018/7/4.
 */

public class CloudSpaceActivity extends BaseActivity<CloudSpacePresenter> implements CloudSpaceContract.View {

    @BindView(R2.id.tv_cloud_space)
    TextView mTvCloudSpace;
    @BindView(R2.id.pb_cloud_progress)
    ProgressBar mPbCloudSpace;
    @BindView(R2.id.rv_cloud_space)
    RecyclerView mRecyclerView;

    private CloudSpaceAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.person_activity_cloud_space;
    }

    @Override
    protected CloudSpacePresenter initPresenter() {
        return new CloudSpacePresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mAdapter = new CloudSpaceAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new HMGrayDividerItemDecoration(this, LinearLayout.VERTICAL));

        mPresenter.getUserCloudSpace();
        mPresenter.getIouCount();
    }

    @Override
    public void showUsedSpaceProgress(int progress) {
        mPbCloudSpace.setProgress(progress);
    }

    @Override
    public void showUsedSpace(CharSequence txt) {
        mTvCloudSpace.setText(txt);
    }

    @Override
    public void showDetailSpace(List<ICloudSpaceItem> list) {
        mAdapter.setNewData(list);
    }

    class CloudSpaceAdapter extends BaseQuickAdapter<ICloudSpaceItem, BaseViewHolder> {

        public CloudSpaceAdapter() {
            super(R.layout.person_item_cloud_space);
        }

        @Override
        protected void convert(BaseViewHolder helper, ICloudSpaceItem item) {
            helper.setImageResource(R.id.iv_cloud_icon, item.getIcon());
            helper.setText(R.id.tv_cloud_title, item.getTitle());
            helper.setText(R.id.tv_cloud_count, item.getCount());
        }
    }

}
