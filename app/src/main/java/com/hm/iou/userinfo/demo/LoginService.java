package com.hm.iou.userinfo.demo;

import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.sharedata.model.UserInfo;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by hjy on 2018/5/29.
 */

public interface LoginService {

    @POST("/api/iou/user/v1/mobileLogin")
    Flowable<BaseResponse<UserInfo>> mobileLogin(@Body MobileLoginReqBean mobileLoginReqBean);

}
