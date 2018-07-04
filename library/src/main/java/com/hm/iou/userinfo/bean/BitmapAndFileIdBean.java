package com.hm.iou.userinfo.bean;

/**
 * Created by hjy on 2018/7/2.
 */

public class BitmapAndFileIdBean {

    String fileId;
    String fileUrl;

    public BitmapAndFileIdBean(String fileId, String fileUrl) {
        this.fileId = fileId;
        this.fileUrl = fileUrl;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
