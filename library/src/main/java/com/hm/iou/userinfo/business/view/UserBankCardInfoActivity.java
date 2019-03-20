package com.hm.iou.userinfo.business.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;
import com.hm.iou.userinfo.bean.UserBankCardInfoResBean;
import com.hm.iou.userinfo.business.UserBankCardInfoContract;
import com.hm.iou.userinfo.business.presenter.UserBankCardInfoPresenter;

import butterknife.BindView;

/**
 * 我的银行卡
 *
 * @author syl
 * @time 2019/3/19 7:46 PM
 */
public class UserBankCardInfoActivity extends BaseActivity<UserBankCardInfoPresenter> implements UserBankCardInfoContract.View {


    @BindView(R2.id.tv_bank_card_number)
    TextView mTvBankCardNumber;
    @BindView(R2.id.tv_bank_name)
    TextView mTvBankName;
    @BindView(R2.id.tv_bank_card_type)
    TextView mTvBankCardType;
    @BindView(R2.id.tv_bank_card_master)
    TextView mTvBankCardMaster;
    @BindView(R2.id.tv_bank_card_bind_mobile)
    TextView mTvBankCardBindMobile;
    @BindView(R2.id.tv_bank_card_bind_time)
    TextView mTvBankCardBindTime;

    @Override
    protected int getLayoutId() {
        return R.layout.person_activity_user_bank_card_info;
    }

    @Override
    protected UserBankCardInfoPresenter initPresenter() {
        return new UserBankCardInfoPresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
    }


    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getBankCardInfo();
    }

    @Override
    public void showBankCardInfo(UserBankCardInfoResBean infoBean) {
        if (infoBean == null) {
            return;
        }
        //银行卡号
        try {
            String bankCardNum = infoBean.getBankCardNo();
            StringBuffer sb = new StringBuffer();
            sb.append(bankCardNum.substring(0, 4));
            int numOfHide = bankCardNum.length() - 8;
            for (int i = 0; i < numOfHide; i++) {
                sb.append("*");
            }
            sb.append(bankCardNum.substring(bankCardNum.length() - 4, bankCardNum.length()));
            mTvBankCardNumber.setText(bankCardNum);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //银行名称
        String bankName = infoBean.getBankName();
        if (!TextUtils.isEmpty(bankName)) {
            mTvBankName.setText(bankName);
        }
        //卡片类型
        String bankCardType = infoBean.getBankType();
        if (!TextUtils.isEmpty(bankCardType)) {
            mTvBankCardType.setText(bankCardType);
        }
        //卡片归属
        String bankCardMaster = infoBean.getBankAscription();
        if (!TextUtils.isEmpty(bankCardMaster)) {
            mTvBankCardMaster.setText(bankCardMaster);
        }
        //绑定手机
        String bankCardBindMobile = infoBean.getMobile();
        if (!TextUtils.isEmpty(bankCardBindMobile)) {
            mTvBankCardBindMobile.setText(bankCardBindMobile);
        }
        //认证时间
        String bankCardBindTime = infoBean.getBindTime();
        if (!TextUtils.isEmpty(bankCardBindTime)) {
            mTvBankCardBindTime.setText(bankCardBindTime);
        }
    }
}
