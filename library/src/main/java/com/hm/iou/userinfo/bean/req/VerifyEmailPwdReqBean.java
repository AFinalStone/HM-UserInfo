package com.hm.iou.userinfo.bean.req;

/**
 * Created by hjy on 2018/5/24.
 */

public class VerifyEmailPwdReqBean {

    private String pwd;
    private String receiverEmail;

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }
}