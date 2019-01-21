package com.hm.iou.userinfo.bean.req;

import lombok.Data;

/**
 * Created by hjy on 2018/5/23.
 */

@Data
public class UpdateUserInfoReqBean {

    private String userId;

    private String avatarUrl;
    private String location;
    private String mailAddr;
    private String mobile;
    private String nickName;
    private String proveDocList;

    private Integer mainIncome;
    private Integer secondIncome;
    private Integer sex;

}
