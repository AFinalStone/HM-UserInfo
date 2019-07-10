package com.hm.iou.userinfo.business.view;

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import com.hm.iou.base.BaseActivity
import com.hm.iou.userinfo.R
import com.hm.iou.userinfo.business.SetTypeOfAddFriendByOtherContract
import com.hm.iou.userinfo.business.presenter.SetTypeOfAddFriendByOtherPresenter
import kotlinx.android.synthetic.main.person_activity_set_type_of_add_friend_by_other.*

class SetTypeOfAddFriendByOtherActivity : BaseActivity<SetTypeOfAddFriendByOtherPresenter>(), SetTypeOfAddFriendByOtherContract.View {


    override fun getLayoutId(): Int {
        return R.layout.person_activity_set_type_of_add_friend_by_other
    }

    override fun initPresenter(): SetTypeOfAddFriendByOtherPresenter {
        return SetTypeOfAddFriendByOtherPresenter(mContext, this)
    }

    override fun initEventAndData(p0: Bundle?) {
        view_close.setOnClickListener {
            onBackPressed()
        }
        mPresenter.init()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mPresenter.updateUserChangeData(cb_need_check.isChecked, switch_search.isChecked)
    }

    override fun showInitView() {
        loading.visibility = VISIBLE;
        loading.showDataLoading()
    }

    override fun hideInitView() {
        loading.stopLoadingAnim()
        loading.visibility = GONE
    }

    override fun showInitFailed(msg: String) {
        loading.visibility = VISIBLE
        loading.showDataFail(msg, { mPresenter.init() })
    }

    override fun showDataEmpty() {
        loading.visibility = VISIBLE
        loading.showDataEmpty("")
    }

    override fun showInitResult(isNeedCheck: Boolean, isCanSearchByMobile: Boolean) {
        if (isNeedCheck) {
            cb_need_check.isEnabled = false
            cb_need_check.setChecked(true, false)
            cb_not_need_check.setChecked(false, false)
            cb_not_need_check.isEnabled = true
        } else {
            cb_not_need_check.isEnabled = false
            cb_not_need_check.setChecked(true, false)
            cb_need_check.setChecked(false, false)
            cb_need_check.isEnabled = true
        }
        switch_search.isChecked = isCanSearchByMobile

        cb_need_check.setOnCheckedChangeListener { _, b ->
            if (b) {
                cb_need_check.isEnabled = false
                cb_not_need_check.isChecked = false
                cb_not_need_check.isEnabled = true
            }
        }
        cb_not_need_check.setOnCheckedChangeListener { _, b ->
            if (b) {
                cb_not_need_check.isEnabled = false
                cb_need_check.isChecked = false
                cb_need_check.isEnabled = true
            }
        }
    }


}
