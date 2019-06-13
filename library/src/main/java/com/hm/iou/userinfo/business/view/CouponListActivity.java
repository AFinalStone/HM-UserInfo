package com.hm.iou.userinfo.business.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.uikit.HMLoadingView;
import com.hm.iou.uikit.dialog.HMAlertDialog;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;
import com.hm.iou.userinfo.business.ConponListContract;
import com.hm.iou.userinfo.business.presenter.CouponListPresenter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;


public class CouponListActivity extends BaseActivity<CouponListPresenter> implements ConponListContract.View {

    @BindView(R2.id.smartrl_coupon_list)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R2.id.recyclerView_coupon_list)
    RecyclerView mRvCoupont;
    @BindView(R2.id.lv_iou_loading)
    HMLoadingView mLoadingView;

    private CouponListAdapter mAdapter;
    private View mViewBottomTips;

    @Override
    protected int getLayoutId() {
        return R.layout.person_activity_coupon_list;
    }

    @Override
    protected CouponListPresenter initPresenter() {
        return new CouponListPresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mAdapter = new CouponListAdapter();
        mAdapter.bindToRecyclerView(mRvCoupont);
        mRvCoupont.setLayoutManager(new LinearLayoutManager(this));
        mRvCoupont.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPresenter.getCouponList();
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.iv_coupon_del) {
                    ICouponItem item = (ICouponItem) view.getTag();
                    if (item != null) {
                        showDeleteConfirmDialog(item.getCouponId());
                    }
                }
            }
        });

        showLoading(true);
        mPresenter.getCouponList();
    }

    @Override
    public void showCouponList(List<ICouponItem> list) {
        mAdapter.setNewData(list);
    }

    @Override
    public void hidePullDownRefresh() {
        mRefreshLayout.finishRefresh();
    }

    @Override
    public void showLoading(boolean show) {
        if (show) {
            mLoadingView.showDataLoading();
        } else {
            mLoadingView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showDataEmpty() {
        mLoadingView.showDataEmpty("啊哦，空空如也～");
    }

    @Override
    public void showError(String errMsg) {
        if (mAdapter.getData() == null || mAdapter.getData().isEmpty()) {
            mLoadingView.showDataFail(errMsg, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLoading(true);
                    mPresenter.getCouponList();
                }
            });
        } else {
            toastErrorMessage(errMsg);
        }
    }

    @Override
    public void showBottomTips() {
        if (mViewBottomTips == null) {
            mViewBottomTips = getLayoutInflater().inflate(R.layout.person_item_coupon_tips, null);
            mAdapter.addFooterView(mViewBottomTips);
        }
        mViewBottomTips.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBottomTips() {
        if (mViewBottomTips != null) {
            mViewBottomTips.setVisibility(View.GONE);
        }
    }

    @Override
    public void deleteCouponItem(String couponId) {
        mAdapter.removeData(couponId);
        if (mAdapter.isEmpty()) {
            showDataEmpty();
        } else {
            hideBottomTips();
        }
    }

    private void showDeleteConfirmDialog(final String couponId) {
        new HMAlertDialog.Builder(this)
                .setMessage("确定删除这张优惠券吗？")
                .setPositiveButton("删除")
                .setNegativeButton("取消")
                .setMessageGravity(Gravity.CENTER)
                .setOnClickListener(new HMAlertDialog.OnClickListener() {
                    @Override
                    public void onPosClick() {
                        mPresenter.deleteCoupon(couponId);
                    }

                    @Override
                    public void onNegClick() {

                    }
                })
                .create().show();
    }

    class CouponListAdapter extends BaseQuickAdapter<ICouponItem, BaseViewHolder> {

        public CouponListAdapter() {
            super(R.layout.person_item_coupon);
        }

        public void removeData(String couponId) {
            List<ICouponItem> list = getData();
            if (list != null) {
                int pos = -1;
                for (int i = 0; i < list.size(); i++) {
                    if (couponId.equals(list.get(i).getCouponId())) {
                        pos = i;
                        break;
                    }
                }
                if (pos != -1) {
                    remove(pos);
                }
            }
        }

        public boolean isEmpty() {
            List<ICouponItem> list = getData();
            return list == null || list.isEmpty();
        }

        @Override
        protected void convert(BaseViewHolder helper, ICouponItem item) {
            helper.setText(R.id.tv_coupon_amount, item.getCouponAmount());
            helper.setText(R.id.tv_coupon_desc, item.getCouponDesc());
            helper.setText(R.id.tv_coupon_invalid_date, item.getCouponInvalidDate());

            TextView tvAmount = helper.getView(R.id.tv_coupon_amount);
            if (item.getCouponAmount() != null && item.getCouponAmount().length() >= 3) {
                tvAmount.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
            } else {
                tvAmount.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 44);
            }

            helper.addOnClickListener(R.id.iv_coupon_del);
            helper.setTag(R.id.iv_coupon_del, item);
        }
    }

}
