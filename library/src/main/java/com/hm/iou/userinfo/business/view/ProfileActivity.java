package com.hm.iou.userinfo.business.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.logger.Logger;
import com.hm.iou.router.Router;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.tools.ImageLoader;
import com.hm.iou.tools.SystemUtil;
import com.hm.iou.uikit.dialog.IOSActionSheetItem;
import com.hm.iou.uikit.dialog.IOSActionSheetTitleDialog;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;
import com.hm.iou.userinfo.business.ProfileContract;
import com.hm.iou.userinfo.business.presenter.ProfilePresenter;
import com.hm.iou.userinfo.business.presenter.UserDataUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hjy on 2018/7/3.
 */

public class ProfileActivity extends BaseActivity<ProfilePresenter> implements ProfileContract.View {

    private static final int REQ_SELECT_CITY = 100;

    @BindView(R2.id.pb_profile_progress)
    ProgressBar mPbProfile;
    @BindView(R2.id.tv_profile_progress)
    TextView mTvProgress;
    @BindView(R2.id.iv_profile_avatar)
    ImageView mIvAvatar;
    @BindView(R2.id.tv_profile_nickname)
    TextView mTvNickname;
    @BindView(R2.id.tv_profile_realname)
    TextView mTvRealName;
    @BindView(R2.id.iv_profile_auth)
    ImageView mIvAuth;
    @BindView(R2.id.ll_profile_realname)
    View mLayoutRealName;
    @BindView(R2.id.iv_profile_auth_arrow)
    ImageView mIvAuthArrow;
    @BindView(R2.id.tv_profile_mobile)
    TextView mTvMobile;
    @BindView(R2.id.tv_profile_weixin)
    TextView mTvWeixin;
    @BindView(R2.id.tv_profile_email)
    TextView mTvEmail;
    @BindView(R2.id.ll_profile_email)
    View mLayoutEmail;
    @BindView(R2.id.tv_profile_city)
    TextView mTvCity;
    @BindView(R2.id.tv_profile_income)
    TextView mTvIncome;

    @Override
    protected int getLayoutId() {
        return R.layout.person_activity_profile;
    }

    @Override
    protected ProfilePresenter initPresenter() {
        return new ProfilePresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mPresenter.getUserProfile();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQ_SELECT_CITY == requestCode) {
            if (resultCode == RESULT_OK && data != null) {
                //获取城市编码
                String cityCode = data.getStringExtra("select_city_code");
                //获取城市名称
                String cityName = data.getStringExtra("select_city_name");
                Logger.d("城市编码" + cityCode + "城市名称" + cityName);
                mPresenter.updateLocation(cityName);
            }
        }
    }

    @OnClick(value = {R2.id.ll_profile_avatar, R2.id.ll_profile_nickname, R2.id.ll_profile_realname,
        R2.id.ll_profile_mobile, R2.id.ll_profile_weixin, R2.id.ll_profile_email, R2.id.ll_profile_city,
        R2.id.ll_profile_income, R2.id.tv_profile_logout})
    void onClick(View v) {
        if (v.getId() == R.id.ll_profile_avatar) {
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/user_avatar")
                    .navigation(this);
        } else if (v.getId() == R.id.ll_profile_nickname) {
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/modify_nickname_sex")
                    .navigation(this);
        } else if (v.getId() == R.id.ll_profile_realname) {
            Router.getInstance()
                    .buildWithUrl("hmiou://m.54jietiao.com/facecheck/authentication")
                    .navigation(this);
        } else if (v.getId() == R.id.ll_profile_mobile) {
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/change_mobile")
                    .navigation(mContext);
        } else if (v.getId() == R.id.ll_profile_weixin) {
            int type = UserManager.getInstance(mContext).getUserInfo().getType();
            boolean isWeixinBound = UserDataUtil.isSubClass(type) ? false : true;
            if (isWeixinBound) {
                Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/unbind_weixin")
                        .navigation(mContext);
            } else {
                boolean flag = SystemUtil.isAppInstalled(mContext, "com.tencent.mm");
                if (flag) {
                    mPresenter.toBindWeixin();
                } else {
                    toastMessage("当前手机没有安装微信");
                }
            }
        } else if (v.getId() == R.id.ll_profile_email) {
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/change_email")
                    .navigation(mContext);
        } else if (v.getId() == R.id.ll_profile_city) {
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/city/index")
                    .navigation(this, REQ_SELECT_CITY);
        } else if (v.getId() == R.id.ll_profile_income) {
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/my_income")
                    .navigation(this);
        } else if (v.getId() == R.id.tv_profile_logout) {
            showDialogLogoutSafely();
        }
    }

    @Override
    public void showProfileProgress(int progress) {
        mPbProfile.setProgress(progress);
    }

    @Override
    public void showProgressTips(String progressTxt) {
        mTvProgress.setText(progressTxt);
    }

    @Override
    public void showAvatar(String avatarUrl, int defaultAvatarResId) {
        if (TextUtils.isEmpty(avatarUrl)) {
            mIvAvatar.setImageResource(defaultAvatarResId);
            return;
        }
        ImageLoader.getInstance(this).displayImage(avatarUrl, mIvAvatar, defaultAvatarResId, defaultAvatarResId);
    }

    @Override
    public void showNickname(String nickname) {
        mTvNickname.setText(nickname);
    }

    @Override
    public void showRealName(String realName, int textColor) {
        mTvRealName.setText(realName);
        mTvRealName.setTextColor(textColor);
    }

    @Override
    public void showRealNameFlag(int visibility) {
        mIvAuth.setVisibility(visibility);
    }

    @Override
    public void enableRealNameClick(boolean enable) {
        mLayoutRealName.setEnabled(enable);
        if (enable) {
            mLayoutRealName.setBackgroundResource(R.drawable.uikit_bg_item_ripple);
            mIvAuthArrow.setVisibility(View.VISIBLE);
        } else {
            mLayoutRealName.setBackgroundColor(Color.WHITE);
            mIvAuthArrow.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showMobile(String mobile) {
        mTvMobile.setText(mobile);
    }

    @Override
    public void showWeixin(String weixin, int textColor) {
        mTvWeixin.setText(weixin);
        mTvWeixin.setTextColor(textColor);
    }

    @Override
    public void showEmail(int visibility, String email) {
        mLayoutEmail.setVisibility(visibility);
        mTvEmail.setText(email);
    }

    @Override
    public void showCity(String city) {
        mTvCity.setText(city);
    }

    @Override
    public void showMainIncome(String income) {
        mTvIncome.setText(income);
    }

    /**
     * 显示安全退出对话框
     */
    private void showDialogLogoutSafely() {
        new IOSActionSheetTitleDialog.Builder(mContext)
                .setTitle("是否退出当前账号？")
                .setTitleTextColor(getResources().getColor(R.color.iosActionSheet_gray))
                .addSheetItem(IOSActionSheetItem
                        .create("安全退出")
                        .setItemClickListener(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                mPresenter.logout();
                            }
                        })).show();
    }

}
