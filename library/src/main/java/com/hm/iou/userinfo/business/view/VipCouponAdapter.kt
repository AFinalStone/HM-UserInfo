package com.hm.iou.userinfo.business.view

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hm.iou.tools.kt.dp2px
import com.hm.iou.userinfo.R
import com.hm.iou.userinfo.dict.CouPinStatusType

/**
 * Created by syl on 2019/7/23.
 */
class VipCouponAdapter : BaseQuickAdapter<VipICouponItem, BaseViewHolder>(R.layout.person_layout_user_vip_info_adapter) {


    override fun convert(helper: BaseViewHolder, item: VipICouponItem) {
        val tvName: TextView = helper.getView(R.id.tv_name)
        val msp = SpannableString(item.getCouponName())
        msp.setSpan(AbsoluteSizeSpan(tvName.context.dp2px(30)), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvName.text = msp
        helper.setText(R.id.tv_desc, item.getCouponDesc())

        when (item.getCouponStatus()) {
            CouPinStatusType.WAIT_GET -> {//等待领取
                if (item.isVIP()) {
                    helper.setBackgroundRes(R.id.tv_status, R.drawable.person_bg_coupon_right_orangered)
                } else {
                    helper.setBackgroundRes(R.id.tv_status, R.drawable.person_bg_coupon_right_blue)
                }
                helper.setText(R.id.tv_status, "领取")
                helper.addOnClickListener(R.id.tv_status)
            }
            CouPinStatusType.TO_EXPENSE -> {//去使用
                helper.setText(R.id.tv_status, "去使用")
                helper.setBackgroundRes(R.id.tv_status, R.drawable.person_bg_coupon_right_blue)
                helper.addOnClickListener(R.id.tv_status)

            }
            CouPinStatusType.HAVE_USE -> {//已使用
                helper.setText(R.id.tv_status, "已使用")
                helper.setBackgroundRes(R.id.tv_status, R.drawable.person_bg_coupon_right_gray)
                tvName.setTextColor(Color.parseColor("#9faabd"))
                helper.setTextColor(R.id.tv_desc, Color.parseColor("#c7cddd"))
                helper.setTextColor(R.id.tv_status, Color.parseColor("#f1feff"))
            }
        }
    }


}