package com.hm.iou.userinfo.business.presenter

import android.content.Context
import com.hm.iou.base.mvp.MvpActivityPresenter
import com.hm.iou.base.utils.CommSubscriber
import com.hm.iou.base.utils.RxUtil
import com.hm.iou.sharedata.UserManager
import com.hm.iou.sharedata.event.CommBizEvent
import com.hm.iou.userinfo.api.PersonApi
import com.hm.iou.userinfo.bean.TypeOfAddFriendByOtherResBean
import com.hm.iou.userinfo.bean.UpdateTypeOfAddFriendByOtherResBean
import com.hm.iou.userinfo.business.SetTypeOfAddFriendByOtherContract
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.EventBus

/**
 * Created by syl on 2019/7/8.
 */
class SetTypeOfAddFriendByOtherPresenter(context: Context, view: SetTypeOfAddFriendByOtherContract.View) : MvpActivityPresenter<SetTypeOfAddFriendByOtherContract.View>(context, view), SetTypeOfAddFriendByOtherContract.Presenter {

    private var mIsNeedCheck: Boolean? = null
    private var mIsCanSearchByMobile: Boolean? = null
    private var mDisposable: Disposable? = null
    override fun init() {
        mView.showInitView()
        PersonApi.getTypeOfAddFriendByOther()
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .map(RxUtil.handleResponse<TypeOfAddFriendByOtherResBean>())
                .subscribeWith(object : CommSubscriber<TypeOfAddFriendByOtherResBean>(mView) {

                    override fun handleResult(p0: TypeOfAddFriendByOtherResBean?) {
                        if (p0 == null) {
                            mView.showInitResult(true, true)
                            mView.hideInitView()
                            return
                        }
                        mIsNeedCheck = p0.friendValidationStatus
                        mIsCanSearchByMobile = p0.findByMobileStatus
                        mView.showInitResult(p0.friendValidationStatus, p0.findByMobileStatus)
                        mView.hideInitView()
                    }

                    override fun handleException(p0: Throwable?, p1: String?, p2: String?) {
                        mView.closeCurrPage()
                    }

                })
    }

    override fun updateUserChangeData(isNeedCheck: Boolean, isCanSearchByMobile: Boolean) {

        if (mIsNeedCheck == isNeedCheck && mIsCanSearchByMobile == isCanSearchByMobile) {
            return
        }
        if (mDisposable != null && !mDisposable!!.isDisposed) {
            mDisposable!!.dispose()
        }
        mDisposable = PersonApi.updateTypeOfAddFriendByOther(isNeedCheck, isCanSearchByMobile)
                .map(RxUtil.handleResponse<UpdateTypeOfAddFriendByOtherResBean>())
                .subscribeWith(object : CommSubscriber<UpdateTypeOfAddFriendByOtherResBean>(mView) {

                    override fun handleResult(p0: UpdateTypeOfAddFriendByOtherResBean?) {
                        if (!isNeedCheck && !UserManager.getInstance(mContext).isLoginIM) {
                            EventBus.getDefault().post(CommBizEvent("MsgCenter_refresh_im_token_and_login", "刷新IMToken并登陆IM"));
                        }
                    }

                    override fun handleException(p0: Throwable?, p1: String?, p2: String?) {
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