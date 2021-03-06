package com.hm.iou.userinfo.business.view;

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hm.iou.base.BaseActivity
import com.hm.iou.router.Router
import com.hm.iou.tools.ImageLoader
import com.hm.iou.tools.StringUtil
import com.hm.iou.userinfo.R
import com.hm.iou.userinfo.bean.BlackNameBean
import com.hm.iou.userinfo.business.BlackNameListContract
import com.hm.iou.userinfo.business.presenter.BlackNameListPresenter
import kotlinx.android.synthetic.main.person_activity_black_name_list.*

class BlackNameListActivity : BaseActivity<BlackNameListPresenter>(), BlackNameListContract.View {


    var mAdapter: BlackNameAdapter = BlackNameAdapter()

    override fun getLayoutId(): Int {
        return R.layout.person_activity_black_name_list
    }

    override fun initPresenter(): BlackNameListPresenter {
        return BlackNameListPresenter(mContext, this)
    }

    override fun initEventAndData(p0: Bundle?) {
        overridePendingTransition(R.anim.uikit_activity_open_from_bottom, 0)
        view_close.setOnClickListener { onBackPressed() }
        mAdapter.setOnItemChildClickListener { _, view, position ->
            if (R.id.ll_content == view.id) {
                var bean: BlackNameBean? = mAdapter.getItem(position)
                if (bean != null) {
                    Router.getInstance()
                            .buildWithUrl("hmiou://m.54jietiao.com/message/firend_detail")
                            .withString("userId", bean.friendId)
                            .navigation(mContext)
                }

            }
        }
        rv_black_name.layoutManager = LinearLayoutManager(mContext)
        rv_black_name.adapter = mAdapter;
    }

    override fun onResume() {
        super.onResume()
        mPresenter.getBlackNameList()
    }

    //关闭Activity的切换动画
    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.uikit_activity_to_bottom)
    }

    override fun showBlackNameList(list: List<BlackNameBean>) {
        mAdapter.setNewData(list)
    }

    override fun showInitView() {
        loading.visibility = VISIBLE
        loading.showDataLoading()
    }

    override fun hideInitView() {
        loading.stopLoadingAnim()
        loading.visibility = GONE
    }

    override fun showInitFailed(msg: String) {
        loading.visibility = VISIBLE
        loading.showDataFail(msg, { mPresenter.getBlackNameList() })
    }

    override fun showDataEmpty() {
        loading.visibility = VISIBLE
        loading.showDataEmpty("")
    }

    class BlackNameAdapter : BaseQuickAdapter<BlackNameBean, BaseViewHolder>(R.layout.person_layout_black_name_list_item) {

        override fun convert(helper: BaseViewHolder, item: BlackNameBean) {
            helper.setText(R.id.tv_name, StringUtil.getUnnullString(item.nickName))
            var ivHeader: ImageView = helper.getView(R.id.iv_header)
            ImageLoader.getInstance(mContext).displayImage(item.avatarUrl, ivHeader, R.drawable.uikit_bg_pic_loading_place, R.mipmap.uikit_icon_header_unknow)
            helper.addOnClickListener(R.id.ll_content)
        }

    }
}
