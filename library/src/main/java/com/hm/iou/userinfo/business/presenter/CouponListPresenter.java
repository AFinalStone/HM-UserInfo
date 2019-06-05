package com.hm.iou.userinfo.business.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.userinfo.business.ConponListContract;
import com.hm.iou.userinfo.business.view.ICouponItem;

import java.util.ArrayList;
import java.util.List;


public class CouponListPresenter extends MvpActivityPresenter<ConponListContract.View> implements ConponListContract.Presenter {

    int t = 0;

    public CouponListPresenter(@NonNull Context context, @NonNull ConponListContract.View view) {
        super(context, view);
    }

    @Override
    public void getCouponList() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                int j = t % 3;
                if (j == 0) {
                    List<ICouponItem> list = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        list.add(construct(i));
                    }
                    mView.showCouponList(list);
                    mView.showBottomTips();
                    mView.hidePullDownRefresh();
                    mView.showLoading(false);
                } else if (j == 1) {
                    List<ICouponItem> list = new ArrayList<>();
                    mView.showCouponList(null);
                    mView.hideBottomTips();
                    mView.hidePullDownRefresh();
                    mView.showDataEmpty();
                } else if (j == 2) {
                    List<ICouponItem> list = new ArrayList<>();
                    for (int i = 0; i < 4; i++) {
                        list.add(construct(i));
                    }
                    mView.showCouponList(list);
                    mView.hideBottomTips();
                    mView.hidePullDownRefresh();
                    mView.showLoading(false);
                }
                t++;
            }
        }, 2000);
    }

    @Override
    public void deleteCoupon(final String couponId) {
        mView.showLoadingView();
        System.out.println("couponId = " + couponId);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                mView.dismissLoadingView();
                mView.deleteCouponItem(couponId);
            }
        }, 2000);
    }

    private ICouponItem construct(final int i) {
        return new ICouponItem() {
            @Override
            public String getCouponId() {
                return "1234" + i;
            }

            @Override
            public String getCouponAmount() {
                return "4";
            }

            @Override
            public String getCouponDesc() {
                return "满10元减3元";
            }

            @Override
            public String getCouponInvalidDate() {
                return "截止到2018.12.25后失效";
            }
        };
    }
}
