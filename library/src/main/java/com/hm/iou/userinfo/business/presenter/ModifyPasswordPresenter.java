package com.hm.iou.userinfo.business.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.tools.Md5Util;
import com.hm.iou.userinfo.api.PersonApi;
import com.hm.iou.userinfo.business.ModifyPasswordContract;
import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * Created by hjy on 2018/5/23.
 */

public class ModifyPasswordPresenter extends MvpActivityPresenter<ModifyPasswordContract.View> implements ModifyPasswordContract.Presenter {

    public ModifyPasswordPresenter(@NonNull Context context, @NonNull ModifyPasswordContract.View view) {
        super(context, view);
    }

    @Override
    public void modifyPwd(String oldPwd, String newPwd) {
        if (oldPwd.equals(newPwd)) {
            mView.toastMessage("新密码不能和旧密码相同");
            return;
        }

        mView.showLoadingView();
        PersonApi.modifyPassword(Md5Util.getMd5ByString(oldPwd), Md5Util.getMd5ByString(newPwd))
                .compose(getProvider().<BaseResponse<Object>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<Object>handleResponse())
                .subscribeWith(new CommSubscriber<Object>(mView) {
                    @Override
                    public void handleResult(Object data) {
                        mView.dismissLoadingView();
                        mView.toastMessage("密码修改成功");
                        mView.closeCurrPage();
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });
    }
}
