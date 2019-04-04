package com.hm.iou.userinfo.bean;

import com.hm.iou.userinfo.business.view.TellNoAgreeReasonActivity;

/**
 * @author syl
 * @time 2019/4/4 5:24 PM
 */

public class NoAgreeReasonBean implements TellNoAgreeReasonActivity.IReasonItem {

    private String reason;
    private String reasonId;

    @Override
    public String getTitle() {
        return reason == null ? "" : reason;
    }

    @Override
    public String getReasonId() {
        return reasonId == null ? "" : reasonId;
    }
}
