package com.hm.iou.userinfo.bean

/**
 * Created by hjy on 2019/9/5.
 */
class InnerCustomerResBean {

    //true-当前登录用户为客服，false-不是客服
    var isBackendUser: Boolean = false
    //待处理反馈数目
    var dealEventCount: Int = 0

}