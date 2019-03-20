package com.hm.iou.userinfo.leftmenu;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.iou.base.utils.ImageLoadUtil;
import com.hm.iou.tools.ImageLoader;
import com.hm.iou.userinfo.R;

public class TopMenuAdapter extends BaseQuickAdapter<ITopMenuItem, BaseViewHolder> {

    private Context mContext;

    public TopMenuAdapter(Context context) {
        super(R.layout.person_layout_home_left_menu_top_item);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ITopMenuItem item) {
        helper.setText(R.id.tv_module_name, item.getIModuleName());
        helper.setTextColor(R.id.tv_module_name, item.getIModuleColor());
        ImageView ivModule = helper.getView(R.id.iv_module_image);
        ivModule.setColorFilter(item.getIModuleColor());
        String imageUrl = ImageLoadUtil.getImageRealUrl(mContext, item.getIModuleImage());
        ImageLoader.getInstance(mContext).displayImage(imageUrl, ivModule);
    }
}