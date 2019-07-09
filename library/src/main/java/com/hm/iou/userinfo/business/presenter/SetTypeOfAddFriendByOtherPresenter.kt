package com.hm.iou.userinfo.business.presenter

import android.content.Context
import com.hm.iou.base.mvp.MvpActivityPresenter
import com.hm.iou.base.utils.CommSubscriber
import com.hm.iou.base.utils.RxUtil
import com.hm.iou.userinfo.api.PersonApi
import com.hm.iou.userinfo.bean.TypeOfAddFriendByOtherBean
import com.hm.iou.userinfo.business.SetTypeOfAddFriendByOtherContract
import com.trello.rxlifecycle2.android.ActivityEvent

/**
 * Created by syl on 2019/7/8.
 */
class SetTypeOfAddFriendByOtherPresenter(context: Context, view: SetTypeOfAddFriendByOtherContract.View) : MvpActivityPresenter<SetTypeOfAddFriendByOtherContract.View>(context, view), SetTypeOfAddFriendByOtherContract.Presenter {

    override fun init() {
        PersonApi.getTypeOfAddFriendByOther()
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse<TypeOfAddFriendByOtherBean>())
                .subscribeWith(object : CommSubscriber<TypeOfAddFriendByOtherBean>(mView) {

                    override fun handleResult(p0: TypeOfAddFriendByOtherBean?) {
                        if (p0 == null) {
                            mView.setIfCheckAddFriendByOther(true)
                            mView.setIfCheckAddFriendByOther(true)
                            return
                        }
                        mView.setIfCheckAddFriendByOther(true)
                        mView.setIfCanSearchMeByMobile(true)
                    }

                    override fun handleException(p0: Throwable?, p1: String?, p2: String?) {
                        mView.setIfCheckAddFriendByOther(true)
                        mView.setIfCheckAddFriendByOther(true)
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