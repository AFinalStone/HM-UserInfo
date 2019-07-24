package com.hm.iou.userinfo.business.view

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hm.iou.base.BaseActivity
import com.hm.iou.tools.ImageLoader
import com.hm.iou.tools.kt.clickWithDuration
import com.hm.iou.userinfo.R
import com.hm.iou.userinfo.business.VipStatusContract
import com.hm.iou.userinfo.business.presenter.VipStatusPresenter
import kotlinx.android.synthetic.main.person_activity_vip_status.*


class VipStatusActivity : BaseActivity<VipStatusPresenter>(), VipStatusContract.View {

    companion object {
        val REQ_CODE_TO_PAY_VIP_PAGE = 100
    }


    private val mAdapter: VipCouponAdapter = VipCouponAdapter()

    override fun getLayoutId(): Int = R.layout.person_activity_vip_status

    override fun initPresenter(): VipStatusPresenter = VipStatusPresenter(mContext, this)

    override fun initEventAndData(p0: Bundle?) {
        mAdapter.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
            if (R.id.tv_status == view.id) {
                val item: VipICouponItem? = mAdapter.getItem(position)
                item?.let {
                    mPresenter.getCoupon(item.getCouponId())
                }
            }
        }
        mPresenter.init()
    }

    override fun showHeaderInfo(headerUrl: String?, defaultAvatarResId: Int) {
        ImageLoader.getInstance(mContext).displayImage(headerUrl, iv_header, defaultAvatarResId, defaultAvatarResId)
    }

    override fun showNoVipUserInfoView(remindDay: Int?, listModule: List<VipIHeaderModuleItem>?, listCoupon: List<VipICouponItem>?) {
        rl_content.visibility = VISIBLE
        iv_vip_open_vip.clickWithDuration {
            mPresenter.getPayInfo()
        }
        iv_header_flag.setImageResource(R.mipmap.persion_user_flag_not_vip)
        rl_header_bg.setBackgroundResource(R.mipmap.person_bg_vip_not)

        //优惠券列表
        rv_coupon_list.layoutManager = GridLayoutManager(mContext, 2)
        rv_coupon_list.adapter = mAdapter
        //优惠券列表头部View
        val viewHeader = LayoutInflater.from(mContext).inflate(R.layout.person_layout_user_vip_info_header, null)
        remindDay?.let {
            viewHeader.findViewById<TextView>(R.id.tv_coupon_valid_date).visibility = VISIBLE
            viewHeader.findViewById<TextView>(R.id.tv_coupon_valid_date).text = "（剩余$remindDay 天）"
        }
        listCoupon?.let {
            if (listCoupon.isNotEmpty()) {
                viewHeader.findViewById<TextView>(R.id.tv_coupon_count).visibility = VISIBLE
                viewHeader.findViewById<TextView>(R.id.tv_coupon_count).text = "${listCoupon.size}张签章金券"
            }
        }
        viewHeader.findViewById<RecyclerView>(R.id.rv_model).layoutManager = GridLayoutManager(mContext, 3)
        val adapter = VipHeaderModuleAdapter()
        viewHeader.findViewById<RecyclerView>(R.id.rv_model).adapter = adapter
        adapter.setNewData(listModule)
        mAdapter.removeAllHeaderView()
        mAdapter.addHeaderView(viewHeader)
        mAdapter.setNewData(listCoupon)
    }

    override fun showVipUserInfoView(remindDay: Int?, vipValidDate: String?, listModule: List<VipIHeaderModuleItem>?, listCoupon: List<VipICouponItem>?) {
        rl_content.visibility = VISIBLE
        iv_vip_open_vip.visibility = GONE
        iv_header_flag.setImageResource(R.mipmap.persion_user_flag_vip)
        rl_header_bg.setBackgroundResource(R.mipmap.person_bg_vip)
        //优惠券列表
        rv_coupon_list.layoutManager = GridLayoutManager(mContext, 2)
        rv_coupon_list.adapter = mAdapter
        //优惠券列表头部View
        val viewHeader = LayoutInflater.from(mContext).inflate(R.layout.person_layout_user_vip_info_header, null)
        remindDay?.let {
            viewHeader.findViewById<TextView>(R.id.tv_coupon_valid_date).visibility = VISIBLE
            viewHeader.findViewById<TextView>(R.id.tv_coupon_valid_date).text = "（剩余$remindDay 天）"
        }
        listCoupon?.let {
            if (listCoupon.isNotEmpty()) {
                viewHeader.findViewById<TextView>(R.id.tv_coupon_count).visibility = VISIBLE
                viewHeader.findViewById<TextView>(R.id.tv_coupon_count).text = "${listCoupon.size}张签章金券"
            }
        }
        viewHeader.findViewById<RecyclerView>(R.id.rv_model).layoutManager = GridLayoutManager(mContext, 3)
        val adapter = VipHeaderModuleAdapter()
        viewHeader.findViewById<RecyclerView>(R.id.rv_model).adapter = adapter
        adapter.setNewData(listModule)
        //优惠券列表底部View
        val viewFooter = LayoutInflater.from(mContext).inflate(R.layout.person_layout_user_vip_info_vip_footer, null)

        mAdapter.removeAllHeaderView()
        mAdapter.removeAllFooterView()
        mAdapter.addHeaderView(viewHeader)
        mAdapter.addFooterView(viewFooter)
        mAdapter.setNewData(listCoupon)
    }

}