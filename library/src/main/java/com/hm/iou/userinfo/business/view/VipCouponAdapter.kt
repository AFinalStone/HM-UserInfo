package com.hm.iou.userinfo.business.view

import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hm.iou.tools.kt.dp2px
import com.hm.iou.userinfo.R

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
        helper.setText(R.id.tv_status, item.getCouponStatus())
        helper.setBackgroundRes(R.id.tv_status, item.getRightResId())
        helper.addOnClickListener(R.id.tv_status)
    }

}