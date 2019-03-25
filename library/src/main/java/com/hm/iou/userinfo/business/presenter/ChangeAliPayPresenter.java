package com.hm.iou.userinfo.business.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.sharedata.model.PersonalCenterInfo;
import com.hm.iou.sharedata.model.UserExtendInfo;
import com.hm.iou.userinfo.api.PersonApi;
import com.hm.iou.userinfo.business.ChangeAliPayContract;
import com.hm.iou.userinfo.event.UpdateAliPayEvent;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * @author syl
 * @time 2019/3/19 1:41 PM
 */

public class ChangeAliPayPresenter extends MvpActivityPresenter<ChangeAliPayContract.View> implements ChangeAliPayContract.Presenter {


    public ChangeAliPayPresenter(@NonNull Context context, @NonNull ChangeAliPayContract.View view) {
        super(context, view);
    }

    @Override
    public void saveAliPay(final String aliPay) {
        mView.showLoadingView();
        PersonApi.addOrUpdateAliPay(aliPay)
                .compose(getProvider().<BaseResponse<Object>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse())
                .subscribeWith(new CommSubscriber<Object>(mView) {
                    @Override
                    public void handleResult(Object o) {
                        mView.dismissLoadingView();
                        mView.toastMessage("保存成功");
                        UpdateAliPayEvent updateAliPayEvent = new UpdateAliPayEvent();
                        updateAliPayEvent.setAlipay(aliPay);
                        EventBus.getDefault().post(updateAliPayEvent);
                        mView.closeCurrPage();
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });
    }
}
