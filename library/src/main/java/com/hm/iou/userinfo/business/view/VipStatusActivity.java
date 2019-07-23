//package com.hm.iou.userinfo.business.view;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.ViewStub;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.hm.iou.base.BaseActivity;
//import com.hm.iou.tools.ImageLoader;
//import com.hm.iou.uikit.HMLoadingView;
//import com.hm.iou.userinfo.R;
//import com.hm.iou.userinfo.R2;
//import com.hm.iou.userinfo.business.VipStatusContract;
//import com.hm.iou.userinfo.business.presenter.VipStatusPresenter;
//
//import butterknife.BindView;
//
///**
// * Created by hjy on 2019/4/3.
// */
//
//public class VipStatusActivity extends BaseActivity<VipStatusPresenter> implements VipStatusContract.View, View.OnClickListener {
//
//    public static final int REQ_CODE_PAY = 101;
//
//    @BindView(R2.id.rl_vip_top_container)
//    View mLayoutTopContainer;
//    @BindView(R2.id.iv_vip_header)
//    ImageView mIvHeader;
//    @BindView(R2.id.iv_vip_flag)
//    ImageView mIvVipFlag;
//
//    @BindView(R2.id.vs_vip_comm)
//    ViewStub mViewStubComm;
//    @BindView(R2.id.vs_vip_vipuser)
//    ViewStub mViewStubVip;
//
//    @BindView(R2.id.loading_view)
//    HMLoadingView mLoadingView;
//
//    private View mLayoutCommUserInfo;
//    private View mLayoutVipUserInfo;
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.person_activity_vip_status;
//    }
//
//    @Override
//    protected VipStatusPresenter initPresenter() {
//        return new VipStatusPresenter(this, this);
//    }
//
//    @Override
//    protected void initEventAndData(Bundle bundle) {
//        mPresenter.init();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQ_CODE_PAY) {
//            if (resultCode == RESULT_OK) {
//                mPresenter.getMemberInfo();
//            }
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (v.getId() == R.id.iv_vip_open_vip) {
//            mPresenter.getPayInfo();
//        } else if (v.getId() == R.id.tv_vip_continue_use) {
//            finish();
//        }
//    }
//
//    @Override
//    public void showCommUserInfoView() {
//        if (mLayoutCommUserInfo == null) {
//            mLayoutCommUserInfo = mViewStubComm.inflate();
//        }
//        mLayoutCommUserInfo.setVisibility(View.VISIBLE);
//        if (mLayoutVipUserInfo != null) {
//            mLayoutVipUserInfo.setVisibility(View.GONE);
//        }
//        mLayoutTopContainer.setBackgroundResource(R.mipmap.person_bg_vip_not);
//        mIvVipFlag.setImageResource(R.mipmap.persion_user_flag_not_vip);
//
//        mLayoutCommUserInfo.findViewById(R.id.iv_vip_open_vip).setOnClickListener(this);
//        mLayoutCommUserInfo.findViewById(R.id.tv_vip_continue_use).setOnClickListener(this);
//    }
//
//    @Override
//    public void showVipUserInfoView() {
//        if (mLayoutVipUserInfo == null) {
//            mLayoutVipUserInfo = mViewStubVip.inflate();
//        }
//        mLayoutVipUserInfo.setVisibility(View.VISIBLE);
//        if (mLayoutCommUserInfo != null) {
//            mLayoutCommUserInfo.setVisibility(View.GONE);
//        }
//        mLayoutTopContainer.setBackgroundResource(R.mipmap.person_bg_vip);
//        mIvVipFlag.setImageResource(R.mipmap.persion_user_flag_vip);
//    }
//
//    @Override
//    public void showAvatar(String url, int defIconResId) {
//        if (TextUtils.isEmpty(url)) {
//            mIvHeader.setImageResource(defIconResId);
//            return;
//        }
//        ImageLoader.getInstance(mContext).displayImage(url, mIvHeader, defIconResId, defIconResId);
//    }
//
//    @Override
//    public void showDataLoading(boolean show) {
//        if (show) {
//            mLoadingView.showDataLoading();
//        } else {
//            mLoadingView.setVisibility(View.GONE);
//        }
//    }
//
//    @Override
//    public void showDataLoadError() {
//        mLoadingView.showDataFail(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPresenter.getMemberInfo();
//            }
//        });
//    }
//
//    @Override
//    public void showVipValidDate(String validDate) {
//        if (mLayoutVipUserInfo != null) {
//            TextView tvValidDate = mLayoutVipUserInfo.findViewById(R.id.tv_vip_valid_date);
//            tvValidDate.setText(validDate);
//        }
//    }
//}
