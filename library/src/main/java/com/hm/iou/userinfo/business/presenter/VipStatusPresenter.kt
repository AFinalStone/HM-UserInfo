package com.hm.iou.userinfo.business.presenter

import android.content.Context
import com.hm.iou.base.mvp.MvpActivityPresenter
import com.hm.iou.base.utils.CommSubscriber
import com.hm.iou.base.utils.RxUtil
import com.hm.iou.sharedata.UserManager
import com.hm.iou.sharedata.model.BaseResponse
import com.hm.iou.tools.MoneyFormatUtil
import com.hm.iou.userinfo.NavigationHelper
import com.hm.iou.userinfo.R
import com.hm.iou.userinfo.api.PersonApi
import com.hm.iou.userinfo.bean.*
import com.hm.iou.userinfo.bean.req.GetMemberCouPonReqBean
import com.hm.iou.userinfo.bean.req.GetPayPackageListReqBean
import com.hm.iou.userinfo.business.VipStatusContract
import com.hm.iou.userinfo.business.view.VipICouponItem
import com.hm.iou.userinfo.business.view.VipIHeaderModuleItem
import com.hm.iou.userinfo.business.view.VipStatusActivity
import com.hm.iou.userinfo.event.UpdateVipEvent
import com.trello.rxlifecycle2.android.ActivityEvent
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat


/**
 * Created by syl on 2019/7/23.
 */
class VipStatusPresenter(context: Context, view: VipStatusContract.View) : MvpActivityPresenter<VipStatusContract.View>(context, view), VipStatusContract.Presenter {

    private var mIsVIP: Boolean = false
    private var mVipEndTime: String? = null

