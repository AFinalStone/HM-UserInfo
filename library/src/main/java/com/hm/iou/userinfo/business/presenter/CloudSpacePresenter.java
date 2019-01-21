package com.hm.iou.userinfo.business.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.sharedata.model.IOUKindEnum;
import com.hm.iou.userinfo.api.PersonApi;
import com.hm.iou.userinfo.bean.IOUCountBean;
import com.hm.iou.userinfo.bean.UserSpaceBean;
import com.hm.iou.userinfo.business.CloudSpaceContract;
import com.hm.iou.userinfo.business.view.ICloudSpaceItem;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hjy on 2018/5/23.
 */

public class CloudSpacePresenter extends MvpActivityPresenter<CloudSpaceContract.View> implements CloudSpaceContract.Presenter {

    public CloudSpacePresenter(@NonNull Context context, @NonNull CloudSpaceContract.View view) {
        super(context, view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void getUserCloudSpace() {
        PersonApi.getUserSpace()
                .compose(getProvider().<BaseResponse<UserSpaceBean>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<UserSpaceBean>handleResponse())
                .subscribeWith(new CommSubscriber<UserSpaceBean>(mView) {
                    @Override
                    public void handleResult(UserSpaceBean userSpaceBean) {
                        double usedSpace = userSpaceBean.getUsedSize() + 0d;
                        long totalSpace = userSpaceBean.getTotalSize();
                        int progress = 0;
                        if (totalSpace > 0) {
                            progress = (int) (usedSpace / (totalSpace * 1048576) * 100);
                        }
                        if (progress > 100) {
                            progress = 100;
                        }
                        mView.showUsedSpaceProgress(progress);
                        StringBuilder sb = new StringBuilder();
                        sb.append("已使用");
                        sb.append(UserDataUtil.formatUserCloudSpace(userSpaceBean.getUsedSize()));
                        sb.append("/").append(totalSpace).append("MB");
                        mView.showUsedSpace(sb.toString());
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public void getIouCount() {
        //先显示占位数据
        generateIOUCountData(null);

        PersonApi.getIOUCountByKind()
                .compose(getProvider().<BaseResponse<List<IOUCountBean>>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<List<IOUCountBean>>handleResponse())
                .subscribeWith(new CommSubscriber<List<IOUCountBean>>(mView) {
                    @Override
                    public void handleResult(List<IOUCountBean> list) {
                        List<IOUCountBean> listData = new ArrayList<>();
                        listData.addAll(list);
                        generateIOUCountData(listData);
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.toastMessage("云存储空间获取失败");
                    }

                    @Override
                    public boolean isShowBusinessError() {
                        return false;
                    }

                    @Override
                    public boolean isShowCommError() {
                        return false;
                    }
                });
    }

    private void generateIOUCountData(final List<IOUCountBean> list) {
        List<ICloudSpaceItem> dataList = new ArrayList<>();
        final Map<Integer, IOUCountBean> map = new HashMap<>();
        if (list != null) {
            for (IOUCountBean bean : list) {
                map.put(bean.getIouKind(), bean);
            }
        }
        final CharSequence[] nameArr = new CharSequence[]{
                "吕约借条", "吕约收条", "娱乐借条", "平台借条", "纸质借条", "纸质收条"
        };
        final int[] iouKindArr = new int[]{
                IOUKindEnum.ElecBorrowReceipt.getValue(), IOUKindEnum.ElecReceiveReceipt.getValue(),
                IOUKindEnum.FunReceipt.getValue(), IOUKindEnum.PlatformReceipt.getValue(),
                IOUKindEnum.PaperBorrowerReceipt.getValue(), IOUKindEnum.PaperReceiveReceipt.getValue()
        };
        for (int i = 0; i < nameArr.length; i++) {
            final int index = i;
            dataList.add(new ICloudSpaceItem() {

                @Override
                public CharSequence getTitle() {
                    return nameArr[index];
                }

                @Override
                public CharSequence getCount() {
                    IOUCountBean bean = map.get(iouKindArr[index]);
                    if (bean != null) {
                        return bean.getSum() + "张";
                    } else {
                        if (list == null) {
                            return "--张";
                        } else {
                            return "0张";
                        }
                    }
                }
            });
        }
        mView.showDetailSpace(dataList);
    }

}