package com.hm.iou.userinfo.bean.req;

import lombok.Data;

/**
 * Created by hjy on 2018/5/24.
 */
@Data
public class ForeverUnRegisterReqBean {

    private String oldMobile;
    private String queryPswd;
    private String verifyCode;

}