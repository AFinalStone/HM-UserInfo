package com.hm.iou.userinfo.business.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hm.iou.base.comm.CommApi;
import com.hm.iou.base.comm.CouponInfo;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.tools.MoneyFormatUtil;
import com.hm.iou.userinfo.api.PersonApi;
import com.hm.iou.userinfo.business.ConponListContract;
import com.hm.iou.userinfo.business.view.ICouponItem;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;


public class CouponListPresenter extends MvpActivityPresenter<ConponListContract.View> implements ConponListContract.Presenter {

    public CouponListPresenter(@NonNull Context context, @NonNull ConponListContract.View view) {
        super(context, view);
    }

    @Override
    public void getCouponList() {
        CommApi.getCouponList(0)
                .compose(getProvider().<BaseResponse<List<CouponInfo>>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<List<CouponInfo>>handleResponse())
                .subscribeWith(new CommSubscriber<List<CouponInfo>>(mView) {

                    @Override
                    public void handleResult(List<CouponInfo> list) {
                        List<ICouponItem> dataList = null;
                        if (list != null && !list.isEmpty()) {
                            dataList = new ArrayList<>();
                            for (final CouponInfo data : list) {
                                dataList.add(new ICouponItem() {

                                    CouponInfo info = data;

                                    @Override
                                    public String getCouponId() {
                                        return info.getCouponId();
                                    }

                                    @Override
                                    public String getCouponAmount() {
                                        try {
                                            return MoneyFormatUtil.changeF2Y(info.getReducedPrice());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            return info.getReducedPrice() / 100 + "";
                                        }
                                    }

                                    @Override
                                    public String getCouponDesc() {
                                        return info.getCouponName();
                                    }

                                    @Override
                                    public String getCouponInvalidDate() {
                                        String date = "";
                                        if (!TextUtils.isEmpty(info.getExpiryDate()) && info.getExpiryDate().length() >= 10) {
                                            date = info.getExpiryDate().substring(0, 10).replace("-", ".");
                                        }
                                        return String.format("截止到%s后失效", date);
                                    }
                                });
                            }
                        }

                        mView.hidePullDownRefresh();
                        mView.showLoading(false);
                        mView.showCouponList(dataList);
                        if (dataList != null && dataList.size() >= 5) {
                            mView.showBottomTips();
                        } else if (dataList != null && !dataList.isEmpty()) {
                            mView.hideBottomTips();
                        } else {
                            mView.showDataEmpty();
                        }
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.hidePullDownRefresh();
                        mView.showLoading(false);
                    }

                });
    }

    @Override
    public void deleteCoupon(final String couponId) {
        mView.showLoadingView();
        PersonApi.delCoupon(couponId)
                .compose(getProvider().<BaseResponse<Object>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse())
                .subscribeWith(new CommSubscriber<Object>(mView) {
                    @Override
                    public void handleResult(Object o) {
                        mView.dismissLoadingView();
                        mView.deleteCouponItem(couponId);
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });
    }

}
