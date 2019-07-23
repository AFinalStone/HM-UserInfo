package com.hm.iou.userinfo.business.view

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hm.iou.userinfo.R

/**
 * Created by syl on 2019/7/23.
 */
class VipCouponAdapter : BaseQuickAdapter<VipICouponItem, BaseViewHolder>(R.layout.person_layout_user_vip_info_adapter) {


    override fun convert(helper: BaseViewHolder, item: VipICouponItem) {
        helper.setText(R.id.tv_price, item.getCouponPrice())
        helper.setText(R.id.tv_desc, item.getCouponDesc())
        helper.setText(R.id.tv_status, item.getCouponStatus())
//        helper.setBackgroundRes(R.id.rl_content, item.getBackgroundResId())
    }

}