    override fun init() {
        mView.showLoadingView()
        PersonApi.getMemberInfo()
                .compose(provider.bindUntilEvent<BaseResponse<MemberBean>>(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse<MemberBean>())
                .subscribeWith(object : CommSubscriber<MemberBean>(mView) {
                    override fun handleResult(memberBean: MemberBean) {
                        //用户头像
                        val userInfo = UserManager.getInstance(mContext).userInfo
                        val avatarUrl = userInfo.avatarUrl
                        val sex = userInfo.sex
                        val defaultAvatarResId = UserDataUtil.getDefaultAvatarBySex(sex)
                        mView.showHeaderInfo(avatarUrl, defaultAvatarResId)
                        //是否是VIP
                        if (memberBean.memType == 110) {   //如果是VIP
                            try {
                                mIsVIP = true
                                val sf01 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                val endTime = sf01.parse(memberBean.endDate)
                                val sf02 = SimpleDateFormat("yyyy年MM月dd日")
                                mVipEndTime = "VIP会员有效期至：" + sf02.format(endTime)
                                //更新用户VIP信息
                                val userInfo = UserManager.getInstance(mContext).userInfo
                                userInfo.memType = memberBean.memType
                                UserManager.getInstance(mContext).updateOrSaveUserInfo(userInfo)
                                EventBus.getDefault().post(UpdateVipEvent())
                            } catch (e: Exception) {

                            }
                        }
                        getMemberPageList()
                    }

                    override fun handleException(throwable: Throwable, s: String, s1: String) {
                        mView.closeCurrPage()
                    }
                })
    }

    fun getMemberPageList() {
        PersonApi.getMemberPageList()
                .compose(provider.bindUntilEvent<BaseResponse<GetMemberPageListRespBean>>(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse<GetMemberPageListRespBean>())
                .subscribeWith(object : CommSubscriber<GetMemberPageListRespBean>(mView) {

                    override fun handleResult(bean: GetMemberPageListRespBean?) {
                        mView.dismissLoadingView()
                        //优惠券套餐
                        val moduleList = bean?.modules
                        val packageList = bean?.coupons
                        //会员有效期
                        if (mIsVIP) {
                            mView.showVipUserInfoView(bean?.couponCount, mVipEndTime, changeModuleToVipIHeaderModuleItem(moduleList), changeCouponToVipICouponItem(packageList))
                        } else {
                            mView.showNoVipUserInfoView(bean?.couponCount, changeModuleToVipIHeaderModuleItem(moduleList), changeCouponToVipICouponItem(packageList))
                        }
                    }

                    override fun handleException(p0: Throwable?, p1: String?, p2: String?) {
                        mView.dismissLoadingView()
                        mView.closeCurrPage()
                    }
                })
    }

    override fun getCoupon(couponId: String) {
        mView.showLoadingView()
        val req = GetMemberCouPonReqBean(couponId)
        PersonApi.getMemberCoupon(req)
                .compose(provider.bindUntilEvent<BaseResponse<String>>(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse())
                .subscribeWith(object : CommSubscriber<String>(mView) {

                    override fun handleException(p0: Throwable?, p1: String?, p2: String?) {
                        mView.dismissLoadingView()
                    }

                    override fun handleResult(p0: String?) {
                        getMemberPageList()
                    }

                })
    }

    override fun getPayInfo() {
        mView.showLoadingView()
        val reqBean = GetPayPackageListReqBean(1, 3)
        PersonApi.getPackageList(reqBean)
                .compose(provider.bindUntilEvent<BaseResponse<PayPackageResBean>>(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse<PayPackageResBean>())
                .subscribeWith(object : CommSubscriber<PayPackageResBean>(mView) {
                    override fun handleResult(payPackageResBean: PayPackageResBean) {
                        mView.dismissLoadingView()
                        val list: List<PayPackageResBean.PackageRespListBean>? = payPackageResBean.packageRespList
                        if (!list.isNullOrEmpty()) {
                            val data = list?.get(0)
                            val payMoney: String?
                            val packageId: String? = data?.packageId.toString()
                            try {
                                val price: Long? = data?.actualPrice?.toLong()
                                payMoney = price?.let { MoneyFormatUtil.changeF2Y(it) }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                mView.toastMessage("发生异常，请稍后再试")
                                return
                            }
                            if (!payMoney.isNullOrEmpty() && !packageId.isNullOrEmpty()) {
                                NavigationHelper.toPayVipPage(mContext, payMoney, packageId, VipStatusActivity.REQ_CODE_TO_PAY_VIP_PAGE)
                            }
                        }
                    }

                    override fun handleException(throwable: Throwable, s: String, s1: String) {
                        mView.dismissLoadingView()
                    }
                })
    }

    fun changeCouponToVipICouponItem(list: List<Coupon>?): List<VipICouponItem> {
        val listResult: ArrayList<VipICouponItem> = ArrayList()
        list?.let {
            for (bean: Coupon in list) {
                var item = object : VipICouponItem {
                    override fun getCouponId(): String = bean.couponId ?: ""

                    override fun getCouponName(): String {
                        var name = ""
                        bean.couponName?.let {
                            name = bean.couponName.replace("¥", "¥ ")
                        }
                        return name
                    }

                    override fun getCouponDesc(): String = bean.couponDesc ?: ""

                    override fun getCouponStatus(): String {
                        var statusDesc = "领取"
                        bean.status?.let {
                            when (bean.status) {
                                1 -> statusDesc = "领取"
                                2 -> statusDesc = "去使用"
                                3 -> statusDesc = "已使用"
                            }
                        }
                        return statusDesc
                    }

                    override fun getRightResId(): Int {
                        var rightBgResId = R.drawable.person_bg_coupon_right_blue
                        bean.status?.let {
                            when (bean.status) {
                                1 -> {
                                    rightBgResId = if (mIsVIP) R.drawable.person_bg_coupon_right_orangered
                                    else R.drawable.person_bg_coupon_right_blue
                                }
                                2 -> rightBgResId = R.drawable.person_bg_coupon_right_blue
                                3 -> rightBgResId = R.drawable.person_bg_coupon_right_gray
                            }
                        }
                        return rightBgResId
                    }
                }
                listResult.add(item)
            }
        }
        return listResult
    }

    fun changeModuleToVipIHeaderModuleItem(list: List<Module>?): List<VipIHeaderModuleItem> {
        val listResult: ArrayList<VipIHeaderModuleItem> = ArrayList()
        list?.let {
            for (bean: Module in list) {
                var item = object : VipIHeaderModuleItem {

                    override fun getModuleUrl(): String = bean.picUrl ?: ""

                    override fun getModuleTitle(): String = bean.mainTitle ?: ""

                    override fun getModuleDesc(): String = bean.subTitle ?: ""

                    override fun getModuleFiltrateColor(): String? {
                        return if (mIsVIP) {
                            "#E9C4AC"
                        } else {
                            null
                        }
                    }

                }
                listResult.add(item)
            }
        }
        return listResult
    }

}