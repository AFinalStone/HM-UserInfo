package com.hm.iou.userinfo.business

import com.hm.iou.base.mvp.BaseContract

/**
 * 更多设置
 * Created by syl on 2019/7/8.
 */

interface MoreSettingContract {

    interface View : BaseContract.BaseView {

        /**
         * 是否显示黑名单条目
         */
        fun setBlackNameItemVisible(isShow: Boolean)

        /**
         * 是否显示隐藏合同条目
         */
        fun setHideContractItemVisible(isShow: Boolean)
    }

    interface Presenter : BaseContract.BasePresenter {

        /**
         * 获取黑名单和隐藏合同
         */
        fun getBlackNameAndHideContract()
    }
}