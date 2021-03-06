package com.hm.iou.userinfo.business.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.utils.TraceUtil;
import com.hm.iou.logger.Logger;
import com.hm.iou.router.Router;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.tools.ImageLoader;
import com.hm.iou.tools.SystemUtil;
import com.hm.iou.uikit.dialog.HMActionSheetDialog;
import com.hm.iou.uikit.dialog.HMAlertDialog;
import com.hm.iou.userinfo.NavigationHelper;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;
import com.hm.iou.userinfo.business.ProfileContract;
import com.hm.iou.userinfo.business.presenter.LogoutUtil;
import com.hm.iou.userinfo.business.presenter.ProfilePresenter;
import com.hm.iou.userinfo.business.presenter.UserDataUtil;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author syl
 * @time 2019/3/19 1:29 PM
 */
public class ProfileActivity extends BaseActivity<ProfilePresenter> implements ProfileContract.View {

    private static final int REQ_SELECT_CITY = 100;

    @BindView(R2.id.iv_profile_avatar)
    ImageView mIvAvatar;
    @BindView(R2.id.tv_profile_nickname)
    TextView mTvNickname;
    @BindView(R2.id.tv_profile_my_alipay)
    TextView mTvProfileMyAliPay;
    @BindView(R2.id.tv_profile_mobile)
    TextView mTvMobile;
    @BindView(R2.id.tv_profile_weixin)
    TextView mTvWeixin;
    @BindView(R2.id.tv_profile_city)
    TextView mTvCity;
    @BindView(R2.id.tv_profile_income)
    TextView mTvIncome;

    private Dialog mChangePwdDialog;
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

    @OnClick(value = {R2.id.ll_profile_avatar, R2.id.ll_profile_nickname, R2.id.ll_profile_my_qr_code,
            R2.id.ll_profile_my_alipay, R2.id.ll_profile_mobile, R2.id.ll_profile_weixin, R2.id.ll_profile_city
            , R2.id.ll_profile_income, R2.id.ll_profile_changepwd, R2.id.ll_profile_login_time})
    void onClick(View v) {
        if (v.getId() == R.id.ll_profile_avatar) {
            TraceUtil.onEvent(mContext, "profile_avatar_click");
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/user_avatar")
                    .navigation(this);
        } else if (v.getId() == R.id.ll_profile_nickname) {
            TraceUtil.onEvent(mContext, "profile_nickname_click");
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/modify_nickname_sex")
                    .navigation(this);
        } else if (v.getId() == R.id.ll_profile_my_qr_code) {
            TraceUtil.onEvent(mContext, "profile_my_qr_code_click");
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/qrcode/index?show_type=show_my_card")
                    .navigation(this);
        } else if (v.getId() == R.id.ll_profile_my_alipay) {
            TraceUtil.onEvent(mContext, "profile_my_alipay_click");
            String aliPayAccount = mTvProfileMyAliPay.getText().toString();
            if (getString(R.string.personal_noBindAliPay).equals(aliPayAccount)) {
                aliPayAccount = "";
            }
            NavigationHelper.toChangeAliPay(mContext, aliPayAccount);
        } else if (v.getId() == R.id.ll_profile_mobile) {
            TraceUtil.onEvent(mContext, "profile_modify_mob_click");
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/change_mobile")
                    .navigation(mContext);
        } else if (v.getId() == R.id.ll_profile_weixin) {
            TraceUtil.onEvent(mContext, "profile_wx_click");
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
        } else if (v.getId() == R.id.ll_profile_city) {
            TraceUtil.onEvent(mContext, "profile_city_click");
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/city/index")
                    .navigation(this, REQ_SELECT_CITY);
        } else if (v.getId() == R.id.ll_profile_income) {
            TraceUtil.onEvent(mContext, "profile_income_click");
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/my_income")
                    .navigation(this);
        } else if (v.getId() == R.id.ll_profile_changepwd) {        //修改密码
            TraceUtil.onEvent(mContext, "profile_modifypwd_click");
            showChangePasswordMenu();
        } else if (v.getId() == R.id.ll_profile_login_time) {       //退出登录
            LogoutUtil.showLogoutConfirmDialog(this, this);
        }
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
    public void showNicknameAndSex(String nickname, int sexIcon) {
        mTvNickname.setText(nickname);
    }

    @Override
    public void showMobile(String mobile) {
        mTvMobile.setText(mobile);
    }

    @Override
    public void showAliPay(String aliPay, int textColor) {
        mTvProfileMyAliPay.setText(aliPay);
        mTvProfileMyAliPay.setTextColor(textColor);
    }

    @Override
    public void showWeixin(String weixin, int textColor) {
        mTvWeixin.setText(weixin);
        mTvWeixin.setTextColor(textColor);
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
     * 显示变更密码菜单
     */
    private void showChangePasswordMenu() {
        if (mChangePwdDialog == null) {
            String itemChangeLoginPassword = getString(R.string.personal_changeLoginPassword);
            String itemChangeSignaturePassword = getString(R.string.personal_changeSignaturePassword);
            List<String> list = Arrays.asList(itemChangeLoginPassword, itemChangeSignaturePassword);
            mChangePwdDialog = new HMActionSheetDialog.Builder(this)
                    .setActionSheetList(list)
                    .setCanSelected(false)
                    .setOnItemClickListener(new HMActionSheetDialog.OnItemClickListener() {
                        @Override
                        public void onItemClick(int i, String s) {
                            if (i == 0) {
                                Router.getInstance()
                                        .buildWithUrl("hmiou://m.54jietiao.com/person/modify_pwd")
                                        .navigation(mContext);
                            } else if (i == 1) {
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
                        }
                    })
                    .create();
            mChangePwdDialog.show();
        } else {
            mChangePwdDialog.show();
        }
    }

    /**
     * 当变更签约密码时，如果没实名认证，弹出提示对话框
     */
    private void showNoAuthWhenChangeSignPwd() {
        new HMAlertDialog.Builder(mContext)
                .setTitle("签约密码")
                .setMessage("通过实名认证后的账户，才能设置签约密码，是否立即实名认证？")
                .setPositiveButton("立即认证")
                .setNegativeButton("取消")
                .setOnClickListener(new HMAlertDialog.OnClickListener() {
                    @Override
                    public void onPosClick() {
                        Router.getInstance()
                                .buildWithUrl("hmiou://m.54jietiao.com/facecheck/authentication")
                                .navigation(mContext);
                    }

                    @Override
                    public void onNegClick() {

                    }
                })
                .create()
                .show();
    }

}
