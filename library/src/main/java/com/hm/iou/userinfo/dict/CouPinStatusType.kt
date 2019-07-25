package com.hm.iou.userinfo.dict

/**
 * Created by syl on 2019/7/25.
 */

enum class CouPinStatusType(val status: Int, val des: String) {
    WAIT_GET(1, "领取"),
    TO_EXPENSE(2, "去使用"),
    HAVE_USE(3, "已使用")
}
