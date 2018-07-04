package com.hm.iou.userinfo.business;


import com.hm.iou.base.mvp.BaseContract;

import java.io.File;

public interface UserHeaderDetailContract {

    interface View extends BaseContract.BaseView {

        void showUserAvatar(String url, int defaultAvatarResId);

    }

    interface Presenter extends BaseContract.BasePresenter {

        void getUserAvatar();

        void uploadFile(File file);

    }
}
