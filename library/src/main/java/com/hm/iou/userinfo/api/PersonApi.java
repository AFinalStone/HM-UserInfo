package com.hm.iou.userinfo.api;

import com.hm.iou.network.HttpReqManager;
import com.hm.iou.sharedata.model.BaseResponse;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.sharedata.model.UserThirdPlatformInfo;
import com.hm.iou.userinfo.bean.BitmapAndFileIdBean;
import com.hm.iou.userinfo.bean.IOUCountBean;
import com.hm.iou.userinfo.bean.IsWXExistBean;
import com.hm.iou.userinfo.bean.MemberBean;
import com.hm.iou.userinfo.bean.PayPackageResBean;
import com.hm.iou.userinfo.bean.UserAuthenticationInfoResBean;
import com.hm.iou.userinfo.bean.UserBankCardInfoResBean;
import com.hm.iou.userinfo.bean.UserCenterStatisticBean;
import com.hm.iou.userinfo.bean.UserEmailInfoResBean;
import com.hm.iou.userinfo.bean.UserSpaceBean;
import com.hm.iou.userinfo.bean.req.ChangeEmailReqBean;
import com.hm.iou.userinfo.bean.req.ChangeMobileReqBean;
import com.hm.iou.userinfo.bean.req.DelAccountReqBean;
import com.hm.iou.userinfo.bean.req.GetPayPackageListReqBean;
import com.hm.iou.userinfo.bean.req.ModifyPwdReqBean;
import com.hm.iou.userinfo.bean.req.SendMessageReqBean;
import com.hm.iou.userinfo.bean.req.UnbindWxReqBean;
import com.hm.iou.userinfo.bean.req.UpdateUserInfoReqBean;
import com.hm.iou.userinfo.bean.req.VerifyEmailPwdReqBean;
import com.hm.iou.userinfo.bean.req.VerifyMobilePwdReqBean;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hjy on 2018/5/23.
 */

public class PersonApi {

    private static PersonService getService() {
        return HttpReqManager.getInstance().getService(PersonService.class);
    }

