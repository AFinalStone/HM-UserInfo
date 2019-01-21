package com.hm.iou.userinfo.business.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.sharedata.model.SexEnum;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.userinfo.api.PersonApi;
import com.hm.iou.userinfo.business.ModifyNicknameAndSexContract;
import com.hm.iou.userinfo.event.UpdateNicknameAndSexEvent;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by hjy on 2018/5/23.
 */

public class ModifyNicknameAndSexPresenter extends MvpActivityPresenter<ModifyNicknameAndSexContract.View> implements ModifyNicknameAndSexContract.Presenter {

    private int mCurrSex;

    public ModifyNicknameAndSexPresenter(@NonNull Context context, @NonNull ModifyNicknameAndSexContract.View view) {
        super(context, view);
    }

    @Override
    public void init() {
        UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        String nickname = userInfo.getNickName();
        mView.showNickname(nickname);

        mCurrSex = userInfo.getSex();
        mView.showSex(SexEnum.getNameByValue(mCurrSex));
    }

    @Override
    public void selectSex(int sex) {
        mCurrSex = sex;
        mView.showSex(SexEnum.getNameByValue(mCurrSex));
    }

    @Override
    public void updateNicknameAndSex(final String nickname) {
        if (TextUtils.isEmpty(nickname)) {
            mView.toastMessage("昵称不能为空");
            return;
        }
        mView.showLoadingView();
        final UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        PersonApi.updateNicknameAndSex(userInfo, nickname, mCurrSex)
                .compose(getProvider().<BaseResponse<Object>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<Object>handleResponse())
                .subscribeWith(new CommSubscriber<Object>(mView) {
                    @Override
                    public void handleResult(Object data) {
                        mView.dismissLoadingView();
                        userInfo.setNickName(nickname);
                        userInfo.setSex(mCurrSex);
                        UserManager.getInstance(mContext).updateOrSaveUserInfo(userInfo);
                        mView.showModifySuccToast();
                        mView.closeCurrPage();
                        EventBus.getDefault().post(new UpdateNicknameAndSexEvent());
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });
    }

}
