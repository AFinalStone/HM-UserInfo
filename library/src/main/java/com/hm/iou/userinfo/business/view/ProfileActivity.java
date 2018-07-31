package com.hm.iou.userinfo.business.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.logger.Logger;
import com.hm.iou.router.Router;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.sharedata.model.UserThirdPlatformInfo;
import com.hm.iou.tools.ImageLoader;
import com.hm.iou.tools.SystemUtil;
import com.hm.iou.uikit.dialog.IOSActionSheetItem;
import com.hm.iou.uikit.dialog.IOSActionSheetTitleDialog;
import com.hm.iou.uikit.dialog.IOSAlertDialog;
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

    @BindView(R2.id.ll_header)
    LinearLayout mLl_header;
    @BindView(R2.id.tv_topProgress)
    TextView mTvTopProgress;
    @BindView(R2.id.iv_topAd)
    ImageView mIvTopAd;
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
    @BindView(R2.id.iv_profile_sex)
    ImageView mIvSex;
    @BindView(R2.id.tv_profile_bind_bank)
    TextView mTvBindBank;
    @BindView(R2.id.iv_profile_bind_bank)
    ImageView mIvBindBank;
    @BindView(R2.id.ll_profile_realname)
    View mLayoutRealName;
    @BindView(R2.id.iv_profile_auth_arrow)
    ImageView mIvSexArrow;
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

    private IOSActionSheetTitleDialog mChangePwdDialog;
    PersonalDialogHelper mPersonalDialogHelper;

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
        mPersonalDialogHelper = new PersonalDialogHelper(mContext);
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

    @OnClick(value = {R2.id.iv_topAd, R2.id.ll_profile_avatar, R2.id.ll_profile_nickname, R2.id.ll_profile_realname,
            R2.id.ll_profile_bind_bank, R2.id.ll_profile_mobile, R2.id.ll_profile_weixin, R2.id.ll_profile_email
            , R2.id.ll_profile_city, R2.id.ll_profile_income, R2.id.tv_profile_logout, R2.id.ll_profile_changepwd})
    void onClick(View v) {
        if (v.getId() == R.id.iv_topAd) {
            toBindBank();
        } else if (v.getId() == R.id.ll_profile_avatar) {
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/user_avatar")
                    .navigation(this);
        } else if (v.getId() == R.id.ll_profile_nickname) {
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/modify_nickname_sex")
                    .navigation(this);
        } else if (v.getId() == R.id.ll_profile_realname) {
            UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
            int customerType = userInfo.getType();
            if (UserDataUtil.isCClass(customerType)) {
                Router.getInstance()
                        .buildWithUrl("hmiou://m.54jietiao.com/facecheck/authentication")
                        .navigation(mContext);
            } else {
                mPersonalDialogHelper.showHaveAuthtication();
            }
        } else if (v.getId() == R.id.ll_profile_bind_bank) {
            toBindBank();
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
        } else if (v.getId() == R.id.ll_profile_changepwd) {        //修改密码
            showChangePasswordMenu();
        }
    }

    @Override
    public void hideTopAd() {
        mIvTopAd.setVisibility(View.GONE);
    }

    @Override
    public void setHeaderVisible(int visible) {
        mLl_header.setVisibility(visible);
    }

    @Override
    public void showProfileProgress(int progress) {
        mTvTopProgress.setText(progress + "%");
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
    public void showSex(int idRes) {
        mIvSex.setVisibility(View.VISIBLE);
        mIvSex.setImageResource(idRes);
    }

    @Override
    public void showBindBank(String text, int textColor) {
        mTvBindBank.setText(text);
        mTvBindBank.setTextColor(textColor);
    }

    @Override
    public void showBindBankFlag() {
        mIvBindBank.setVisibility(View.VISIBLE);
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

    private void toBindBank() {
        UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        int customerType = userInfo.getType();
        if (UserDataUtil.isCClass(customerType)) {
            mPersonalDialogHelper.showBinkBankNeedAuthen();
        } else {
            UserThirdPlatformInfo userThirdPlatformInfo = UserManager.getInstance(mContext).getUserExtendInfo().getThirdPlatformInfo();
            if (userThirdPlatformInfo != null) {
                UserThirdPlatformInfo.BankInfoRespBean bankInfoRespBean = userThirdPlatformInfo.getBankInfoResp();
                if (bankInfoRespBean == null || 0 == bankInfoRespBean.getIsBinded()) {
                    Router.getInstance()
                            .buildWithUrl("hmiou://m.54jietiao.com/pay/user_bind_bank")
                            .navigation(mContext);
                } else {
                    mPersonalDialogHelper.showBinkBankInfo(bankInfoRespBean.getBankCard(), bankInfoRespBean.getBankPhone());
                }
                return;
            }
        }
    }

    /**
     * 显示变更密码菜单
     */
    private void showChangePasswordMenu() {
        if (mChangePwdDialog == null) {
            String itemChangeLoginPassword = getString(R.string.personal_changeLoginPassword);
            String itemChangeSignaturePassword = getString(R.string.personal_changeSignaturePassword);
            mChangePwdDialog = new IOSActionSheetTitleDialog.Builder(mContext)
                    .setCanceledOnTouchOutside(true)
                    .addSheetItem(IOSActionSheetItem.create(itemChangeLoginPassword).setItemClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Router.getInstance()
                                    .buildWithUrl("hmiou://m.54jietiao.com/person/modify_pwd")
                                    .navigation(mContext);
                        }
                    }))
                    .addSheetItem(IOSActionSheetItem.create(itemChangeSignaturePassword).setItemClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
                            int customerType = userInfo.getType();
                            if (UserDataUtil.isCClass(customerType)) {
                                showNoAuthWhenChangeSignPwd();
                            } else {
                                Router.getInstance()
                                        .buildWithUrl("hmiou://m.54jietiao.com/facecheck/facecheckfindsignpsd")
                                        .navigation(mContext);
                            }
                        }
                    }))
                    .show();
        } else {
            mChangePwdDialog.show();
        }
    }

    /**
     * 当变更签约密码时，如果没实名认证，弹出提示对话框
     */
    private void showNoAuthWhenChangeSignPwd() {
        new IOSAlertDialog.Builder(mContext)
                .setTitle("签约密码")
                .setMessage("通过实名认证后的账户，才能设置签约密码，是否立即实名认证？")
                .setPositiveButton("立即认证", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Router.getInstance()
                                .buildWithUrl("hmiou://m.54jietiao.com/facecheck/authentication")
                                .navigation(mContext);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

}
