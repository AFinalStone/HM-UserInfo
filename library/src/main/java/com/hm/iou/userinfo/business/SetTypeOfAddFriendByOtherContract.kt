package com.hm.iou.userinfo.business

import com.hm.iou.base.mvp.BaseContract

/**
 * 设置添加方式
 */

interface SetTypeOfAddFriendByOtherContract {

    interface View : BaseContract.BaseView {

        /**
         * 设置是否添加我为好友的时候是否需要验证
         */
        fun setIfCheckAddFriendByOther(isNeedCheck: Boolean)

        /**
         * 是否能够通过手机号搜索到我
         */
        fun setIfCanSearchMeByMobile(isCan: Boolean)
    }

    interface Presenter : BaseContract.BasePresenter {

        /**
         * 获取黑名单和隐藏合同
         */
        fun init()
    }
}