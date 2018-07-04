package com.hm.iou.userinfo.bean.req;

/**
 * Created by hjy on 2018/5/24.
 */

public class VerifyMobilePwdReqBean {

    private String oldMobile;

    private String queryPswd;

    public String getOldMobile() {
        return oldMobile;
    }

    public void setOldMobile(String oldMobile) {
        this.oldMobile = oldMobile;
    }

    public String getQueryPswd() {
        return queryPswd;
    }

    public void setQueryPswd(String queryPswd) {
        this.queryPswd = queryPswd;
    }
}
