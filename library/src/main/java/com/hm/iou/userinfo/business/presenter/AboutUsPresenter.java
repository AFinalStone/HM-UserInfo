package com.hm.iou.userinfo.business.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.base.utils.CommSubscriber;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.base.version.CheckVersionResBean;
import com.hm.iou.base.version.VersionApi;
import com.hm.iou.router.Router;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.sharedata.model.IOUKindEnum;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.api.PersonApi;
import com.hm.iou.userinfo.bean.IOUCountBean;
import com.hm.iou.userinfo.bean.UserSpaceBean;
import com.hm.iou.userinfo.business.AboutUsContract;
import com.hm.iou.userinfo.business.CloudSpaceContract;
import com.hm.iou.userinfo.business.view.ICloudSpaceItem;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author syl
 * @time 2018/7/23 下午5:51
 */
public class AboutUsPresenter extends MvpActivityPresenter<AboutUsContract.View> implements AboutUsContract.Presenter {

    public AboutUsPresenter(@NonNull Context context, @NonNull AboutUsContract.View view) {
        super(context, view);
    }

    @Override
    public void checkVersion() {
        mView.showLoadingView();
        VersionApi.checkVersion()
                .compose(getProvider().<BaseResponse<CheckVersionResBean>>bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.<CheckVersionResBean>handleResponse())
                .subscribeWith(new CommSubscriber<CheckVersionResBean>(mView) {
                    @Override
                    public void handleResult(CheckVersionResBean data) {
                        mView.dismissLoadingView();
                        if (data == null || TextUtils.isEmpty(data.getDownloadUrl())) {
                            mView.showNewestVersionDialog();
                            return;
                        }
                        if (data.getType() != 2 && data.getType() != 3) {
                            mView.showNewestVersionDialog();
                            return;
                        }
                        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/homedialog")
                                .withString("dialog_type", data.getType() + "")
                                .withString("dialog_title", data.getTitile())
                                .withString("dialog_content", data.getContent())
                                .withString("dialog_sub_content", data.getSubContent())
                                .withString("dialog_file_down_url", data.getDownloadUrl())
                                .withString("dialog_file_md5", data.getFileMD5())
                                .navigation(mContext);
                    }

                    @Override
                    public void handleException(Throwable throwable, String s, String s1) {
                        mView.dismissLoadingView();
                    }
                });
    }
}