package com.hm.iou.userinfo.bean

/**
 * Created by syl on 2019/7/23.
 */
data class GetMemberPageListRespBean(
        val coupons: List<Coupon>?,
        val getToday: Boolean?,
        val modules: List<Module>?,
        val remainDays: Int?
)

data class Coupon(
        val couponId: String?,
        val couponName: String?,
        val reachPrice: String?,
        val reducePrice: String?,
        val status: Int?
)

data class Module(
        val mainTitle: String?,
        val picUrl: String?,
        val position: Int?,
        val subTitle: String?
)