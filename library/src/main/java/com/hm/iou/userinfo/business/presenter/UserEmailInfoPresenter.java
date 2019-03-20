package com.hm.iou.userinfo.business.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.userinfo.api.PersonApi;
import com.hm.iou.userinfo.bean.UserBankCardInfoResBean;
import com.hm.iou.userinfo.bean.UserEmailInfoResBean;
import com.hm.iou.userinfo.business.UserBankCardInfoContract;
import com.hm.iou.userinfo.business.UserEmailInfoContract;
import com.hm.iou.userinfo.event.UpdateEmailEvent;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author syl
 * @time 2019/3/19 1:41 PM
 */

public class UserEmailInfoPresenter extends MvpActivityPresenter<UserEmailInfoContract.View> implements UserEmailInfoContract.Presenter {

    private boolean mNeedRefresh = true;

    public UserEmailInfoPresenter(@NonNull Context context, @NonNull UserEmailInfoContract.View view) {
        super(context, view);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void getUserEmailInfo() {
        if (mNeedRefresh) {
            mView.showLoadingView();
            PersonApi.getUserBindEmailInfo()
                    .compose(getProvider().<BaseResponse<UserEmailInfoResBean>>bindUntilEvent(ActivityEvent.DESTROY))
                    .map(RxUtil.<UserEmailInfoResBean>handleResponse())
                    .subscribeWith(new CommSubscriber<UserEmailInfoResBean>(mView) {
                        @Override
                        public void handleResult(UserEmailInfoResBean infoBean) {
                            mView.dismissLoadingView();
                            mNeedRefresh = false;
                            mView.showBankCardInfo(infoBean);
                        }

                        @Override
                        public void handleException(Throwable throwable, String s, String s1) {
                            mView.dismissLoadingView();
                            mView.closeCurrPage();
                        }
                    });
        }
    }

    /**
     * 邮箱更换通知
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdateEmail(UpdateEmailEvent event) {
        mNeedRefresh = true;
    }

}
