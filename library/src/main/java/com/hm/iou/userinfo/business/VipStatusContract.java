package com.hm.iou.userinfo.business;

import com.hm.iou.base.mvp.BaseContract;

public interface VipStatusContract {

    interface View extends BaseContract.BaseView {

        void showCommUserInfoView();

        void showVipUserInfoView();

        /**
         * 显示头像
         *
         * @param url          头像地址
         * @param defIconResId 默认头像图片资源id
         */
        void showAvatar(String url, int defIconResId);

        void showDataLoading(boolean show);

        void showDataLoadError();

        /**
         * 显示VIP有效期
         *
         * @param validDate
         */
        void showVipValidDate(String validDate);
    }

    interface Presenter extends BaseContract.BasePresenter {

        void init();

        void getMemberInfo();

        void getPayInfo();
    }

}