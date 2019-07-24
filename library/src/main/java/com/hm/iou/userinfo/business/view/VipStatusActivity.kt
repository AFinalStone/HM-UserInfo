package com.hm.iou.userinfo.business.view

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View.VISIBLE
import com.hm.iou.base.BaseActivity
import com.hm.iou.tools.ImageLoader
import com.hm.iou.userinfo.R
import com.hm.iou.userinfo.business.VipStatusContract
import com.hm.iou.userinfo.business.presenter.VipStatusPresenter
import kotlinx.android.synthetic.main.person_activity_vip_status.*


class VipStatusActivity : BaseActivity<VipStatusPresenter>(), VipStatusContract.View {

    private val mAdapter: VipCouponAdapter = VipCouponAdapter()

    override fun getLayoutId(): Int = R.layout.person_activity_vip_status

    override fun initPresenter(): VipStatusPresenter = VipStatusPresenter(mContext, this)

    override fun initEventAndData(p0: Bundle?) {
        mPresenter.init()
    }

    override fun showNoVipUserInfoView(headerUrl: String?, defaultAvatarResId: Int, listModule: List<VipIHeaderModuleItem>?, listCoupon: List<VipICouponItem>?) {
        recyclerView.visibility = VISIBLE
        recyclerView.layoutManager = GridLayoutManager(mContext, 2)
        recyclerView.adapter = mAdapter
        //头部View
        val viewHeader = LayoutInflater.from(mContext).inflate(R.layout.person_layout_user_no_vip_info_header, null)
        ImageLoader.getInstance(mContext).displayImage(headerUrl, viewHeader.findViewById(R.id.iv_header), defaultAvatarResId, defaultAvatarResId)
        viewHeader.findViewById<RecyclerView>(R.id.rv_model).layoutManager = GridLayoutManager(mContext, 3)
        val adapter = VipHeaderModuleAdapter()
        viewHeader.findViewById<RecyclerView>(R.id.rv_model).adapter = adapter
        adapter.setNewData(listModule)
        mAdapter.addHeaderView(viewHeader)
        //底部View
        val viewFooter = LayoutInflater.from(mContext).inflate(R.layout.person_layout_user_no_vip_info_footer, null)
        mAdapter.addFooterView(viewFooter)
        mAdapter.setNewData(listCoupon)
    }

    override fun showVipUserInfoView(headerUrl: String?, defaultAvatarResId: Int, vipValidDate: String?, listModule: List<VipIHeaderModuleItem>?, listCoupon: List<VipICouponItem>?) {
        recyclerView.visibility = VISIBLE
        recyclerView.layoutManager = GridLayoutManager(mContext, 2)
        recyclerView.adapter = mAdapter
        //头部View
        val viewHeader = LayoutInflater.from(mContext).inflate(R.layout.person_layout_user_vip_info_header, null)
        ImageLoader.getInstance(mContext).displayImage(headerUrl, viewHeader.findViewById(R.id.iv_header), defaultAvatarResId, defaultAvatarResId)
        val adapter = VipHeaderModuleAdapter()
        viewHeader.findViewById<RecyclerView>(R.id.rv_model).adapter = adapter
        adapter.setNewData(listModule)
        mAdapter.addHeaderView(viewHeader)
        //底部View
        val viewFooter = LayoutInflater.from(mContext).inflate(R.layout.person_layout_user_vip_info_footer, null)
        mAdapter.addHeaderView(viewHeader)
        mAdapter.addFooterView(viewFooter)
        mAdapter.setNewData(listCoupon)
    }


}