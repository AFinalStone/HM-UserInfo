package com.hm.iou.userinfo.business.view

import android.os.Bundle
import com.hm.iou.base.BaseActivity
import com.hm.iou.userinfo.R
import com.hm.iou.userinfo.business.VipStatusContract
import com.hm.iou.userinfo.business.presenter.VipStatusPresenter

/**
 * Created by syl on 2019/7/23.
 */
class VipStatusActivity : BaseActivity<VipStatusPresenter>(), VipStatusContract.View {

    override fun getLayoutId(): Int = R.layout.person_activity_vip_status

    override fun initEventAndData(p0: Bundle?) {
        mPresenter.init()
    }

    override fun initPresenter(): VipStatusPresenter = VipStatusPresenter(mContext, this)


    override fun showCommUserInfoView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showVipUserInfoView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showAvatar(url: String?, defIconResId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showDataLoading(show: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showDataLoadError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showVipValidDate(validDate: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}