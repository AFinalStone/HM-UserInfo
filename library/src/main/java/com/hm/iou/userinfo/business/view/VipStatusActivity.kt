package com.hm.iou.userinfo.business.view

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hm.iou.base.BaseActivity
import com.hm.iou.router.Router
import com.hm.iou.tools.ImageLoader
import com.hm.iou.tools.kt.clickWithDuration
import com.hm.iou.userinfo.R
import com.hm.iou.userinfo.business.VipStatusContract
import com.hm.iou.userinfo.business.presenter.VipStatusPresenter
import com.hm.iou.userinfo.dict.CouPinStatusType
import kotlinx.android.synthetic.main.person_activity_vip_status.*


class VipStatusActivity : BaseActivity<VipStatusPresenter>(), VipStatusContract.View {

    companion object {
        val REQ_CODE_TO_PAY_VIP_PAGE = 100
    }


    private val mAdapter: VipCouponAdapter = VipCouponAdapter()

    override fun getLayoutId(): Int = R.layout.person_activity_vip_status

    override fun initPresenter(): VipStatusPresenter = VipStatusPresenter(mContext, this)

    override fun initEventAndData(p0: Bundle?) {
        mAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
            val item: VipICouponItem? = mAdapter.getItem(position)
            item?.let {
                if (CouPinStatusType.TO_EXPENSE == item.getCouponStatus()) {
                    Router.getInstance()
                            .buildWithUrl("hmiou://m.54jietiao.com/iou/draftlist?if_create=true")
                            .navigation()
                } else if (CouPinStatusType.HAVE_USE == item.getCouponStatus()) {
                } else {
                    mPresenter.getCoupon(item.getCouponId(), position)
                }
            }
        }
        mPresenter.init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE_TO_PAY_VIP_PAGE) {
            if (resultCode == Activity.RESULT_OK) {
                mPresenter.init()
            }
        }
    }

    override fun showNoVipUserInfoView(headerUrl: String?, defaultAvatarResId: Int, remindDay: Int?, listModule: List<VipIHeaderModuleItem>?, listCoupon: List<VipICouponItem>?) {
        rl_content.visibility = VISIBLE
        iv_vip_open_vip.clickWithDuration {
            mPresenter.getPayInfo()
        }

        //优惠券列表
        rv_coupon_list.layoutManager = GridLayoutManager(mContext, 2)
        rv_coupon_list.adapter = mAdapter
        //优惠券列表头部View
        val viewHeader = LayoutInflater.from(mContext).inflate(R.layout.person_layout_user_vip_info_header, null)

        rv_coupon_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val distance = viewHeader.findViewById<LinearLayout>(R.id.ll_header_bg).height - rv_coupon_list.computeVerticalScrollOffset()
                if (distance > 0) {
                    topBar.setBackgroundColor(Color.TRANSPARENT)
                } else {
                    topBar.setBackgroundColor(Color.WHITE)
                }
            }
        })
        //头像
        val ivHeader: ImageView = viewHeader.findViewById(R.id.iv_header)
        ImageLoader.getInstance(mContext).displayImage(headerUrl, ivHeader, defaultAvatarResId, defaultAvatarResId)
        viewHeader.findViewById<LinearLayout>(R.id.ll_header_bg).setBackgroundResource(R.mipmap.person_bg_vip_not)
        viewHeader.findViewById<ImageView>(R.id.iv_header_flag).setImageResource(R.mipmap.persion_user_flag_not_vip)
        //剩余天数
        viewHeader.findViewById<TextView>(R.id.tv_coupon_valid_date).visibility = INVISIBLE
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

    override fun showVipUserInfoView(headerUrl: String?, defaultAvatarResId: Int, remindDay: Int?, vipValidDate: String?, listModule: List<VipIHeaderModuleItem>?, listCoupon: List<VipICouponItem>?) {
        rl_content.visibility = VISIBLE
        iv_vip_open_vip.visibility = GONE
        //优惠券列表
        rv_coupon_list.layoutManager = GridLayoutManager(mContext, 2)
        rv_coupon_list.adapter = mAdapter
        //优惠券列表头部View
        val viewHeader = LayoutInflater.from(mContext).inflate(R.layout.person_layout_user_vip_info_header, null)
        rv_coupon_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val distance = viewHeader.findViewById<LinearLayout>(R.id.ll_header_bg).height - rv_coupon_list.computeVerticalScrollOffset()
                if (distance > 0) {
                    topBar.setBackgroundColor(Color.TRANSPARENT)
                } else {
                    topBar.setBackgroundColor(Color.WHITE)
                }
            }
        })
        //头像
        val ivHeader: ImageView = viewHeader.findViewById(R.id.iv_header)
        ImageLoader.getInstance(mContext).displayImage(headerUrl, ivHeader, defaultAvatarResId, defaultAvatarResId)
        viewHeader.findViewById<LinearLayout>(R.id.ll_header_bg).setBackgroundResource(R.mipmap.person_bg_vip)
        viewHeader.findViewById<ImageView>(R.id.iv_header_flag).setImageResource(R.mipmap.persion_user_flag_vip)
        //VIP有效期
        viewHeader.findViewById<LinearLayout>(R.id.ll_vip_valid_date).visibility = VISIBLE
        viewHeader.findViewById<TextView>(R.id.tv_vip_valid_date).text = vipValidDate

        //剩余天数
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

    override fun updateCouPonItem(position: Int) {
        val item: VipICouponItem? = mAdapter.getItem(position)
        item?.let {
            item.setCouponStatus(CouPinStatusType.TO_EXPENSE)
            mAdapter.notifyItemChanged(position + mAdapter.headerLayoutCount)
        }
    }
}