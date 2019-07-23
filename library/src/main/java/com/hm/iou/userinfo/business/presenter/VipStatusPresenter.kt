package com.hm.iou.userinfo.business.presenter

import android.content.Context
import com.hm.iou.base.mvp.MvpActivityPresenter
import com.hm.iou.sharedata.UserManager
import com.hm.iou.userinfo.business.VipStatusContract


/**
 * Created by syl on 2019/7/23.
 */
class VipStatusPresenter(context: Context, view: VipStatusContract.View) : MvpActivityPresenter<VipStatusContract.View>(context, view), VipStatusContract.Presenter {


    override fun init() {
        val userInfo = UserManager.getInstance(mContext).userInfo
        val avatarUrl = userInfo.avatarUrl
        //头像
        val sex = userInfo.sex
        val defaultAvatarResId = UserDataUtil.getDefaultAvatarBySex(sex)
        mView.showNoVipUserInfoView(avatarUrl, defaultAvatarResId, null)
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

    override fun getMemberInfo() {
        mView.showLoadingView()

    }

//    /**
//     * 显示用户头像
//     */
//    private fun showUserAvatar() {
//        val userInfo = UserManager.getInstance(mContext).userInfo
//        val avatarUrl = userInfo.avatarUrl
//        //头像
//        val sex = userInfo.sex
//        val defaultAvatarResId = UserDataUtil.getDefaultAvatarBySex(sex)
//        mView.showAvatar(avatarUrl, defaultAvatarResId)
//    }

}