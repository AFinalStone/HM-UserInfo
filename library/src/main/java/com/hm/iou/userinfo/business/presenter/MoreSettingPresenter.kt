package com.hm.iou.userinfo.business.presenter

import android.content.Context
import com.hm.iou.base.mvp.MvpActivityPresenter
import com.hm.iou.base.utils.CommSubscriber
import com.hm.iou.base.utils.RxUtil
import com.hm.iou.userinfo.api.PersonApi
import com.hm.iou.userinfo.bean.BlackNameAndHideContractNumBean
import com.hm.iou.userinfo.business.MoreSettingContract
import com.trello.rxlifecycle2.android.ActivityEvent

/**
 * Created by syl on 2019/7/8.
 */
class MoreSettingPresenter(context: Context, view: MoreSettingContract.View) : MvpActivityPresenter<MoreSettingContract.View>(context, view), MoreSettingContract.Presenter {

    override fun getBlackNameAndHideContract() {

        PersonApi.getBlackNameAndHideContractNum()
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse<BlackNameAndHideContractNumBean>())
                .subscribeWith(object : CommSubscriber<BlackNameAndHideContractNumBean>(mView) {

                    override fun handleResult(p0: BlackNameAndHideContractNumBean?) {
                        if (p0 == null) {
                            mView.setBlackNameItemVisible(false)
                            mView.setHideContractItemVisible(false)
                            return
                        }
                        mView.setBlackNameItemVisible(p0.blackFriendCount > 0)
                        mView.setHideContractItemVisible(p0.hideIouCount > 0)
                    }

                    override fun handleException(p0: Throwable?, p1: String?, p2: String?) {

                    }

                })
    }

}