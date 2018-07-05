package com.hm.iou.userinfo.business;

import com.hm.iou.base.mvp.BaseContract;
import com.hm.iou.userinfo.business.view.ICloudSpaceItem;

import java.util.List;

/**
 * Created by hjy on 2018/5/23.
 */

public interface CloudSpaceContract {

    interface View extends BaseContract.BaseView {

        void showUsedSpaceProgress(int progress);

        void showUsedSpace(CharSequence txt);

        void showDetailSpace(List<ICloudSpaceItem> list);

    }

    interface Presenter extends BaseContract.BasePresenter {

        /**
         * 获取用户的存储空间
         */
        void getUserCloudSpace();

        /**
         * 获取用户借条数量
         */
        void getIouCount();

    }

}