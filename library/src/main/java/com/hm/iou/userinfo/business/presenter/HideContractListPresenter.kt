package com.hm.iou.userinfo.business.presenter

import android.content.Context
import com.hm.iou.base.mvp.MvpActivityPresenter
import com.hm.iou.base.utils.CommSubscriber
import com.hm.iou.base.utils.RxUtil
import com.hm.iou.userinfo.api.PersonApi
import com.hm.iou.userinfo.bean.HideContractBean
import com.hm.iou.userinfo.business.HideContractListContract
import com.trello.rxlifecycle2.android.ActivityEvent

/**
 * Created by syl on 2019/7/8.
 */
class HideContractListPresenter(context: Context, view: HideContractListContract.View) : MvpActivityPresenter<HideContractListContract.View>(context, view), HideContractListContract.Presenter {
    override fun getHideContractList() {
        mView.showInitView()
        PersonApi.getHideContractList()
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse<List<HideContractBean>>())
                .subscribeWith(object : CommSubscriber<List<HideContractBean>>(mView) {

                    override fun handleResult(list: List<HideContractBean>?) {
                        if (list == null || list.isEmpty()) {
                            mView.showDataEmpty()
                            return
                        }
                        mView.hideInitView()
                        mView.showHideContractList(list)
                    }

                    override fun handleException(p0: Throwable?, p1: String?, p2: String) {
                        mView.showInitFailed(p2)
                    }


                    override fun isShowBusinessError(): Boolean {
                        return false
                    }

                    override fun isShowCommError(): Boolean {
                        return false
                    }

                })
    }


}