    public static Flowable<BaseResponse<Object>> logout(String mobile) {
        return getService().logout(mobile).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 更新常住城市
     *
     * @param userInfo
     * @param location
     * @return
     */
    public static Flowable<BaseResponse<Object>> updateLocation(UserInfo userInfo, String location) {
        UpdateUserInfoReqBean reqBean = new UpdateUserInfoReqBean();
        reqBean.setUserId(userInfo.getUserId());
        reqBean.setLocation(location);
        return getService().updateUserInfo(reqBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Flowable<BaseResponse<Object>> updateNicknameAndSex(UserInfo userInfo, String nickname, int sex) {
        UpdateUserInfoReqBean reqBean = new UpdateUserInfoReqBean();
        reqBean.setUserId(userInfo.getUserId());
        reqBean.setSex(sex);
        reqBean.setNickName(nickname);
        return getService().updateUserInfo(reqBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Flowable<BaseResponse<Object>> updateIncome(UserInfo userInfo, int mainIncome, int secondIncome) {
        UpdateUserInfoReqBean reqBean = new UpdateUserInfoReqBean();
        reqBean.setUserId(userInfo.getUserId());
        reqBean.setMainIncome(mainIncome);
        reqBean.setSecondIncome(secondIncome);
        return getService().updateUserInfo(reqBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 更新头像地址
     *
     * @param userInfo
     * @param avatar   新的头像地址
     * @return
     */
    public static Flowable<BaseResponse<Object>> updateAvatar(UserInfo userInfo, String avatar) {
        UpdateUserInfoReqBean reqBean = new UpdateUserInfoReqBean();
        reqBean.setUserId(userInfo.getUserId());
        reqBean.setAvatarUrl(avatar);
        return getService().updateUserInfo(reqBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 更新职业证明材料
     *
     * @param userInfo
     * @param proveDocList
     * @return
     */
    public static Flowable<BaseResponse<Object>> updateProveDocList(UserInfo userInfo, String proveDocList) {
        UpdateUserInfoReqBean reqBean = new UpdateUserInfoReqBean();
        reqBean.setUserId(userInfo.getUserId());
        reqBean.setProveDocList(proveDocList);
        return getService().updateUserInfo(reqBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 修改密码
     *
     * @param oldPwd 老密码
     * @param newPwd 新密码
     * @return
     */
    public static Flowable<BaseResponse<Object>> modifyPassword(String oldPwd, String newPwd) {
        ModifyPwdReqBean reqBean = new ModifyPwdReqBean();
        reqBean.setOldPswd(oldPwd);
        reqBean.setNewPswd(newPwd);
        return getService().modifyPwd(reqBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static Flowable<BaseResponse<IsWXExistBean>> isWXExist(String code) {
        return getService().isWXExist(code).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 登录后绑定微信
     *
     * @param wxSn 微信流水号
     * @return
     */
    public static Flowable<BaseResponse<Object>> bindWXAfterLogin(String wxSn) {
        return getService().bindWXAfterLogin(wxSn).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 解绑微信
     *
     * @param mobile 手机号
     * @param pwd    密码
     * @return
     */
    public static Flowable<BaseResponse<Object>> unbindWeixin(String mobile, String pwd) {
        UnbindWxReqBean reqBean = new UnbindWxReqBean();
        reqBean.setMobile(mobile);
        reqBean.setQueryPswd(pwd);
        return getService().unbindWeixin(reqBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 更换手机时验证码密码
     *
     * @param mobile 手机号
     * @param pwd    密码
     * @return
     */
    public static Flowable<BaseResponse<Object>> verifyPwdWhenChangeMobile(String mobile, String pwd) {
        VerifyMobilePwdReqBean reqBean = new VerifyMobilePwdReqBean();
        reqBean.setOldMobile(mobile);
        reqBean.setQueryPswd(pwd);
        return getService().verifyPwdWhenChangeMobile(reqBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 发送短信验证码或者邮箱验证码
     *
     * @param type 用途--1:短信注册码，2:短信重置验证码，3:修改手机号，4:绑定邮箱，5:重置邮箱 ,
     * @param to   手机号码或者邮箱
     * @return
     */
    public static Flowable<BaseResponse<String>> sendMessage(int type, String to) {
        SendMessageReqBean reqBean = new SendMessageReqBean();
        reqBean.setPurpose(type);
        reqBean.setTo(to);
        return getService().sendMessage(reqBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 更换手机号
     *
     * @param newMobile 新手机号
     * @param checkcode 验证码
     * @param oldMobile 老手机号
     * @param pwd       原密码
     * @return
     */
    public static Flowable<BaseResponse<Object>> changeMobile(String newMobile, String checkcode, String oldMobile, String pwd) {
        ChangeMobileReqBean reqBean = new ChangeMobileReqBean();
        reqBean.setNewMobile(newMobile);
        reqBean.setCheckCode(checkcode);
        reqBean.setOldMobile(oldMobile);
        reqBean.setQueryPswd(pwd);
        return getService().changeMobile(reqBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取职业证明材料
     *
     * @return
     */
    public static Flowable<BaseResponse<List<BitmapAndFileIdBean>>> getProveDocList() {
        return getService().getProveDocList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 验证邮箱密码是否正确
     *
     * @param email
     * @param pwd
     * @return
     */
    public static Flowable<BaseResponse<String>> verifyEmailAndPwd(String email, String pwd) {
        VerifyEmailPwdReqBean reqBean = new VerifyEmailPwdReqBean();
        reqBean.setPwd(pwd);
        reqBean.setReceiverEmail(email);
        return getService().verifyEmailAndPwd(reqBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 更换邮箱
     *
     * @param oldEmail  老邮箱
     * @param newEmail  新的邮箱
     * @param sn        校验流水号
     * @param checkCode 验证码
     * @return
     */
    public static Flowable<BaseResponse<Object>> changeEmail(String oldEmail, String newEmail, String sn, String checkCode) {
        ChangeEmailReqBean reqBean = new ChangeEmailReqBean();
        reqBean.setOldEmail(oldEmail);
        reqBean.setNewEmail(newEmail);
        reqBean.setSn(sn);
        reqBean.setValidateCode(checkCode);
        return getService().changeEmail(reqBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 删除账户
     *
     * @param mobile 手机号
     * @param pwd    密码
     * @return
     */
    public static Flowable<BaseResponse<Object>> deleteAccount(String mobile, String pwd) {
        DelAccountReqBean reqBean = new DelAccountReqBean();
        reqBean.setOldMobile(mobile);
        reqBean.setQueryPswd(pwd);
        return getService().deleteAccount(reqBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取所有的借条类型统计数
     *
     * @return
     */
    public static Flowable<BaseResponse<List<IOUCountBean>>> getIOUCountByKind() {
        return getService().getIOUCountByKind().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取个人中心统计
     *
     * @return
     */
    public static Flowable<BaseResponse<UserCenterStatisticBean>> getUserCenterStatistic() {
        return getService().getUserCenterStatistic().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取云存储空间个人使用量
     *
     * @return
     */
    public static Flowable<BaseResponse<UserSpaceBean>> getUserSpace() {
        return getService().getUserSpace().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取用户绑定的第三方平台信息
     *
     * @return
     */
    public static Flowable<BaseResponse<UserThirdPlatformInfo>> getUserThirdPlatformInfo() {
        return getService().getUserThirdPlatformInfo().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取用户的实名认证信息
     *
     * @return
     */
    public static Flowable<BaseResponse<UserAuthenticationInfoResBean>> getRealNameInfo() {
        return getService().getRealNameInfo().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 修改支付宝账号
     *
     * @return
     */
    public static Flowable<BaseResponse<Object>> addOrUpdateAliPay(String aliPay) {
        return getService().addOrUpdateAliPay(aliPay).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取绑定的邮箱
     * \
     *
     * @return
     */
    public static Flowable<BaseResponse<UserEmailInfoResBean>> getUserBindEmailInfo() {
        return getService().getUserBindEmailInfo().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取绑定的银行卡
     *
     * @return
     */
    public static Flowable<BaseResponse<UserBankCardInfoResBean>> getUserBindBankCardInfo() {
        return getService().getUserBindBankCardInfo().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 查询套餐，剩余签章数量
     *
     * @return
     */
    public static Flowable<BaseResponse<PayPackageResBean>> getPackageList(GetPayPackageListReqBean reqBean) {
        return getService().getPackageList(reqBean).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取会员信息
     *
     * @return
     */
    public static Flowable<BaseResponse<MemberBean>> getMemberInfo() {
        return getService().getMemberInfo().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
