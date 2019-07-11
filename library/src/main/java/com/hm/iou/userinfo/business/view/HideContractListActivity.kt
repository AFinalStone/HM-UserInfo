package com.hm.iou.userinfo.business.view;

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View.GONE
import android.view.View.VISIBLE
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hm.iou.base.BaseActivity
import com.hm.iou.tools.ClipUtil
import com.hm.iou.userinfo.R
import com.hm.iou.userinfo.bean.HideContractBean
import com.hm.iou.userinfo.business.HideContractListContract
import com.hm.iou.userinfo.business.presenter.HideContractListPresenter
import kotlinx.android.synthetic.main.person_activity_hide_contract_list.*

class HideContractListActivity : BaseActivity<HideContractListPresenter>(), HideContractListContract.View {

    private var mAdapter: HideContractAdapter = HideContractAdapter()

    override fun getLayoutId(): Int {
        return R.layout.person_activity_hide_contract_list
    }

    override fun initPresenter(): HideContractListPresenter {
        return HideContractListPresenter(mContext, this)
    }

    override fun initEventAndData(p0: Bundle?) {
        overridePendingTransition(R.anim.uikit_activity_open_from_bottom, 0)
        view_close.setOnClickListener { onBackPressed() }
        mAdapter.setOnItemChildClickListener { _, view, position
            ->
            if (R.id.btn_copy == view.id) {
                var bean: HideContractBean? = mAdapter.getItem(position)
                if (bean != null) {
                    ClipUtil.getInstance(mContext).putTextIntoClip(bean.justiceId)
                    toastMessage("复制编号，“粘贴”到“搜索”收录")
                }

            }
        }
        rv_hide_contract.layoutManager = LinearLayoutManager(mContext)
        rv_hide_contract.adapter = mAdapter;
        mPresenter.getHideContractList()
    }

    //关闭Activity的切换动画
    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.uikit_activity_to_bottom)
    }

    override fun showHideContractList(list: List<HideContractBean>) {
        mAdapter.setNewData(list)
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
        loading.showDataFail(msg, { mPresenter.getHideContractList() })
    }

    override fun showDataEmpty() {
        loading.visibility = VISIBLE
        loading.showDataEmpty("")
    }


    class HideContractAdapter : BaseQuickAdapter<HideContractBean, BaseViewHolder>(R.layout.person_layout_hide_contract_list_item) {

        override fun convert(helper: BaseViewHolder, item: HideContractBean) {
            helper.setText(R.id.tv_money, item.title)
            helper.setText(R.id.tv_contract_id, "编号：" + item.justiceId)
            helper.setText(R.id.tv_hide_time, "隐藏：" + item.hideDate)
            helper.addOnClickListener(R.id.btn_copy)
        }

    }
}
