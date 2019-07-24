package com.hm.iou.userinfo.business.presenter

import android.content.Context
import com.hm.iou.base.mvp.MvpActivityPresenter
import com.hm.iou.base.utils.CommSubscriber
import com.hm.iou.base.utils.RxUtil
import com.hm.iou.sharedata.UserManager
import com.hm.iou.sharedata.model.BaseResponse
import com.hm.iou.userinfo.R
import com.hm.iou.userinfo.api.PersonApi
import com.hm.iou.userinfo.bean.Coupon
import com.hm.iou.userinfo.bean.GetMemberPageListRespBean
import com.hm.iou.userinfo.bean.Module
import com.hm.iou.userinfo.business.VipStatusContract
import com.hm.iou.userinfo.business.view.VipICouponItem
import com.hm.iou.userinfo.business.view.VipIHeaderModuleItem
import com.trello.rxlifecycle2.android.ActivityEvent


/**
 * Created by syl on 2019/7/23.
 */
class VipStatusPresenter(context: Context, view: VipStatusContract.View) : MvpActivityPresenter<VipStatusContract.View>(context, view), VipStatusContract.Presenter {


    override fun init() {
        mView.showLoadingView()
        PersonApi.getMemberPageList()
                .compose(provider.bindUntilEvent<BaseResponse<GetMemberPageListRespBean>>(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse<GetMemberPageListRespBean>())
                .subscribeWith(object : CommSubscriber<GetMemberPageListRespBean>(mView) {

                    override fun handleResult(bean: GetMemberPageListRespBean?) {
                        mView.dismissLoadingView()
                        //用户头像
                        val userInfo = UserManager.getInstance(mContext).userInfo
                        val avatarUrl = userInfo.avatarUrl
                        val sex = userInfo.sex
                        val defaultAvatarResId = UserDataUtil.getDefaultAvatarBySex(sex)
                        //优惠券套餐
                        val packageList = bean?.coupons
                        val moduleList = bean?.modules
                        mView.showNoVipUserInfoView(avatarUrl, defaultAvatarResId, changeModuleToVipIHeaderModuleItem(moduleList), changeCouponToVipICouponItem(packageList))
                    }

                    override fun handleException(p0: Throwable?, p1: String?, p2: String?) {
                        mView.dismissLoadingView()
                        mView.closeCurrPage()
                    }


                })
    }

    override fun getPayInfo() {
//        mView.showLoadingView()
//        val reqBean = GetPayPackageListReqBean()
//        reqBean.channel = 1
//        reqBean.scene = 3
//        PersonApi.getPackageList(reqBean)
//                .compose(provider.bindUntilEvent<BaseResponse<PayPackageResBean>>(ActivityEvent.DESTROY))
//                .map(RxUtil.handleResponse<PayPackageResBean>())
//                .subscribeWith(object : CommSubscriber<PayPackageResBean>(mView) {
//                    override fun handleResult(payPackageResBean: PayPackageResBean) {
//                        mView.dismissLoadingView()
//                        val list: List<PayPackageResBean.PackageRespListBean>? = payPackageResBean.packageRespList
//                        if (!list.isNullOrEmpty()) {
//                            val data = list?.get(0)
//                            val payMoney: String?
//                            val packageId: String? = data?.packageId.toString()
//                            try {
//                                val price: Long? = data?.actualPrice?.toLong()
//                                payMoney = price?.let { MoneyFormatUtil.changeF2Y(it) }
//                            } catch (e: Exception) {
//                                e.printStackTrace()
//                                mView.toastMessage("发生异常，请稍后再试")
//                                return
//                            }
//                            if (!payMoney.isNullOrEmpty() && !packageId.isNullOrEmpty()) {
////                                NavigationHelper.toPayVipPage(mContext, payMoney, packageId, REQ_CODE_PAY)
//                            }
//                        }
//                    }
//
//                    override fun handleException(throwable: Throwable, s: String, s1: String) {
//                        mView.dismissLoadingView()
//                    }
//                })
    }

    fun changeCouponToVipICouponItem(list: List<Coupon>?): List<VipICouponItem> {
        val listResult: ArrayList<VipICouponItem> = ArrayList()
        list?.let {
            for (bean: Coupon in list) {
                var item = object : VipICouponItem {
                    override fun getCouponId(): String = bean.couponId ?: ""

                    override fun getCouponPrice(): String = bean.reachPrice ?: ""

                    override fun getCouponDesc(): String = bean.couponName ?: ""

                    override fun getCouponStatus(): String = "领取"

                    override fun getBackgroundResId(): Int = R.color.uikit_red
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

                    override fun getModuleFiltrateColor(): String = bean.picUrl ?: ""

                }
                listResult.add(item)
            }
        }
        return listResult
    }

}