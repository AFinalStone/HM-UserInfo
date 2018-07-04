package com.hm.iou.userinfo.business.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.tools.Md5Util;
import com.hm.iou.userinfo.api.PersonApi;
import com.hm.iou.userinfo.business.UnbindWeixinContract;
import com.hm.iou.userinfo.event.UpdateWeixinEvent;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by hjy on 2018/5/23.
 */

public class UnbindWeixinPresenter extends MvpActivityPresenter<UnbindWeixinContract.View> implements UnbindWeixinContract.Presenter {

    public UnbindWeixinPresenter(@NonNull Context context, @NonNull UnbindWeixinContract.View view) {
        super(context, view);
    }

    @Override
    public void unbindWeixin(String pwd) {
        mView.showLoadingView();
        String mobile = UserManager.getInstance(mContext).getUserInfo().getMobile();
        PersonApi.unbindWeixin(mobile, Md5Util.getMd5ByString(pwd))
                .compose(getProvider().<BaseResponse<Object>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<Object>handleResponse())
                .subscribeWith(new CommSubscriber<Object>(mView) {
                    @Override
                    public void handleResult(Object data) {
                        mView.dismissLoadingView();

                        UserInfo userDataBean = UserManager.getInstance(mContext).getUserInfo();
                        int customerTypeEnum = userDataBean.getType();
                        int newCustomerType = UserDataUtil.getDowngradeCustomerTypeAfterUnBindWeixin(customerTypeEnum);
                        if (newCustomerType != 0) {
                            userDataBean.setType(newCustomerType);
                        }
                        UserManager.getInstance(mContext).updateOrSaveUserInfo(userDataBean);
                        EventBus.getDefault().post(new UpdateWeixinEvent(false));
                        mView.toastMessage("解绑成功");
                        mView.closeCurrPage();
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });
    }

}
