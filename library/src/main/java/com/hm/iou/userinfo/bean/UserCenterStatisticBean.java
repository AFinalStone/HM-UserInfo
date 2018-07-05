package com.hm.iou.userinfo.bean;

/**
 * Created by hjy on 2018/7/5.
 */

public class UserCenterStatisticBean {

    private int myCollect;
    private int noReadComplain;
    private long userSpaceSize;

    public int getMyCollect() {
        return myCollect;
    }

    public void setMyCollect(int myCollect) {
        this.myCollect = myCollect;
    }

    public int getNoReadComplain() {
        return noReadComplain;
    }

    public void setNoReadComplain(int noReadComplain) {
        this.noReadComplain = noReadComplain;
    }

    public long getUserSpaceSize() {
        return userSpaceSize;
    }

    public void setUserSpaceSize(long userSpaceSize) {
        this.userSpaceSize = userSpaceSize;
    }
}