package com.hm.iou.userinfo.bean;

import lombok.Data;

@Data
public class CouponInfo {

    private String couponId;
    private String couponName;
    private String createTime;
    private String expiryDate;
    private int reachPrice;
    private int reducedPrice;

}
