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
import com.hm.iou.sharedata.model.IncomeEnum;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.userinfo.api.PersonApi;
import com.hm.iou.userinfo.bean.BitmapAndFileIdBean;
import com.hm.iou.userinfo.business.MyIncomeContract;
import com.hm.iou.userinfo.event.UpdateIncomeEvent;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的收入
 */
public class MyIncomePresenter extends MvpActivityPresenter<MyIncomeContract.View> implements MyIncomeContract.Presenter {

    public MyIncomePresenter(@NonNull Context context, @NonNull MyIncomeContract.View view) {
        super(context, view);
    }

    @Override
    public void getIncomeInfo() {
        UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        int mainIncomeType = userInfo.getMainIncome();
        int secondIncomeType = userInfo.getSecondIncome();
        mView.showMainIncome(UserDataUtil.getIncomeNameByType(mainIncomeType));
        mView.showSecondIncome(UserDataUtil.getIncomeNameByType(secondIncomeType));
    }

    @Override
    public void updateUserIncome(IncomeEnum income, boolean isMainIncome) {
        final UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        int mainIncome;
        int secondIncome;
        if (isMainIncome) {
            mainIncome = income.getValue();
            secondIncome = userInfo.getSecondIncome();
        } else {
            mainIncome = userInfo.getMainIncome();
            secondIncome = income.getValue();
        }
        mView.showLoadingView();
        PersonApi.updateIncome(userInfo, mainIncome, secondIncome)
                .compose(getProvider().<BaseResponse<Object>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<Object>handleResponse())
                .subscribeWith(new CommSubscriber<Object>(mView) {
                    @Override
                    public void handleResult(Object o) {
                        mView.dismissLoadingView();
                        userInfo.setMainIncome(mainIncome);
                        userInfo.setSecondIncome(secondIncome);
                        UserManager.getInstance(mContext).updateOrSaveUserInfo(userInfo);

                        mView.showMainIncome(UserDataUtil.getIncomeNameByType(mainIncome));
                        mView.showSecondIncome(UserDataUtil.getIncomeNameByType(secondIncome));
                        EventBus.getDefault().post(new UpdateIncomeEvent());
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });
    }

    @Override
    public void getProveDocList() {
        PersonApi.getProveDocList()
                .compose(getProvider().<BaseResponse<List<BitmapAndFileIdBean>>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<List<BitmapAndFileIdBean>>handleResponse())
                .subscribeWith(new CommSubscriber<List<BitmapAndFileIdBean>>(mView) {
                    @Override
                    public void handleResult(List<BitmapAndFileIdBean> list) {
                        mView.showProveDocList(list);
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {

                    }
                });
    }

    @Override
    public void uploadImage(File file) {
        mView.showLoadingView();
        Map<String, Object> map = new HashMap<>();
        map.put("resourceType", "Income");
        map.put("operId", UserManager.getInstance(mContext).getUserId());
        map.put("operKind", "CUSTOMER");
        map.put("businessModel", "ANDROID");
        map.put("right", "777");
        FileApi.uploadFile(file, map)
                .compose(getProvider().<BaseResponse<FileUploadResult>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<FileUploadResult>handleResponse())
                .subscribeWith(new CommSubscriber<FileUploadResult>(mView) {
                    @Override
                    public void handleResult(FileUploadResult result) {
                        mView.dismissLoadingView();
                        mView.addNewProveImage(new BitmapAndFileIdBean(result.getFileId(), result.getFilePath()));
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });
    }

    @Override
    public void updateProveDocList(JSONArray proveDocList) {
        mView.showLoadingView();
        String proveStrArr = proveDocList.toString();
        final UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        PersonApi.updateProveDocList(userInfo, proveStrArr)
                .compose(getProvider().<BaseResponse<Object>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<Object>handleResponse())
                .subscribeWith(new CommSubscriber<Object>(mView) {
                    @Override
                    public void handleResult(Object o) {
                        mView.dismissLoadingView();
                        mView.closeCurrPage();
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                        mView.toastMessage("职业证明材料更新失败");
                        mView.closeCurrPage();
                    }

                    @Override
                    public boolean isShowCommError() {
                        return false;
                    }

                    @Override
                    public boolean isShowBusinessError() {
                        return false;
                    }
                });
    }
}
