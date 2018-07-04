package com.hm.iou.userinfo.bean.req;

/**
 * Created by hjy on 2018/5/23.
 */

public class ModifyPwdReqBean {

    private String newPswd;
    private String oldPswd;

    public String getNewPswd() {
        return newPswd;
    }

    public void setNewPswd(String newPswd) {
        this.newPswd = newPswd;
    }

    public String getOldPswd() {
        return oldPswd;
    }

    public void setOldPswd(String oldPswd) {
        this.oldPswd = oldPswd;
    }
}
