package com.hm.iou.userinfo.business.view;

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import com.hm.iou.base.BaseActivity
import com.hm.iou.userinfo.NavigationHelper
import com.hm.iou.userinfo.R
import com.hm.iou.userinfo.business.MoreSettingContract
import com.hm.iou.userinfo.business.presenter.LogoutUtil
import com.hm.iou.userinfo.business.presenter.MoreSettingPresenter
import kotlinx.android.synthetic.main.person_activity_more_setting.*

class MoreSettingActivity : BaseActivity<MoreSettingPresenter>(), MoreSettingContract.View {

    override fun getLayoutId(): Int {
        return R.layout.person_activity_more_setting
    }

    override fun initPresenter(): MoreSettingPresenter {
        return MoreSettingPresenter(mContext, this)
    }

    override fun initEventAndData(p0: Bundle?) {
        ll_set_type_of_add_friend_by_other.setOnClickListener {
            NavigationHelper.toSetAddFriendByOtherType(mContext);
        }
        ll_black_name.setOnClickListener {
            NavigationHelper.toBlackNamePage(mContext)
        }
        ll_hide_contract.setOnClickListener {
            NavigationHelper.toHideContractPage(mContext)
        }
        ll_more_exit.setOnClickListener {
            LogoutUtil.showLogoutConfirmDialog(mContext, this)
        }
        mPresenter.getBlackNameAndHideContract()
    }

    override fun setBlackNameItemVisible(isShow: Boolean) {
        if (isShow) {
            view_divider_black_name.visibility = VISIBLE
            ll_black_name.visibility = VISIBLE
        } else {
            view_divider_black_name.visibility = GONE
            ll_black_name.visibility = GONE
        }
    }

    override fun setHideContractItemVisible(isShow: Boolean) {
        if (isShow) {
            view_divider_hide_contract.visibility = VISIBLE
            ll_hide_contract.visibility = VISIBLE
        } else {
            view_divider_hide_contract.visibility = GONE
            ll_hide_contract.visibility = GONE
        }
    }
}
