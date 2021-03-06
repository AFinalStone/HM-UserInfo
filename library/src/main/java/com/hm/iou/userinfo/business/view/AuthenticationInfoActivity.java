package com.hm.iou.userinfo.business.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.sharedata.model.SexEnum;
import com.hm.iou.tools.ImageLoader;
import com.hm.iou.userinfo.NavigationHelper;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;
import com.hm.iou.userinfo.bean.UserAuthenticationInfoResBean;
import com.hm.iou.userinfo.business.AuthenticationInfoContract;
import com.hm.iou.userinfo.business.presenter.AuthenticationInfoPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author syl
 * @time 2019/3/19 7:46 PM
 */
public class AuthenticationInfoActivity extends BaseActivity<AuthenticationInfoPresenter> implements AuthenticationInfoContract.View {


    @BindView(R2.id.rl_header)
    RelativeLayout mRlHeader;
    @BindView(R2.id.iv_header)
    ImageView mIvHeader;
    @BindView(R2.id.tv_name)
    TextView mTvName;
    @BindView(R2.id.tv_id_card)
    TextView mTvIdCard;
    @BindView(R2.id.tv_id_card_valid_time)
    TextView mTvIdCardValidTime;
    @BindView(R2.id.tv_update_flag)
    TextView mTvUpdateFlag;
    @BindView(R2.id.tv_id_card_photo_desc)
    TextView mTvIdCardPhotoDesc;
    @BindView(R2.id.tv_is_adult_desc)
    TextView mTvIsAdultDesc;
    @BindView(R2.id.tv_handler_signature_desc)
    TextView mTvHandlerSignatureDesc;
    @BindView(R2.id.tv_authentication_time_desc)
    TextView mTvAuthenticationTimeDesc;
    @BindView(R2.id.iv_id_card_photo_flag)
    ImageView mIvIdCardPhotoFlag;
    @BindView(R2.id.iv_is_adult_flag)
    ImageView mIvIsAdultFlag;
    @BindView(R2.id.iv_handler_signature_flag)
    ImageView mIvHandlerSignatureFlag;

    private int mWarnColor;
    private int mWarnImage;

    private int mNormalColor;
    private int mNormalImage;

    @Override
    protected int getLayoutId() {
        return R.layout.person_activity_user_authentication_info;
    }

    @Override
    protected AuthenticationInfoPresenter initPresenter() {
        return new AuthenticationInfoPresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mWarnColor = getResources().getColor(R.color.uikit_function_exception);
        mWarnImage = R.mipmap.person_ic_authen_warn;
        mNormalColor = getResources().getColor(R.color.uikit_text_sub_content);
        mNormalImage = R.mipmap.person_ic_authen_right;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getAuthenticationInfo();
    }


    @OnClick(R2.id.tv_update_flag)
    public void onClick() {
        NavigationHelper.toUpdateAuthentication(mContext);
    }

    @Override
    public void showAuthenticationInfo(UserAuthenticationInfoResBean infoBean) {
        if (infoBean == null) {
            return;
        }
        //性别
        int sex = infoBean.getSex();
        if (sex == SexEnum.FEMALE.getValue()) {
            mRlHeader.setBackgroundResource(R.mipmap.person_ic_authen_header_bg_woman);
            mTvIdCardValidTime.setTextColor(0xff75605c);
        } else {
            mRlHeader.setBackgroundResource(R.mipmap.person_ic_authen_header_bg_man);
            mTvIdCardValidTime.setTextColor(0xff495161);
        }
        //头像
        String headerUrl = infoBean.getAvatarUrl();
        if (!TextUtils.isEmpty(headerUrl)) {
            ImageLoader.getInstance(mContext).displayImage(headerUrl, mIvHeader, R.mipmap.uikit_icon_header_unknow, R.mipmap.uikit_icon_header_unknow);
        }

        //姓名
        String name = infoBean.getRealName();
        mTvName.setText(name);
        //身份证编号
        try {
            String idCard = infoBean.getIdCardNum();
            String idStart = idCard.substring(0, 3);
            String idEnd = idCard.substring(idCard.length() - 2, idCard.length());
            idCard = idStart + "*************" + idEnd;
            mTvIdCard.setText(idCard);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //有效期
        try {
            String startTime = infoBean.getIdCardStartTime().replace("-", ".");
            String endTime = infoBean.getIdCardEndTime().replace("-", ".");
            String validTime = "有效期：" + startTime + " - " + endTime;
            mTvIdCardValidTime.setText(validTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //是否需要更新,证件照片是否过期
        if (!infoBean.isIdCardPhoto()) {
            mTvIdCardPhotoDesc.setText("已过期");
            mTvIdCardPhotoDesc.setTextColor(mWarnColor);
            mIvIdCardPhotoFlag.setImageResource(mWarnImage);
        } else {
            mTvIdCardPhotoDesc.setText("已上传");
            mTvIdCardPhotoDesc.setTextColor(mNormalColor);
            mIvIdCardPhotoFlag.setImageResource(mNormalImage);
        }

        if (!infoBean.isOverDue()) {
            mTvUpdateFlag.setVisibility(View.VISIBLE);
        } else {
            mTvUpdateFlag.setVisibility(View.GONE);
        }

        //年满18周岁
        if (!infoBean.isUnderAge()) {
            mTvIsAdultDesc.setTextColor(mWarnColor);
            mTvIsAdultDesc.setText("未满足");
            mIvIsAdultFlag.setImageResource(mWarnImage);
        } else {
            mTvIsAdultDesc.setTextColor(mNormalColor);
            mTvIsAdultDesc.setText("已满足");
            mIvIsAdultFlag.setImageResource(mNormalImage);
        }

        //手写签名
        if (!infoBean.isWriteSign()) {
            mTvHandlerSignatureDesc.setText("未录入");
            mTvHandlerSignatureDesc.setTextColor(mWarnColor);
            mIvHandlerSignatureFlag.setImageResource(mWarnImage);
        } else {
            mTvHandlerSignatureDesc.setText("已录入");
            mTvHandlerSignatureDesc.setTextColor(mNormalColor);
            mIvHandlerSignatureFlag.setImageResource(mNormalImage);
        }

        //认证时间
        String authenTime = infoBean.getAttestTime();
        if (!TextUtils.isEmpty(authenTime)) {
            authenTime = authenTime.replaceAll("-", ".");
            mTvAuthenticationTimeDesc.setText(authenTime);
        }
    }

}
