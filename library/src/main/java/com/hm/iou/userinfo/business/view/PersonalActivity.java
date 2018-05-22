package com.hm.iou.userinfo.business.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.BuildConfig;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.tools.ImageLoader;
import com.hm.iou.tools.StringUtil;
import com.hm.iou.uikit.dialog.DialogCommonKnow;
import com.hm.iou.uikit.dialog.IOSActionSheetItem;
import com.hm.iou.uikit.dialog.IOSActionSheetTitleDialog;
import com.hm.iou.uikit.dialog.IOSAlertDialog;
import com.hm.iou.userinfo.NavigationHelper;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;
import com.hm.iou.userinfo.business.contract.PersonalContract;
import com.hm.iou.userinfo.business.presenter.PersonalPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个人中心
 *
 * @author AFinalStone
 * @time 2018/4/23 上午11:08
 */
public class PersonalActivity extends BaseActivity<PersonalPresenter> implements PersonalContract.View {

    private static final int REQ_CODE_CITY_SELECT = 100;

    @BindView(R2.id.iv_action)
    ImageView ivAction;
    @BindView(R2.id.tv_userNickName)
    TextView tvUserNickName;
    @BindView(R2.id.tv_userId)
    TextView tvUserId;
    @BindView(R2.id.iv_authentication)
    ImageView ivAuthentication;
    @BindView(R2.id.tv_signatureMake)
    TextView tvSignatureMake;

    @BindView(R2.id.iv_header)
    ImageView ivHeader;
    @BindView(R2.id.tv_modulePhone)
    TextView tvModulePhone;
    @BindView(R2.id.tv_moduleName)
    TextView tvModuleName;
    @BindView(R2.id.tv_moduleLivePlace)
    TextView tvModuleLivePlace;
    @BindView(R2.id.tv_moduleMainInCome)
    TextView tvModuleMainInCome;
    @BindView(R2.id.tv_currentVersion)
    TextView tvCurrentVersion;

