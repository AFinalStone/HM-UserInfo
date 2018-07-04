package com.hm.iou.userinfo.business.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.logger.Logger;
import com.hm.iou.router.Router;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.socialshare.SocialShareUtil;
import com.hm.iou.tools.ImageLoader;
import com.hm.iou.tools.SystemUtil;
import com.hm.iou.uikit.HMTopBarView;
import com.hm.iou.uikit.dialog.IOSActionSheetItem;
import com.hm.iou.uikit.dialog.IOSActionSheetTitleDialog;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;
import com.hm.iou.userinfo.business.PersonalCenterContract;
import com.hm.iou.userinfo.business.presenter.PersonalCenterPresenter;
import com.hm.iou.userinfo.business.presenter.UserDataUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hjy on 2018/5/23.
 */

public class PersonalCenterActivity extends BaseActivity<PersonalCenterPresenter> implements PersonalCenterContract.View {

    private static final int REQ_SELECT_CITY = 100;
    private static final int REQ_CHECK_SIGNTURE_PASSWORD = 101;

    @BindView(R2.id.topbar)
    HMTopBarView mTopBarView;

    @BindView(R2.id.iv_header)
    ImageView mIvHeader;                 //头像
    @BindView(R2.id.tv_userNickName)
    TextView mTvUserNickName;            //昵称
    @BindView(R2.id.tv_userId)
    TextView mTvUserId;                  //用户id
    @BindView(R2.id.iv_authentication)
    ImageView mIvAuthentication;         //实名认证标志
    @BindView(R2.id.tv_signatureMake)
    TextView mTvSignatureMake;           //

    @BindView(R2.id.tv_modulePhone)
    TextView mTvModulePhone;             //手机号
    @BindView(R2.id.tv_moduleName)
    TextView mTvModuleName;              //昵称
    @BindView(R2.id.iv_sex)
    ImageView mIvSex;
    @BindView(R2.id.tv_moduleLivePlace)
    TextView mTvModuleLivePlace;         //常驻城市
    @BindView(R2.id.tv_moduleMainInCome)
    TextView mTvModuleMainInCome;        //主要收入
    @BindView(R2.id.tv_currentVersion)
    TextView mTvCurrentVersion;          //当前版本

    @Override
    protected int getLayoutId() {
        return R.layout.person_activity_personal_center;
    }

