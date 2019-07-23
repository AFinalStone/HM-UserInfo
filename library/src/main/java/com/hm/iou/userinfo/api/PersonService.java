package com.hm.iou.userinfo.api;

import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.sharedata.model.UserThirdPlatformInfo;
import com.hm.iou.userinfo.bean.BitmapAndFileIdBean;
import com.hm.iou.userinfo.bean.BlackNameAndHideContractNumResBean;
import com.hm.iou.userinfo.bean.BlackNameBean;
import com.hm.iou.userinfo.bean.HideContractBean;
import com.hm.iou.userinfo.bean.IOUCountBean;
import com.hm.iou.userinfo.bean.IsWXExistBean;
import com.hm.iou.userinfo.bean.MemberBean;
import com.hm.iou.userinfo.bean.NoAgreeReasonBean;
import com.hm.iou.userinfo.bean.PayPackageResBean;
import com.hm.iou.userinfo.bean.TypeOfAddFriendByOtherResBean;
import com.hm.iou.userinfo.bean.UpdateTypeOfAddFriendByOtherResBean;
import com.hm.iou.userinfo.bean.UserAuthenticationInfoResBean;
import com.hm.iou.userinfo.bean.UserBankCardInfoResBean;
import com.hm.iou.userinfo.bean.UserCenterStatisticBean;
import com.hm.iou.userinfo.bean.UserEmailInfoResBean;
import com.hm.iou.userinfo.bean.UserSpaceBean;
import com.hm.iou.userinfo.bean.req.ChangeEmailReqBean;
import com.hm.iou.userinfo.bean.req.ChangeMobileReqBean;
import com.hm.iou.userinfo.bean.req.DelAccountReqBean;
import com.hm.iou.userinfo.bean.req.DelCouponReqBean;
import com.hm.iou.userinfo.bean.req.ForeverUnRegisterReqBean;
import com.hm.iou.userinfo.bean.req.GetPayPackageListReqBean;
import com.hm.iou.userinfo.bean.req.ModifyPwdReqBean;
import com.hm.iou.userinfo.bean.req.SendMessageReqBean;
import com.hm.iou.userinfo.bean.req.UnbindWxReqBean;
import com.hm.iou.userinfo.bean.req.UpdateTypeOfAddFriendByOtherReqBean;
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

    @GET("/api/iou/front/v1/iou/countIOUByKind")
    Flowable<BaseResponse<List<IOUCountBean>>> getIOUCountByKind();

    @GET("/api/iou/user/v1/userCenter")
    Flowable<BaseResponse<UserCenterStatisticBean>> getUserCenterStatistic();

    @GET("/api/iou/user/v1/userCenterUserSpace")
    Flowable<BaseResponse<UserSpaceBean>> getUserSpace();


    @GET("/pay/verify/v1/selectVerifyInfo")
    Flowable<BaseResponse<UserThirdPlatformInfo>> getUserThirdPlatformInfo();

    @GET("/api/iou/user/v1/getRealNameInfo")
    Flowable<BaseResponse<UserAuthenticationInfoResBean>> getRealNameInfo();

    @GET("/api/iou/user/v1/addOrUpdateAliPay")
    Flowable<BaseResponse<Object>> addOrUpdateAliPay(@Query("alipay") String aliPay);

    @GET("/api/iou/user/v1/getMailboxInfo")
    Flowable<BaseResponse<UserEmailInfoResBean>> getUserBindEmailInfo();

    @GET("/api/iou/user/v1/getBankCardInfo")
    Flowable<BaseResponse<UserBankCardInfoResBean>> getUserBindBankCardInfo();

    @GET("/api/iou/user/v1/getCustomerFeedback")
    Flowable<BaseResponse<List<NoAgreeReasonBean>>> getNoAgreeReasonList(@Query("scene") int scene);

    @GET("/api/iou/user/v1/addFeedbackById")
    Flowable<BaseResponse<Boolean>> submitNoAgreeReason(@Query("feedbackId") int feedbackId);

    @POST("/api/iou/user/v1/clearAccount")
    Flowable<BaseResponse<String>> foreverUnRegister(@Body ForeverUnRegisterReqBean reqBean);

    @POST("/pay/iou/package/v2/packageList")
    Flowable<BaseResponse<PayPackageResBean>> getPackageList(@Body GetPayPackageListReqBean getPayPackageListReqBean);

    @GET("/api/iou/user/v1/getMemberInfo")
    Flowable<BaseResponse<MemberBean>> getMemberInfo();


    @POST("/api/coupon/v1/delCoupons")
    Flowable<BaseResponse<Object>> delCoupon(@Body DelCouponReqBean reqBean);

    @GET("/api/iou/user/v1/getHideIOUAndOther")
    Flowable<BaseResponse<BlackNameAndHideContractNumResBean>> getBlackNameAndHideContractNum();

    @GET("/api/iou/front/v1/iou/getHideIOU")
    Flowable<BaseResponse<List<HideContractBean>>> getHideContractList();

    @GET("/api/news/friend/v1/getBlackFriend")
    Flowable<BaseResponse<List<BlackNameBean>>> getBlackNameList();

    @GET("/api/news/friend/v1/getApplyMeRelatedSetting")
    Flowable<BaseResponse<TypeOfAddFriendByOtherResBean>> getTypeOfAddFriendByOther();

    @POST("/api/news/friend/v1/updateApplyMeRelatedSetting")
    Flowable<BaseResponse<UpdateTypeOfAddFriendByOtherResBean>> updateTypeOfAddFriendByOther(@Body UpdateTypeOfAddFriendByOtherReqBean reqBean);

    @GET("/api/coupon/v1/member/page")
    Flowable<BaseResponse<String>> getMemberPageList();

    @POST("/api/coupon/v1/getMemberCoupon")
    Flowable<BaseResponse<String>> getMemberCoupon();

}