    @BindView(R2.id.iv_sex)
    ImageView ivSex;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal;
    }

    @Override
    protected PersonalPresenter initPresenter() {
        return new PersonalPresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
        initData();
    }

    private void initData() {
        UserInfo userDataBean = UserManager.getInstance(this).getUserInfo();
        String userShowId = userDataBean.getShowId();
        String location = userDataBean.getLocation();
        String mobile = userDataBean.getMobile();
        String nickName = userDataBean.getNickName();
        String userName = userDataBean.getName();
        String incomeMain = userDataBean.getMainIncome();
        String currentVersion = getString(R.string.personal_currentVersion) + BuildConfig.VERSION_NAME;
        //手机号
        tvModulePhone.setText(mobile);
        //用户ID
        tvUserId.setText(userShowId);
        //软件版本
        tvCurrentVersion.setText(currentVersion);
        //地址
        if (!StringUtil.isEmpty(location)) {
            tvModuleLivePlace.setText(location);
        }
        //昵称
        if (!StringUtil.isEmpty(nickName)) {
            tvUserNickName.setText(nickName);
            tvModuleName.setText(nickName);
        }
        //用户类型
        String customerTypeEnum = userDataBean.getType();
        if ("CSub".equals(customerTypeEnum) || "CPlus".equals(customerTypeEnum)) {
            ivAuthentication.setImageResource(R.mipmap.icon_authentication_not_have);
        } else {
            if (!StringUtil.isEmpty(userName)) {
                tvSignatureMake.setText(userName);
            }
            ivAuthentication.setImageResource(R.mipmap.icon_authentication_have);
        }

        //我的收入
        displayIncome(tvModuleMainInCome, incomeMain);

        //头像
        String sexEnum = userDataBean.getSex();
        int imageResId;
        if ("MALE".equals(sexEnum)) {
            imageResId = R.mipmap.uikit_icon_header_man;
            ivSex.setImageResource(R.mipmap.icon_personal_name_sex_man);
        } else if ("FEMALE".equals(sexEnum)) {
            imageResId = R.mipmap.uikit_icon_header_wuman;
            ivSex.setImageResource(R.mipmap.icon_personal_name_sex_wuman);
        } else {
            imageResId = R.mipmap.uikit_icon_header_unknow;
            ivSex.setImageResource(R.mipmap.icon_personal_name_sex_unkonw);
        }
        ivHeader.setImageResource(imageResId);
        String urlHeader = userDataBean.getAvatarUrl();
        ImageLoader.getInstance(mContext).displayImage(urlHeader, ivHeader, imageResId, imageResId);
    }

    private void displayIncome(TextView tv, String income) {
        if ("Else".equals(income)) {
            tv.setText(R.string.myIncome_other);
        } else if ("Wages".equals(income)) {
            tv.setText(R.string.myIncome_salary);
        } else if ("Parents".equals(income)) {
            tv.setText(R.string.myIncome_ParentsSupport);
        } else if ("Business".equals(income)) {
            tv.setText(R.string.myIncome_business);
        } else if ("Investment".equals(income)) {
            tv.setText(R.string.myIncome_invest);
        } else {
            tv.setText(R.string.myIncome_nothing);
        }
    }

    @OnClick({R2.id.iv_header, R2.id.iv_action
            , R2.id.tv_signatureMake, R2.id.relativeLayout_phoneAndWx
            , R2.id.relativeLayout_livePlace, R2.id.relativeLayout_nameAndSex
            , R2.id.relativeLayout_mainIncome, R2.id.linearLayout_changePassword
            , R2.id.linearLayout_helpAndTickling,
            R2.id.btn_logoutSafely, R2.id.tv_personalSignature, R2.id.ll_person_favorite})
    public void onClick(View view) {
        int id = view.getId();
        if (R.id.iv_header == id) {
            toHeaderDetailView();
        } else if (R.id.iv_action == id) {
            toMyCardView();
        } else if (R.id.tv_signatureMake == id || R.id.tv_personalSignature == id) {
            UserInfo userInfo = UserManager.getInstance(this).getUserInfo();
            String customerType = userInfo.getType();
            if ("CSub".equals(customerType) || "CPlus".equals(customerType)) {
                mPresenter.canRealNameAuth(userInfo.getUserId(), false);
            } else {
                toCheckSignPassword();
            }
        } else if (R.id.relativeLayout_phoneAndWx == id) {
            showUnBindWxAndPhone();
        } else if (R.id.relativeLayout_livePlace == id) {
            toCityPickerView();
        } else if (R.id.relativeLayout_nameAndSex == id) {
            toChangeNameAndSexView();
        } else if (R.id.relativeLayout_mainIncome == id) {
            toMyIncomeView();
        } else if (R.id.linearLayout_changePassword == id) {
            showChangePasswordMenu();
        } else if (R.id.linearLayout_helpAndTickling == id) {
            toHelpCenterView();
        } else if (R.id.btn_logoutSafely == id) {
            showDialogLogoutSafely();
        } else if (R.id.ll_person_favorite == id) {
            NavigationHelper.toNewsFavorite(mContext);
        }
    }

    private void toMyCardView() {
        NavigationHelper.toQRCode(mContext);
    }

    void toCityPickerView() {
//        startNewActivityForResult(CityPickerActivity.class, Constants.REQUEST_CODE_PICK_CITY);
//        startNewActivityForResult(CitySelectActivity.class, REQ_CODE_CITY_SELECT);
    }

    void toHelpCenterView() {
//        startNewActivity(HelpCenterActivity.class);

    }

    void toHeaderDetailView() {
//        startNewActivity(UserHeaderDetailActivity.class);
    }

    private void toMyIncomeView() {
//        startNewActivity(MyIncomeActivity.class);
    }

    private void toUnBindWXView() {
//        startNewActivity(UnBindWxActivity.class);
    }

    private void toBindWXView() {
//        boolean flag = SystemUtil.isAppInstalled(HMApplication.getInstance(), Constants.PACKAGE_NAME_OF_WXCHAT);
//        if (flag) {
//            mPresenter.getWxCode(mContext);
//        } else {
//            showErrorMsg(R.string.mobileNoInstallWxChat);
//        }
    }
