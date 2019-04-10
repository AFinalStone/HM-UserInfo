package com.hm.iou.userinfo.business.view;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.router.Router;
import com.hm.iou.tools.ViewConcurrencyUtil;
import com.hm.iou.uikit.HMLoadingView;
import com.hm.iou.uikit.HMTopBarView;
import com.hm.iou.userinfo.NavigationHelper;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;
import com.hm.iou.userinfo.business.TellNoAgreeReasonContract;
import com.hm.iou.userinfo.business.presenter.TellNoAgreeReasonPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author syl
 * @time 2019/4/4 4:25 PM
 */
public class TellNoAgreeReasonActivity extends BaseActivity<TellNoAgreeReasonPresenter> implements TellNoAgreeReasonContract.View {


    @BindView(R2.id.topBar)
    HMTopBarView mTopBar;
    @BindView(R2.id.btn_submit_reason)
    Button mBtnSubmitReason;
    @BindView(R2.id.rv_reason_list)
    RecyclerView mRvReasonList;
    @BindView(R2.id.tv_online_people_server)
    TextView mTvOnlinePeopleServer;
    @BindView(R2.id.hmLoadingView)
    HMLoadingView mHmLoadingView;

    ReasonAdapter mAdapter;
    private IReasonItem mCurrentSelectItem;

    @Override
    protected int getLayoutId() {
        return R.layout.person_activity_tell_no_agree_reason;
    }

    @Override
    protected TellNoAgreeReasonPresenter initPresenter() {
        return new TellNoAgreeReasonPresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mTopBar.setOnMenuClickListener(new HMTopBarView.OnTopBarMenuClickListener() {
            @Override
            public void onClickTextMenu() {
                NavigationHelper.toApplyForeverUnRegister(mContext);
            }

            @Override
            public void onClickImageMenu() {

            }
        });
        mAdapter = new ReasonAdapter();
        mRvReasonList.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter.bindToRecyclerView(mRvReasonList);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mAdapter.selectItem(position);
                mCurrentSelectItem = (IReasonItem) adapter.getItem(position);
                mBtnSubmitReason.setEnabled(true);
            }
        });
        mTvOnlinePeopleServer.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        mTvOnlinePeopleServer.getPaint().setAntiAlias(true);//抗锯齿
        mPresenter.getNoAgreeReasonList();
    }

    @Override
    public void showInitLoadingView() {
        mHmLoadingView.setVisibility(View.VISIBLE);
        mHmLoadingView.showDataLoading();
    }

    @Override
    public void hideInitLoadingView() {
        mHmLoadingView.setVisibility(View.GONE);
    }

    @Override
    public void showInitLoadingFailed(String msg) {
        mHmLoadingView.setVisibility(View.VISIBLE);
        mHmLoadingView.showDataFail(msg, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getNoAgreeReasonList();
            }
        });
    }

    @Override
    public void showData(List<IReasonItem> list) {
        mAdapter.setNewData(list);
    }

    @OnClick({R2.id.btn_submit_reason, R2.id.tv_online_people_server})
    public void onClick(View view) {
        int id = view.getId();
        if (R.id.btn_submit_reason == id) {
            if (ViewConcurrencyUtil.isFastClicks()) {
                return;
            }
            if (mCurrentSelectItem != null) {
                mPresenter.submitReason(mCurrentSelectItem.getReasonId());
            }
        } else if (R.id.tv_online_people_server == id) {
            if (ViewConcurrencyUtil.isFastClicks()) {
                return;
            }
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/helper_center")
                    .navigation(mContext);
        }
    }

    public interface IReasonItem {

        String getTitle();

        int getReasonId();
    }

    public static class ReasonAdapter extends BaseQuickAdapter<IReasonItem, BaseViewHolder> {

        private int mCurrentCheckPosition = -1;

        public ReasonAdapter() {
            super(R.layout.person_item_tell_no_agree_reason_list);
        }

        @Override
        protected void convert(BaseViewHolder helper, IReasonItem item) {
            helper.setText(R.id.tv_item_title, item.getTitle());
            if (mCurrentCheckPosition == helper.getLayoutPosition()) {
                helper.setImageResource(R.id.checkBox, R.mipmap.uikit_icon_check_black);
            } else {
                helper.setImageResource(R.id.checkBox, R.mipmap.uikit_icon_check_default);
            }
        }

        public void selectItem(int position) {
            mCurrentCheckPosition = position;
            notifyDataSetChanged();
        }
    }
}
