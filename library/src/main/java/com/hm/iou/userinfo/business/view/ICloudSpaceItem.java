package com.hm.iou.userinfo.business.view;

/**
 * Created by hjy on 2018/7/4.
 * 云存储空间使用量列表数据
 */

public interface ICloudSpaceItem {

    int getIcon();

    CharSequence getTitle();

    CharSequence getCount();
}