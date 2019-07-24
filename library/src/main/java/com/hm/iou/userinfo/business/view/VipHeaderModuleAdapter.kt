package com.hm.iou.userinfo.business.view

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hm.iou.tools.ImageLoader
import com.hm.iou.userinfo.R

/**
 * Created by syl on 2019/7/23.
 */
class VipHeaderModuleAdapter : BaseQuickAdapter<VipIHeaderModuleItem, BaseViewHolder>(R.layout.person_layout_user_info_header_module_adapter) {

    override fun convert(helper: BaseViewHolder, item: VipIHeaderModuleItem) {
        ImageLoader.getInstance(mContext).displayImage(item.getModuleUrl(), helper.getView(R.id.iv_module))
        helper.setText(R.id.tv_title, item.getModuleTitle())
        helper.setText(R.id.tv_desc, item.getModuleDesc())
//        helper.setBackgroundRes(R.id.rl_content, item.getBackgroundResId())
    }

}