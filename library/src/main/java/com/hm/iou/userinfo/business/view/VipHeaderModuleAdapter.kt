package com.hm.iou.userinfo.business.view

import android.graphics.Color
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hm.iou.tools.ImageLoader
import com.hm.iou.userinfo.R

/**
 * Created by syl on 2019/7/23.
 */
class VipHeaderModuleAdapter : BaseQuickAdapter<VipIHeaderModuleItem, BaseViewHolder>(R.layout.person_layout_user_vip_info_header_module_adapter) {

    override fun convert(helper: BaseViewHolder, item: VipIHeaderModuleItem) {
        ImageLoader.getInstance(mContext).displayImage(item.getModuleUrl(), helper.getView(R.id.iv_module))
        helper.setText(R.id.tv_title, item.getModuleTitle())
        helper.setText(R.id.tv_desc, item.getModuleDesc())
        item.getModuleFiltrateColor()?.let {
            helper.getView<ImageView>(R.id.iv_module).setColorFilter(Color.parseColor(item.getModuleFiltrateColor()))
        }
    }

}