package com.hm.iou.userinfo.business.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.uikit.HMTopBarView;
import com.hm.iou.userinfo.NavigationHelper;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;
import com.hm.iou.userinfo.api.PersonApi;
import com.hm.iou.userinfo.bean.UserEmailInfoResBean;
import com.hm.iou.userinfo.business.UserEmailInfoContract;
import com.hm.iou.userinfo.business.presenter.UserEmailInfoPresenter;

import butterknife.BindView;

/**
 * 我的邮箱
 *
 * @author syl
 * @time 2019/3/19 7:46 PM
 */
public class UserEmailInfoActivity extends BaseActivity<UserEmailInfoPresenter> implements UserEmailInfoContract.View {


    @BindView(R2.id.topBar)
    HMTopBarView mTopBar;
    @BindView(R2.id.tv_email_number)
    TextView mTvEmailNumber;
    @BindView(R2.id.tv_find_login_psd_desc)
    TextView mTvFindLoginPsdDesc;
    @BindView(R2.id.iv_back_up_receipt_flag)
    ImageView mIvBackUpReceiptFlag;
    @BindView(R2.id.tv_back_up_receipt_desc)
    TextView mTvBackUpReceiptDesc;
    @BindView(R2.id.tv_sign_default_contact_way)
    TextView mTvSignDefaultContactWay;
    @BindView(R2.id.tv_bind_email_time_desc)
    TextView mTvBindEmailTimeDesc;

    @Override
    protected int getLayoutId() {
        return R.layout.person_activity_user_email_info;
    }

    @Override
    protected UserEmailInfoPresenter initPresenter() {
        return new UserEmailInfoPresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mTopBar.setOnMenuClickListener(new HMTopBarView.OnTopBarMenuClickListener() {
            @Override
            public void onClickTextMenu() {
                NavigationHelper.toModifyEmailPage(mContext);
            }

            @Override
            public void onClickImageMenu() {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getUserEmailInfo();
    }

    @Override
    public void showBankCardInfo(UserEmailInfoResBean infoBean) {
        if (infoBean == null) {
            return;
        }
        //邮箱
        try {
            StringBuffer sb = new StringBuffer();
            String emailNum = infoBean.getReceiverEmail();
            sb.append(emailNum.substring(0, 2));
            int index = emailNum.indexOf("@");
            int numOfHide = index - 2;
            for (int i = 0; i < numOfHide; i++) {
                sb.append("*");
            }
            sb.append(emailNum.substring(index, emailNum.length()));
            mTvEmailNumber.setText(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //认证时间
        String bindEmailTime = infoBean.getAttestTime();
        if (!TextUtils.isEmpty(bindEmailTime)) {
            mTvBindEmailTimeDesc.setText(bindEmailTime);
        }
    }
}
