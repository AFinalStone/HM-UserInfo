package com.hm.iou.userinfo.leftmenu;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.iou.userinfo.R;

public class MenuAdapter extends BaseQuickAdapter<IListMenuItem, BaseViewHolder> {

    private int mColor;

    public MenuAdapter(Context context) {
        super(R.layout.main_layout_home_left_menu_list_item);
        mContext = context;
        mColor = mContext.getResources().getColor(R.color.uikit_text_sub_content);
    }

    @Override
    protected void convert(BaseViewHolder helper, IListMenuItem item) {
        helper.setText(R.id.tv_menu_name, item.getIModuleName());
        String redMsg = item.getIMenuRedMsg();
        ImageView ivArrow = helper.getView(R.id.iv_arrow);
        ivArrow.setColorFilter(mColor);
        if (!TextUtils.isEmpty(redMsg)) {
            helper.setGone(R.id.tv_menu_red_msg, true);
            helper.setText(R.id.tv_menu_desc, "");
            helper.setText(R.id.tv_menu_red_msg, redMsg);
            return;
        }
        helper.setGone(R.id.tv_menu_red_msg, false);
        helper.setText(R.id.tv_menu_desc, item.getIMenuDesc());
    }

}