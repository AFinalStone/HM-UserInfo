package com.hm.iou.userinfo.business.presenter

import android.content.Context
import com.hm.iou.base.mvp.MvpActivityPresenter
import com.hm.iou.base.utils.CommSubscriber
import com.hm.iou.base.utils.RxUtil
import com.hm.iou.sharedata.UserManager
import com.hm.iou.sharedata.model.BaseResponse
import com.hm.iou.tools.MoneyFormatUtil
import com.hm.iou.userinfo.NavigationHelper
import com.hm.iou.userinfo.api.PersonApi
import com.hm.iou.userinfo.bean.MemberBean
import com.hm.iou.userinfo.bean.PayPackageResBean
import com.hm.iou.userinfo.bean.req.GetPayPackageListReqBean
import com.hm.iou.userinfo.business.VipStatusContract
import com.hm.iou.userinfo.business.view.VipStatusActivity.REQ_CODE_PAY
import com.hm.iou.userinfo.event.UpdateVipEvent
import com.trello.rxlifecycle2.android.ActivityEvent
import org.greenrobot.eventbus.EventBus


/**
 * Created by syl on 2019/7/23.
 */
class VipStatusPresenter(context: Context, view: VipStatusContract.View) : MvpActivityPresenter<VipStatusContract.View>(context, view), VipStatusContract.Presenter {


    override fun init() {

        getMemberInfo()
    }

    override fun getPayInfo() {
        mView.showLoadingView()
        val reqBean = GetPayPackageListReqBean()
        reqBean.channel = 1
        reqBean.scene = 3
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
                                NavigationHelper.toPayVipPage(mContext, payMoney, packageId, REQ_CODE_PAY)
                            }
                        }
                    }

                    override fun handleException(throwable: Throwable, s: String, s1: String) {
                        mView.dismissLoadingView()
                    }
                })
    }

    override fun getMemberInfo() {
        mView.showDataLoading(true)
        PersonApi.getMemberInfo()
                .compose(provider.bindUntilEvent<BaseResponse<MemberBean>>(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse<MemberBean>())
                .subscribeWith(object : CommSubscriber<MemberBean>(mView) {
                    override fun handleResult(memberBean: MemberBean) {
                        mView.showDataLoading(false)
                        if (memberBean.memType == 110) {   //如果是VIP
                            mView.showVipUserInfoView()

                            val endDate = memberBean.endDate
                            if (endDate != null && endDate.length >= 10) {
                                val y = endDate.substring(0, 4)
                                val m = endDate.substring(5, 7)
                                val d = endDate.substring(8, 10)
                                val validDate = String.format("VIP会员有效期至：%s年%s月%s日", y, m, d)
                                mView.showVipValidDate(validDate)
                            }

                            //更新用户VIP信息
                            val userInfo = UserManager.getInstance(mContext).userInfo
                            userInfo.memType = memberBean.memType
                            UserManager.getInstance(mContext).updateOrSaveUserInfo(userInfo)

                            EventBus.getDefault().post(UpdateVipEvent())

                        } else {
                            mView.showCommUserInfoView()
                        }
                    }

                    override fun handleException(throwable: Throwable, s: String, s1: String) {
                        mView.showDataLoadError()
                    }
                })
    }

    /**
     * 显示用户头像
     */
    private fun showUserAvatar() {
        val userInfo = UserManager.getInstance(mContext).userInfo
        val avatarUrl = userInfo.avatarUrl
        //头像
        val sex = userInfo.sex
        val defaultAvatarResId = UserDataUtil.getDefaultAvatarBySex(sex)
        mView.showAvatar(avatarUrl, defaultAvatarResId)
    }

}