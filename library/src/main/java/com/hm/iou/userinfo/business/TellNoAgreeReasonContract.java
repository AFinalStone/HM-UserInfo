package com.hm.iou.userinfo.business;

import com.hm.iou.base.mvp.BaseContract;
import com.hm.iou.userinfo.business.view.TellNoAgreeReasonActivity;

import java.util.List;

/**
 * @author syl
 * @time 2018/7/23 下午5:58
 */
public interface TellNoAgreeReasonContract {

    interface View extends BaseContract.BaseView {

        /**
         * 显示初始化View
         */
        void showInitLoadingView();

        /**
         * 隐藏初始化View
         */
        void hideInitLoadingView();

        /**
         * 显示初始化失败
         */
        void showInitLoadingFailed(String msg);

        /**
         * 显示数据
         */
        void showData(List<TellNoAgreeReasonActivity.IReasonItem> listData);
    }

    interface Presenter extends BaseContract.BasePresenter {

        /**
         * 获取不同意的原因列表
         */
        void getNoAgreeReasonList();

        /**
         * 提交原因
         */
        void submitReason(int reasonId);

    }

}