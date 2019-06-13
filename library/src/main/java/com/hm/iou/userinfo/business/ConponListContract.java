package com.hm.iou.userinfo.business;

import com.hm.iou.base.mvp.BaseContract;
import com.hm.iou.userinfo.business.view.ICouponItem;

import java.util.List;

public interface ConponListContract {

    interface View extends BaseContract.BaseView {

        void showCouponList(List<ICouponItem> list);

        void hidePullDownRefresh();

        void showLoading(boolean show);

        void showError(String errMsg);

        void showDataEmpty();

        void showBottomTips();

        void hideBottomTips();

        void deleteCouponItem(String couponId);
    }

    interface Presenter extends BaseContract.BasePresenter {

        void getCouponList();

        void deleteCoupon(String couponId);
    }

}