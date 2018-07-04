package com.hm.iou.userinfo.bean.req;

/**
 * Created by hjy on 2018/5/24.
 */

public class UnbindWxReqBean {

    private String mobile;
    private String queryPswd;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getQueryPswd() {
        return queryPswd;
    }

    public void setQueryPswd(String queryPswd) {
        this.queryPswd = queryPswd;
    }
}
