package com.hm.iou.userinfo.business;


import com.hm.iou.base.mvp.BaseContract;
import com.hm.iou.sharedata.model.IncomeEnum;
import com.hm.iou.userinfo.bean.BitmapAndFileIdBean;

import org.json.JSONArray;

import java.io.File;
import java.util.List;

/**
 * 我的收入
 */
public interface MyIncomeContract {

    interface View extends BaseContract.BaseView {

        void showMainIncome(String mainIncome);

        void showSecondIncome(String secondIncome);

        void showProveDocList(List<BitmapAndFileIdBean> list);

        void addNewProveImage(BitmapAndFileIdBean data);
    }

    interface Presenter extends BaseContract.BasePresenter {

        void getIncomeInfo();

        void updateUserIncome(IncomeEnum income, boolean isMainIncome);

        void getProveDocList();

        void uploadImage(File file);

        void updateProveDocList(JSONArray proveDocList);
    }
}
