package com.hm.iou.userinfo.bean;

import lombok.Data;

/**
 * @author syl
 * @time 2019/3/19 7:36 PM
 */
@Data
public class UserEmailInfoResBean {
    /**
     * attestTime : 2019-03-20T05:46:00.517Z
     * avatarUrl : string
     * facePhoto : true
     * idCardEndTime : 2019-03-20T05:46:00.517Z
     * idCardNum : string
     * idCardPhoto : true
     * idCardStartTime : 2019-03-20T05:46:00.517Z
     * overDue : true
     * personalInfo : true
     * realName : string
     * sex : 0
     * underAge : true
     * writeSign : true
     */

    private int sex;//(性别 0:女,1:男,3:未知)
    private String realName;//(真实姓名)
    private String idCardNum;//(身份证编号)
    private String attestTime;//(认证时间)
    private String avatarUrl;//（用户头像链接）
    private boolean facePhoto;//(人像照片 0未录用 1已录用)
    private String idCardEndTime;//(身份证有效期结束时间)
    private boolean idCardPhoto;//(证件照片 0已过期 1已上传)
    private String idCardStartTime;//(身份证有效期开始时间)
    private boolean overDue;//（身份证是否过期 0未过期 1过期）
    private boolean personalInfo;//(个人信息 0未完善 1已完善)
    private boolean underAge;//(年满18周岁 0未满足 1已满足)
    private boolean writeSign;//(手写签名 0未录入 1已录入)

}
