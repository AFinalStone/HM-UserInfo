package com.hm.iou.userinfo.api;

import com.hm.iou.sharedata.model.BaseResponse;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * @author syl
 * @time 2018/5/21 下午4:50
 */
public interface UserInfoService {

    @GET("/api/iou/user/v1/isAccountExist")
    Flowable<BaseResponse<Boolean>> isAccountExist(@Query("mobile") String mobile);

}
