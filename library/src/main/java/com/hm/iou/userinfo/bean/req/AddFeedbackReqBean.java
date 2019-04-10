package com.hm.iou.userinfo.bean.req;

import lombok.Data;

/**
 * Created by hjy on 2019/4/9.
 */
@Data
public class AddFeedbackReqBean {

    private int feedbackId;
    private int osType;     //1:ios，2：android

}
