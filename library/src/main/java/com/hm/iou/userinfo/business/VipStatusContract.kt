package com.hm.iou.userinfo.business;

import com.hm.iou.base.mvp.BaseContract;
import com.hm.iou.userinfo.business.view.VipICouponItem
import com.hm.iou.userinfo.business.view.VipIHeaderModuleItem

interface VipStatusContract {

    interface View : BaseContract.BaseView {

        /**
         * 显示头部头像
         */
//        fun showHeaderInfo(headerUrl: String?, defaultAvatarResId: Int)

        /**
         * 显示普通用户信息
         */
        fun showNoVipUserInfoView(headerUrl: String?, defaultAvatarResId: Int, remindDay: Int?, listModule: List<VipIHeaderModuleItem>?, listCoupon: List<VipICouponItem>?)

        fun updateCouPonItem(position: Int)
        /**
         * 显示VIP用户信息
         *
         * @param vipValidDate
         */
        fun showVipUserInfoView(headerUrl: String?, defaultAvatarResId: Int, remindDay: Int?, vipValidDate: String?, listModule: List<VipIHeaderModuleItem>?, listCoupon: List<VipICouponItem>?)

    }

    interface Presenter : BaseContract.BasePresenter {

        fun init()

        fun getPayInfo()

        fun getCoupon(couponId: String, position: Int)

    }

}