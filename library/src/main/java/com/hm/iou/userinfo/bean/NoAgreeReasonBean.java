package com.hm.iou.userinfo.bean;

import com.hm.iou.userinfo.business.view.TellNoAgreeReasonActivity;

import lombok.Data;

/**
 * @author syl
 * @time 2019/4/4 5:24 PM
 */
@Data
public class NoAgreeReasonBean implements TellNoAgreeReasonActivity.IReasonItem {

    private int feedbackId;
    private String content;

    @Override
    public String getTitle() {
        return content == null ? "" : content;
    }

    @Override
    public int getReasonId() {
        return feedbackId;
    }
}
