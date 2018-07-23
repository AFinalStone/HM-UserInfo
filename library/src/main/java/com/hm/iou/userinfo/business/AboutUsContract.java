package com.hm.iou.userinfo.business;

import com.hm.iou.base.mvp.BaseContract;

/**
 * @author syl
 * @time 2018/7/23 下午5:58
 */
public interface AboutUsContract {

    interface View extends BaseContract.BaseView {
        /**
         * 最新版本的提示
         */
        void showNewestVersionDialog();
    }

    interface Presenter extends BaseContract.BasePresenter {

        /**
         * 检验版本进行更新
         */
        void checkVersion();

    }

}