//
//    @Override
//    public void getWxCodeSuccess(String code) {
//        mPresenter.isWXExist(code);
//    }
//
//    @Override
//    public void bindWxAfterLoginSuccess(CustomerTypeEnum typeEnum) {
//        UserInfo userDataBean = UserManager.getInstance(this).getUserInfo();
//        userDataBean.setType(typeEnum.name());
//        UserManager.getInstance(this).updateOrSaveUserInfo(userDataBean);
//        HttpReqManager.getInstance().setUserId(userDataBean.getUserId());
//        HttpReqManager.getInstance().setToken(userDataBean.getToken());
//
//        showSuccessMsg(R.string.personal_bindWXSuccess);
//    }

//    /**
//     * 当前手机的微信已经绑定
//     *
//     * @param wxSn
//     */
//    @Override
//    public void mobileWXHaveBind(String wxSn) {
//        showSuccessMsg(R.string.personal_wxHaveBindPhone);
//    }
//
//    /**
//     * 当前手机的微信没有绑定账号
//     *
//     * @param wxSn
//     */
//    @Override
//    public void mobileWXNotBind(String wxSn) {
//        mPresenter.bindWXAfterLogin(wxSn);
//    }


    //修改昵称和性别
    private void toChangeNameAndSexView() {
//        startNewActivityForResult(NickNameAndSexActivity.class, Constants.REQUEST_NAME_AND_SEX);
    }

    //进入签约密码校验页面
    private void toCheckSignPassword() {
//        startNewActivityForResult(SignatureCheckPsdActivity.class, Constants.REQUEST_CHECK_SIGNTURE_PASSWORD);
    }


    //绑定邮箱
    public void toBindEmailView() {
//        Intent intent = new Intent(mContext, BindEmailAndResetPsdActivity.class);
//        intent.putExtra(Constants.INTENT_BIND_EMAIL_AND_RESET_PSD, BindEmailAndResetPsdEnum.BindEmail);
//        intent.putExtra(Constants.INTENT_MOBILE_NUMBER, UserManager.getInstance(this).getUserInfo().getMobile());
//        startActivity(intent);
    }


    void toChangeLoginPasswordView() {
//        startNewActivity(ChangeLoginPasswordActivity.class);
    }

    void toChangeBindPhoneActivityView() {
//        startNewActivity(ChangeBindPhoneActivity.class);
    }

    void toChangeSignaturePsdView() {
//        Intent intent = new Intent(this, LivingCheckActivity.class);
//        intent.putExtra(Constants.INTENT_LIVINIG_CHECK_RESET_PSD_TYPE, LivingCheckResetPsdTypeEnum.LivingCheckResetSignaturePsd);
//        startActivity(intent);
    }

    //重写onActivityResult方法，通过第三方获取定位
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (REQ_CODE_CITY_SELECT == requestCode && resultCode == RESULT_OK) {
//            if (data != null) {
//                //获取城市编码
//                String cityCode = data.getStringExtra(CitySelectActivity.EXTRA_RESULT_KEY_CITY_CODE);
//                //获取城市名称
//                String cityName = data.getStringExtra(CitySelectActivity.EXTRA_RESULT_KEY_CITY_NAME);
//                Logger.d("城市编码" + cityCode + "城市名称" + cityName);
//                mPresenter.changeLocation(cityName);
//            }
//        }
//        if (Constants.REQUEST_CHECK_SIGNTURE_PASSWORD == requestCode) {
//            if (resultCode == RESULT_OK) {
//                startNewActivity(SignatureTypeActivity.class);
//            }
//        }
//        if (Constants.REQUEST_NAME_AND_SEX == requestCode) {
//            if (resultCode == RESULT_OK) {
//                String newName = data.getStringExtra(Constants.INTENT_USER_NAME);
//                String sexEnum = data.getStringExtra(Constants.INTENT_USER_SEX);
//                if (!StringUtil.isEmpty(newName)) {
//                    mPresenter.changeNickName(newName);
//                }
//                if (sexEnum != null) {
//                    if ("FEMALE".equals(sexEnum)) {
//                        mPresenter.changeSex(SexEnum.FEMALE);
//                    } else if ("MALE".equals(sexEnum)) {
//                        mPresenter.changeSex(SexEnum.MALE);
//                    }
//                }
//            }
//        }
    }


    private void showChangePasswordMenu() {
        String itemChangeLoginPassword = getString(R.string.personal_changeLoginPassword);
        String itemChangeSignaturePassword = getString(R.string.personal_changeSignaturePassword);
        new IOSActionSheetTitleDialog.Builder(mContext)
                .setCanceledOnTouchOutside(true)
                .addSheetItem(IOSActionSheetItem.create(itemChangeLoginPassword).setItemClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toChangeLoginPasswordView();
                        dialog.dismiss();
                    }
                }))
                .addSheetItem(IOSActionSheetItem.create(itemChangeSignaturePassword).setItemClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserInfo userDataBean = UserManager.getInstance(PersonalActivity.this).getUserInfo();
                        String customerType = userDataBean.getType();
                        if ("CSub".equals(customerType) || "CPlus".equals(customerType)) {
                            toastMessage(R.string.personal_pleaseNeedAuthentication);
                        } else {
                            mPresenter.canRealNameAuth(userDataBean.getUserId(), true);
                        }
                        dialog.dismiss();
                    }
                }))
                .show();
    }

    private void showUnBindWxAndPhone() {
        String strBindWX = getString(R.string.personal_bindWX);
        String strUnBindWX = getString(R.string.personal_unBindWX);
        String strChangePhone = getString(R.string.personal_unBindPhone);
        String typeEnum = UserManager.getInstance(this).getUserInfo().getType();
        if ("CSub".equals(typeEnum) || "BSub".equals(typeEnum) || "ASub".equals(typeEnum)) {
            new IOSActionSheetTitleDialog.Builder(mContext)
                    .setCanceledOnTouchOutside(true)
                    .addSheetItem(IOSActionSheetItem.create(strBindWX).setItemClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            toBindWXView();
                        }
                    }))
                    .addSheetItem(IOSActionSheetItem.create(strChangePhone).setItemClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            toChangeBindPhoneActivityView();
                        }
                    }))
                    .show();
        } else if ("CPlus".equals(typeEnum) || "BPlus".equals(typeEnum) || "APlus".equals(typeEnum)) {
            new IOSActionSheetTitleDialog.Builder(mContext)
                    .setCanceledOnTouchOutside(true)
                    .addSheetItem(IOSActionSheetItem.create(strUnBindWX).setItemClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            toUnBindWXView();
                        }
                    }))
                    .addSheetItem(IOSActionSheetItem.create(strChangePhone).setItemClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            toChangeBindPhoneActivityView();
                        }
                    }))
                    .show();
        }
    }

    private void showAuthenticationDialog() {
        String cancel = getString(R.string.base_cancel);
        String ok = getString(R.string.personal_dialogNeedAuthenticationOK);
        String title = getString(R.string.personal_dialogNeedAuthenticationTitle);
        String msg = getString(R.string.personal_dialogNeedAuthenticationMsg);

        new IOSAlertDialog.Builder(mContext)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        NavigationHelper.toAuthentication(mContext);
                    }
                })
                .setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    private void showAuthenticationDialogByNotEnoughTimes() {
        String title = getString(R.string.personal_dialogByNotEnoughTimesTitle);
        String msg = getString(R.string.personal_dialogByNotEnoughTimesMsg);
        new DialogCommonKnow.Builder(mContext).setMsg(msg).setTitle(title).show();
    }

    private void showDialogLogoutSafely() {
        String title = getString(R.string.dialog_systemTipTitle);
        String msg = getString(R.string.dialog_tvLogoutMsg);
        String ok = getString(R.string.dialog_tvLogoutOk);
        String cancel = getString(R.string.dialog_tvLogoutCancel);
        new IOSAlertDialog.Builder(mContext)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(ok, (dialog, which) -> {
                    dialog.dismiss();
//                    logoutSuccess();
                })
                .setNegativeButton(cancel, (dialog, which) -> {
                    dialog.dismiss();
                }).show();
    }

    private void jumpToLoginSelectView() {
//        HMApplication.getInstance().clearUserDataBean();
//        HMApplication.getInstance().exitApp(false);
//        startNewActivity(LoginSelectActivity.class);
    }

