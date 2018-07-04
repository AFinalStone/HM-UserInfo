package com.hm.iou.userinfo.bean.req;

/**
 * Created by hjy on 2018/5/24.
 */

public class ChangeMobileReqBean {

    private String newMobile;
    private String checkCode;
    private String oldMobile;
    private String queryPswd;

    public String getNewMobile() {
        return newMobile;
    }

    public void setNewMobile(String newMobile) {
        this.newMobile = newMobile;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

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
