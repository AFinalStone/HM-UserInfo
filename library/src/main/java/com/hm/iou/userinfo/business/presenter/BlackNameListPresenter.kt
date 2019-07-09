package com.hm.iou.userinfo.business.presenter

import android.content.Context
import com.hm.iou.base.mvp.MvpActivityPresenter
import com.hm.iou.base.utils.CommSubscriber
import com.hm.iou.base.utils.RxUtil
import com.hm.iou.userinfo.api.PersonApi
import com.hm.iou.userinfo.bean.BlackNameBean
import com.hm.iou.userinfo.business.BlackNameListContract
import com.trello.rxlifecycle2.android.ActivityEvent

/**
 * Created by syl on 2019/7/8.
 */
class BlackNameListPresenter(context: Context, view: BlackNameListContract.View) : MvpActivityPresenter<BlackNameListContract.View>(context, view), BlackNameListContract.Presenter {
    override fun getBlackNameList() {
        mView.showInitView()
        PersonApi.getBlackNameList()
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse<List<BlackNameBean>>())
                .subscribeWith(object : CommSubscriber<List<BlackNameBean>>(mView) {

                    override fun handleResult(list: List<BlackNameBean>?) {
                        if (list == null || list.isEmpty()) {
                            mView.showDataEmpty()
                            return
                        }
                        mView.hideInitView()
                        mView.showBlackNameList(list)
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