//    @Override
//    public void canRealNameAuthSuccess(boolean isToChangeSignaturePsd) {
//        if (isToChangeSignaturePsd) {
//            toChangeSignaturePsdView();
//        } else {
//            showAuthenticationDialog();
//        }
//    }
//
//    @Override
//    public void canRealNameAuthFailedByNotAdult(boolean isToChangeSignaturePsd) {
//        startNewActivity(NotAdultActivity.class);
//    }
//
//    @Override
//    public void canRealNameAuthFailedByNotEnoughTimes(boolean isToChangeSignaturePsd) {
//        showAuthenticationDialogByNotEnoughTimes();
//    }
//
//    @Override
//    public void changeLocationSuccess(String city) {
//        UserInfo userDataBean = UserManager.getInstance(this).getUserInfo();
//        userDataBean.setLocation(city);
//        UserManager.getInstance(this).updateOrSaveUserInfo(userDataBean);
//        tvModuleLivePlace.setText(city);
//    }
//
//    @Override
//    public void logoutSuccess() {
//        jumpToLoginSelectView();
//    }
//
//    @Override
//    public void logoutFailed() {
//        HMApplication.getInstance().clearUserDataBean();
//        HMApplication.getInstance().exitApp(false);
//        startNewActivity(LoginSelectActivity.class);
//    }
//
//    @Override
//    public void changeNickNameSuccess(String nickName) {
//        UserInfo userDataBean = UserManager.getInstance(this).getUserInfo();
//        userDataBean.setNickName(nickName);
//        UserManager.getInstance(this).updateOrSaveUserInfo(userDataBean);
//        tvUserNickName.setText(nickName);
//        tvModuleName.setText(nickName);
//    }
//
//    @Override
//    public void changeSexSuccess(SexEnum sexEnum) {
//        UserInfo userDataBean = UserManager.getInstance(this).getUserInfo();
//        userDataBean.setSex(sexEnum.name());
//        UserManager.getInstance(this).updateOrSaveUserInfo(userDataBean);
//        switch (sexEnum) {
//            case UNKNOWN:
//                ivHeader.setImageResource(R.mipmap.icon_header_unknow);
//                ivSex.setImageResource(R.mipmap.icon_personal_name_sex_unkonw);
//                break;
//            case MALE:
//                ivHeader.setImageResource(R.mipmap.icon_header_man);
//                ivSex.setImageResource(R.mipmap.icon_personal_name_sex_man);
//                break;
//            case FEMALE:
//                ivHeader.setImageResource(R.mipmap.icon_header_wuman);
//                ivSex.setImageResource(R.mipmap.icon_personal_name_sex_wuman);
//                break;
//        }
//        String urlHeader = userDataBean.getAvatarUrl();
//        ImageLoaderUtil.getInstance(mContext).displayMyImage(urlHeader, ivHeader);
//    }


}
