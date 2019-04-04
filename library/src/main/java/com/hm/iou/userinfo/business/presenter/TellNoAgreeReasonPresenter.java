package com.hm.iou.userinfo.business.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.userinfo.api.PersonApi;
import com.hm.iou.userinfo.bean.NoAgreeReasonBean;
import com.hm.iou.userinfo.business.TellNoAgreeReasonContract;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;


/**
 * @author syl
 * @time 2018/7/23 下午5:51
 */
public class TellNoAgreeReasonPresenter extends MvpActivityPresenter<TellNoAgreeReasonContract.View> implements TellNoAgreeReasonContract.Presenter {

    public TellNoAgreeReasonPresenter(@NonNull Context context, @NonNull TellNoAgreeReasonContract.View view) {
        super(context, view);
    }

    @Override
    public void getNoAgreeReasonList() {
        mView.showInitLoadingView();
        PersonApi.getNoAgreeReasonList()
                .compose(getProvider().<BaseResponse<List<NoAgreeReasonBean>>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<List<NoAgreeReasonBean>>handleResponse())
                .subscribeWith(new CommSubscriber<List<NoAgreeReasonBean>>(mView) {
                    @Override
                    public void handleResult(List<NoAgreeReasonBean> noAgreeReasonBeans) {
                        mView.hideInitLoadingView();
                        mView.showData((ArrayList) noAgreeReasonBeans);
                    }

                    @Override
                    public void handleException(Throwable throwable, String code, String msg) {
                        mView.showInitLoadingFailed(msg);
                    }

                    @Override
                    public boolean isShowBusinessError() {
                        return false;
                    }

                    @Override
                    public boolean isShowCommError() {
                        return false;
                    }
                });
    }

    @Override
    public void submitReason(String reasonId) {
        mView.showLoadingView();
        PersonApi.submitNoAgreeReason(reasonId)
                .compose(getProvider().<BaseResponse<Boolean>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<Boolean>handleResponse())
                .subscribeWith(new CommSubscriber<Boolean>(mView) {
                    @Override
                    public void handleResult(Boolean flag) {
                        mView.dismissLoadingView();
                        mView.toastMessage("提交成功");
                        mView.closeCurrPage();
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });
    }
}