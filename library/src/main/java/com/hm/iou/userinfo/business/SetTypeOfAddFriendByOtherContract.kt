package com.hm.iou.userinfo.business

import com.hm.iou.base.mvp.BaseContract

/**
 * 设置添加方式
 */

interface SetTypeOfAddFriendByOtherContract {

    interface View : BaseContract.BaseView {
        fun showDataEmpty()

        fun showInitView()

        fun hideInitView()

        fun showInitFailed(msg: String)
        /**
         * 显示初始化结果
         */
        fun showInitResult(isNeedCheck: Boolean, isCanSearchByMobile: Boolean)

    }

    interface Presenter : BaseContract.BasePresenter {

        /**
         * 获取黑名单和隐藏合同
         */
        fun init()

        /**
         * 更新用户修改的数据
         */
        fun updateUserChangeData(isNeedCheck: Boolean, isCanSearchByMobile: Boolean)
    }
}