    @Override
    protected PersonalCenterPresenter initPresenter() {
        return new PersonalCenterPresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mTopBarView.setRightIcon(R.mipmap.person_ic_personal_qr_code);
        mTopBarView.showDivider(false);
        mTopBarView.setOnMenuClickListener(new HMTopBarView.OnTopBarMenuClickListener() {
            @Override
            public void onClickTextMenu() {

            }

            @Override
            public void onClickImageMenu() {
                toMyCardView();
            }
        });

        mPresenter.init();
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

    @OnClick({R2.id.iv_header, R2.id.tv_signatureMake, R2.id.relativeLayout_phoneAndWx
            , R2.id.relativeLayout_livePlace, R2.id.relativeLayout_nameAndSex
            , R2.id.relativeLayout_mainIncome, R2.id.linearLayout_changePassword
            , R2.id.linearLayout_helpAndTickling, R2.id.ll_person_center_about,
            R2.id.btn_logoutSafely, R2.id.tv_personalSignature, R2.id.ll_person_favorite})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_header) {
/*
            Intent intent = new Intent(this, UserHeaderDetailActivity.class);
            startActivity(intent);
*/

        } else if (id == R.id.tv_signatureMake || id == R.id.tv_personalSignature) {
            UserInfo userInfo = UserManager.getInstance(this).getUserInfo();
            int customerType = userInfo.getType();
            if (UserDataUtil.isCClass(customerType)) {
                Router.getInstance()
                        .buildWithUrl("hmiou://m.54jietiao.com/facecheck/authentication")
                        .navigation(this);
            } else {
                Router.getInstance()
                        .buildWithUrl("hmiou://m.54jietiao.com/signature/check_sign_psd")
                        .navigation(this);
            }
        } else if (id == R.id.relativeLayout_phoneAndWx) {
            showUnBindWxAndPhone();
        } else if (id == R.id.relativeLayout_livePlace) {
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/city/index")
                    .navigation(this, REQ_SELECT_CITY);
        } else if (id == R.id.relativeLayout_nameAndSex) {
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/modify_nickname_sex")
                    .navigation(this);
        } else if (id == R.id.relativeLayout_mainIncome) {
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/modify_income")
                    .navigation(this);
        } else if (id == R.id.linearLayout_changePassword) {
            showChangePasswordMenu();
        } else if (id == R.id.linearLayout_helpAndTickling) {
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/message/helpcenter")
                    .navigation(this);
        } else if (id == R.id.ll_person_favorite) {
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/news/favorite")
                    .navigation(this);
        } else if (id == R.id.btn_logoutSafely) {
            showDialogLogoutSafely();
        } else if (id == R.id.ll_person_center_about) {
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/about")
                    .navigation(this);
        }
    }

    @Override
    public void showUserAvatar(String avatarUrl, int defaultAvatarResId) {
        if (TextUtils.isEmpty(avatarUrl)) {
            mIvHeader.setImageResource(defaultAvatarResId);
            return;
        }
        ImageLoader.getInstance(this).displayImage(avatarUrl, mIvHeader, defaultAvatarResId, defaultAvatarResId);
    }

    @Override
    public void showUserNickname(String nickname) {
        mTvUserNickName.setText(nickname);
        mTvModuleName.setText(nickname);
    }

    @Override
    public void showUserId(String id) {
        mTvUserId.setText(id);
    }

    @Override
    public void showAuthenticationImg(int resId) {
        mIvAuthentication.setImageResource(resId);
    }

    @Override
    public void showAuthName(String name) {
        mTvSignatureMake.setText(name);
    }

    @Override
    public void showModuleMobile(String mobile) {
        mTvModulePhone.setText(mobile);
    }

    @Override
    public void showModuleLocation(String city) {
        mTvModuleLivePlace.setText(city);
    }

    @Override
    public void showModuleSexImage(int resId) {
        mIvSex.setImageResource(resId);
    }

    @Override
    public void showModuleMainIncome(String income) {
        mTvModuleMainInCome.setText(income);
    }

    @Override
    public void showAppVersion(String version) {
        mTvCurrentVersion.setText(version);
    }

    /**
     * 跳转到我的卡片
     */
    private void toMyCardView() {
        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/qrcode/index")
                .withString("show_type", "show_my_card")
                .navigation(mContext);
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
                                mPresenter.doLogout();
                            }
                        })).show();
    }

    /**
     * 显示绑定微信或解绑微信以及更换手机号
     */
    private void showUnBindWxAndPhone() {
        String strBindWX = getString(R.string.personal_bindWX);
        String strUnBindWX = getString(R.string.personal_unBindWX);
        UserManager userManager = UserManager.getInstance(this);
        int typeEnum = userManager.getUserInfo().getType();
        final boolean isWeixinBound = UserDataUtil.isSubClass(typeEnum) ? false : true;
        IOSActionSheetTitleDialog.Builder builder = new IOSActionSheetTitleDialog.Builder(mContext)
                .setCanceledOnTouchOutside(true)
                .addSheetItem(IOSActionSheetItem.create(isWeixinBound ? strUnBindWX : strBindWX).setItemClickListener((dialog, which) -> {
                    dialog.dismiss();
                    if (isWeixinBound) {
                        Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/unbind_weixin")
                                .navigation(PersonalCenterActivity.this);
                    } else {
                        boolean flag = SystemUtil.isAppInstalled(getApplicationContext(), "com.tencent.mm");
                        if (flag) {
                            mPresenter.toBindWeixin();
                        } else {
                            toastMessage("当前手机没有安装微信");
                        }
                    }
                }))
                .addSheetItem(IOSActionSheetItem.create("变更手机").setItemClickListener((dialog, which) -> {
                    dialog.dismiss();
                    Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/change_mobile")
                            .navigation(PersonalCenterActivity.this);
                }));
        if (UserDataUtil.isAClass(typeEnum)) {
            builder.addSheetItem(IOSActionSheetItem.create("变更邮箱").setItemClickListener((dialog, which) -> {
                dialog.dismiss();
                Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/change_email")
                        .navigation(PersonalCenterActivity.this);
            }));
        }
        builder.show();
    }

    /**
     * 显示变更密码菜单
     */
    private void showChangePasswordMenu() {
        String itemChangeLoginPassword = getString(R.string.personal_changeLoginPassword);
        String itemChangeSignaturePassword = getString(R.string.personal_changeSignaturePassword);
        new IOSActionSheetTitleDialog.Builder(mContext)
                .setCanceledOnTouchOutside(true)
                .addSheetItem(IOSActionSheetItem.create(itemChangeLoginPassword).setItemClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Router.getInstance()
                                .buildWithUrl("hmiou://m.54jietiao.com/person/modify_pwd")
                                .navigation(PersonalCenterActivity.this);
                    }
                }))
                .addSheetItem(IOSActionSheetItem.create(itemChangeSignaturePassword).setItemClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        UserInfo userInfo = UserManager.getInstance(PersonalCenterActivity.this).getUserInfo();
                        int customerType = userInfo.getType();
                        if (UserDataUtil.isCClass(customerType)) {
                            toastMessage(R.string.personal_pleaseNeedAuthentication);
                        } else {
                            Router.getInstance()
                                    .buildWithUrl("hmiou://m.54jietiao.com/facecheck/facecheckfindsignpsd")
                                    .navigation(mContext);
                        }
                    }
                }))
                .show();
    }

}
