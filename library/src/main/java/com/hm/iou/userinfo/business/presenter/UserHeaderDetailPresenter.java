package com.hm.iou.userinfo.business.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.iou.base.file.FileApi;
import com.hm.iou.base.file.FileUploadResult;
import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.userinfo.api.PersonApi;
import com.hm.iou.userinfo.business.UserHeaderDetailContract;
import com.hm.iou.userinfo.event.UpdateAvatarEvent;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AFinalStone on 2017/12/12.
 */

public class UserHeaderDetailPresenter extends MvpActivityPresenter<UserHeaderDetailContract.View> implements UserHeaderDetailContract.Presenter {

    private String mAvatarUrl;

    public UserHeaderDetailPresenter(@NonNull Context context, @NonNull UserHeaderDetailContract.View view) {
        super(context, view);
    }

    @Override
    public void getUserAvatar() {
        UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        int defAvatarResId = UserDataUtil.getDefaultAvatarBySex(userInfo.getSex());
        mView.showUserAvatar(userInfo.getAvatarUrl(), defAvatarResId);
    }

    @Override
    public void uploadFile(File file) {
        mView.showLoadingView("图片上传中...");
        Map<String, Object> map = new HashMap<>();
        map.put("resourceType", "Avatar");
        map.put("operId", UserManager.getInstance(mContext).getUserId());
        map.put("businessModel", "ANDROID");
        map.put("operKind", "CUSTOMER");
        map.put("right", "777");
        FileApi.uploadFile(file, map)
                .compose(getProvider().<BaseResponse<FileUploadResult>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<FileUploadResult>handleResponse())
                .subscribeWith(new CommSubscriber<FileUploadResult>(mView) {
                    @Override
                    public void handleResult(FileUploadResult result) {
                        mView.dismissLoadingView();
                        mAvatarUrl = result.getFilePath();
                        changeAvatarUrl(result.getFileId());
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });
    }

    public void changeAvatarUrl(String avatarUrl) {
        final UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        mView.showLoadingView();
        PersonApi.updateAvatar(userInfo, avatarUrl)
                .compose(getProvider().<BaseResponse<Object>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<Object>handleResponse())
                .subscribeWith(new CommSubscriber<Object>(mView) {
                    @Override
                    public void handleResult(Object o) {
                        mView.dismissLoadingView();
                        UserManager userManager = UserManager.getInstance(mContext);
                        UserInfo userDataBean = userManager.getUserInfo();
                        userDataBean.setAvatarUrl(mAvatarUrl);
                        userManager.updateOrSaveUserInfo(userDataBean);

                        int defAvatarResId = UserDataUtil.getDefaultAvatarBySex(userInfo.getSex());
                        mView.showUserAvatar(userInfo.getAvatarUrl(), defAvatarResId);
                        EventBus.getDefault().post(new UpdateAvatarEvent());
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });
    }

}
