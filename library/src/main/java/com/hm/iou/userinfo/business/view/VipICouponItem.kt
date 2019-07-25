package com.hm.iou.userinfo.business.view

import com.hm.iou.userinfo.dict.CouPinStatusType

/**
 * Created by syl on 2019/7/23.
 */
interface VipICouponItem {

    fun getCouponId(): String

    fun getCouponName(): String

    fun getCouponDesc(): String

    fun getCouponStatus(): CouPinStatusType

    fun setCouponStatus(status: CouPinStatusType)

    fun isVIP(): Boolean
}