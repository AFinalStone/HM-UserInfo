package com.hm.iou.userinfo.bean

/**
 * Created by syl on 2019/7/8.
 */
class UpdateTypeOfAddFriendByOtherBean {
    var ifCreateImToken: Boolean = false
    lateinit var imTokenResp: Item

    class Item {
        lateinit var imAccId: String;
        lateinit var imToken: String;
    }

}