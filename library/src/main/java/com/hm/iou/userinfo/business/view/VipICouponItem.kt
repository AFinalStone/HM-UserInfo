package com.hm.iou.userinfo.business.view

/**
 * Created by syl on 2019/7/23.
 */
interface VipICouponItem {

    fun getCouponId(): String

    fun getCouponPrice(): String

    fun getCouponDesc(): String

    fun getCouponStatus(): String

    fun getBackgroundResId(): Int
}