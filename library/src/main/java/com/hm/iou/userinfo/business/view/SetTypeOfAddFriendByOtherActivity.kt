package com.hm.iou.userinfo.business.view;

import android.os.Bundle
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
        cb_need_check.setOnCheckedChangeListener { _, b ->
            if (b) {
                cb_not_need_check.isEnabled = true
                cb_not_need_check.isChecked = false
                cb_need_check.isEnabled = false
            }
        }
        cb_not_need_check.setOnCheckedChangeListener { _, b ->
            if (b) {
                cb_need_check.isEnabled = true
                cb_need_check.isChecked = false
                cb_not_need_check.isEnabled = false
            }
        }
        mPresenter.init()
    }

    override fun setIfCheckAddFriendByOther(isNeedCheck: Boolean) {
        cb_need_check.isChecked = isNeedCheck
        cb_not_need_check.isChecked = !isNeedCheck
    }

    override fun setIfCanSearchMeByMobile(isCan: Boolean) {
        switch_search.isChecked = isCan
    }


}
