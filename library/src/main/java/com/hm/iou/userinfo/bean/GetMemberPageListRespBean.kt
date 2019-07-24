package com.hm.iou.userinfo.bean

/**
 * Created by syl on 2019/7/23.
 */
data class GetMemberPageListRespBean(
        val coupons: List<Coupon>?,
        val getToday: Boolean?,
        val modules: List<Module>?,
        val remainDays: Int?,
        val couponCount: Int?
)

data class Coupon(
        val couponId: String?,
        val couponName: String?,
        val couponDesc: String?,
        val status: Int?//1未领取 2已领取未使用 3已使用
)

data class Module(
        val mainTitle: String?,
        val picUrl: String?,
        val position: Int?,
        val subTitle: String?
)