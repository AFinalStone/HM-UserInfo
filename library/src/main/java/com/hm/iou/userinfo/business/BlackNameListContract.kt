package com.hm.iou.userinfo.business

import com.hm.iou.base.mvp.BaseContract
import com.hm.iou.userinfo.bean.BlackNameBean

/**
 * 黑名单列表
 */

interface BlackNameListContract {

    interface View : BaseContract.BaseView {

        fun showInitView()

        fun hideInitView()

        fun showDataEmpty()

        fun showInitFailed(msg: String)
        /**
         * 设置添加我为好友的时候是否需要验证
         */
        fun showBlackNameList(list: List<BlackNameBean>)

    }

    interface Presenter : BaseContract.BasePresenter {

        /**
         * 获取黑名单列表
         */
        fun getBlackNameList()
    }
}