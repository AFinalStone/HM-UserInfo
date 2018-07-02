package com.hm.iou.userinfo.api;

import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.userinfo.bean.BitmapAndFileIdBean;
import com.hm.iou.userinfo.bean.IsWXExistBean;
import com.hm.iou.userinfo.bean.UpdateResultBean;
import com.hm.iou.userinfo.bean.req.ChangeEmailReqBean;
import com.hm.iou.userinfo.bean.req.ChangeMobileReqBean;
import com.hm.iou.userinfo.bean.req.DelAccountReqBean;
import com.hm.iou.userinfo.bean.req.ModifyPwdReqBean;
import com.hm.iou.userinfo.bean.req.SendMessageReqBean;
import com.hm.iou.userinfo.bean.req.UnbindWxReqBean;
import com.hm.iou.userinfo.bean.req.UpdateUserInfoReqBean;
import com.hm.iou.userinfo.bean.req.VerifyEmailPwdReqBean;
import com.hm.iou.userinfo.bean.req.VerifyMobilePwdReqBean;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by hjy on 2018/5/23.
 */

public interface PersonService {


    @GET("/api/iou/user/v1/logout")
    Flowable<BaseResponse<Object>> logout(@Query("mobile") String mobile);

    @POST("/api/iou/user/v1/updateInfo")
    Flowable<BaseResponse<Object>> updateUserInfo(@Body UpdateUserInfoReqBean reqBean);

    @POST("/api/iou/user/v1/changeQueryPswd")
    Flowable<BaseResponse<Object>> modifyPwd(@Body ModifyPwdReqBean reqBean);

    @GET("/api/iou/user/v1/isWXExist")
    Flowable<BaseResponse<IsWXExistBean>> isWXExist(@Query("code") String code);

    @GET("/api/iou/user/v1/bindWXAfterLogin")
    Flowable<BaseResponse<Object>> bindWXAfterLogin(@Query("wxSn") String wxSn);

    @POST("/api/iou/user/v1/unbindWX")
    Flowable<BaseResponse<Object>> unbindWeixin(@Body UnbindWxReqBean reqBean);

    @POST("/api/iou/user/v1/validateMobileAndPwd")
    Flowable<BaseResponse<Object>> verifyPwdWhenChangeMobile(@Body VerifyMobilePwdReqBean reqBean);

    @POST("/api/iou/user/v1/changeMobile")
    Flowable<BaseResponse<Object>> changeMobile(@Body ChangeMobileReqBean reqBean);

    @POST("/api/base/msg/v1/sendMessage")
    Flowable<BaseResponse<String>> sendMessage(@Body SendMessageReqBean sendMessageReqBean);

    @GET("/api/iou/user/v1/getProveDocList")
    Flowable<BaseResponse<List<BitmapAndFileIdBean>>> getProveDocList();

    @POST("/api/iou/user/v1/checkMailWithPWD")
    Flowable<BaseResponse<String>> verifyEmailAndPwd(@Body VerifyEmailPwdReqBean reqBean);

    @POST("/api/iou/user/v1/verifyEmailByValidateCode")
    Flowable<BaseResponse<Object>> changeEmail(@Body ChangeEmailReqBean reqBean);

    @POST("/api/iou/user/v1/delUser")
    Flowable<BaseResponse<Object>> deleteAccount(@Body DelAccountReqBean reqBean);

    @GET("/api/iou/user/v1/checkVersion")
    Flowable<BaseResponse<UpdateResultBean>> checkVersion();

}