package com.hm.iou.userinfo.business

import com.hm.iou.base.mvp.BaseContract
import com.hm.iou.userinfo.bean.HideContractBean

/**
 * 隐藏合同列表
 */

interface HideContractListContract {

    interface View : BaseContract.BaseView {

        fun showDataEmpty()

        fun showInitView()

        fun hideInitView()

        fun showInitFailed(msg: String)
        /**
         * 设置添加我为好友的时候是否需要验证
         */
        fun showHideContractList(list: List<HideContractBean>)

    }

    interface Presenter : BaseContract.BasePresenter {

        /**
         * 获取隐藏合同列表
         */
        fun getHideContractList()
    }
}