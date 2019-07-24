package com.hm.iou.userinfo.business;

import com.hm.iou.base.mvp.BaseContract;
import com.hm.iou.userinfo.business.view.VipICouponItem
import com.hm.iou.userinfo.business.view.VipIHeaderModuleItem

interface VipStatusContract {

    interface View : BaseContract.BaseView {

        /**
         * 显示普通用户信息
         * @param headerUrl
         * @param defaultAvatarResId
         */
        fun showNoVipUserInfoView(headerUrl: String?, defaultAvatarResId: Int, listModule: List<VipIHeaderModuleItem>?, listCoupon: List<VipICouponItem>?)

        /**
         * 显示VIP用户信息
         *
         * @param headerUrl
         * @param defaultAvatarResId
         * @param list
         * @param vipValidDate
         */
        fun showVipUserInfoView(headerUrl: String?, defaultAvatarResId: Int, vipValidDate: String?, listModule: List<VipIHeaderModuleItem>?, listCoupon: List<VipICouponItem>?)

    }

    interface Presenter : BaseContract.BasePresenter {

        fun init()

        fun getPayInfo()
    }

}