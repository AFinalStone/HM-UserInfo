package com.hm.iou.userinfo.bean;

import lombok.Data;

/**
 * @author syl
 * @time 2019/3/19 7:36 PM
 */
@Data
public class UserBankCardInfoResBean {


    /**
     * bankAscription : 本人
     * bankCardNo : 123456789
     * bankName : 招商银行
     * bankType : 借记卡
     * bindTime : 2019-03-20T05:46:00.474Z
     * mobile : 15267163669
     */

    private String bankAscription;
    private String bankCardNo;
    private String bankName;
    private String bankType;
    private String bindTime;
    private String mobile;

}
