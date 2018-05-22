package com.hm.iou.userinfo.api;

import com.hm.iou.network.HttpReqManager;
import com.hm.iou.sharedata.model.BaseResponse;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author syl
 * @time 2018/5/17 上午11:28
 */
public class UserInfoApi {

    private static UserInfoService getService() {
        return HttpReqManager.getInstance().getService(UserInfoService.class);
    }

    /**
     * 判断手机号是否存在
     *
     * @param mobile 手机号
     * @return
     */
    public static Flowable<BaseResponse<Boolean>> isAccountExist(String mobile) {
        return getService().isAccountExist(mobile).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}