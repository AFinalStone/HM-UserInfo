package com.hm.iou.userinfo.bean;

import lombok.Data;

/**
 * @author syl
 * @time 2019/3/19 7:36 PM
 */
@Data
public class UserAuthenticationInfoBean {
    int sex;//(性别 0:女,1:男,3:未知)
    String name;//(真实姓名)
    String idCard;//(身份证编号)
    String idCardStartTime;//(身份证有效期开始时间)
    String idCardEndTime;//(身份证有效期结束时间)
    int personalInfo;//(个人信息 0未完善 1已完善)
    int idCardPhoto;//(证件照片 0已过期 1已上传)
    int facePhoto;//(人像照片 0未录用 1已录用)
    int underAge;//(年满18周岁 0未满足 1已满足)
    int writeSign;//(手写签名 0未录入 1已录入)
    String attestTime;//(认证时间)
    String avatarUrl;//（用户头像链接）
    int overDue;//（身份证是否过期 0未过期 1过期）
}
