package com.hm.iou.userinfo.business.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hm.iou.base.BaseFragment;
import com.hm.iou.router.Router;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.tools.ImageLoader;
import com.hm.iou.uikit.HMTopBarView;
import com.hm.iou.uikit.dialog.IOSActionSheetItem;
import com.hm.iou.uikit.dialog.IOSActionSheetTitleDialog;
import com.hm.iou.uikit.dialog.IOSAlertDialog;
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

public class PersonalCenterFragment extends BaseFragment<PersonalCenterPresenter> implements PersonalCenterContract.View {

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

    @BindView(R2.id.tv_person_profile_progress)
    TextView mTvProfileProgress;         //资料完成度
    @BindView(R2.id.tv_person_cloud_space)
    TextView mTvCloudSpace;              //云存储空间
    @BindView(R2.id.tv_person_unread)
    TextView mTvUnReadCount;             //帮助与反馈未读数目
    @BindView(R2.id.tv_person_favorite_count)
    TextView mTvFavoriteCount;          //收藏的篇数

    private IOSActionSheetTitleDialog mChangePwdDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.person_fragment_personal_center;
    }

    @Override
    protected PersonalCenterPresenter initPresenter() {
        return new PersonalCenterPresenter(getActivity(), this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mTopBarView.setRightIcon(R.mipmap.person_ic_personal_qr_code);
        mTopBarView.showDivider(false);
        mTopBarView.hideBackIcon();
        mTopBarView.setOnMenuClickListener(new HMTopBarView.OnTopBarMenuClickListener() {
            @Override
            public void onClickTextMenu() {

            }

            @Override
            public void onClickImageMenu() {
                Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/qrcode/index")
                        .withString("show_type", "show_my_card")
                        .navigation(mActivity);
            }
        });

        mPresenter.init();
    }


    @OnClick({R2.id.ll_person_signature, R2.id.ll_person_profile, R2.id.ll_person_favorite, R2.id.ll_person_changepwd,
        R2.id.ll_person_cloud_space, R2.id.ll_person_helpcenter, R2.id.ll_person_about})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ll_person_signature) {
            UserInfo userInfo = UserManager.getInstance(getActivity()).getUserInfo();
            int customerType = userInfo.getType();
            if (UserDataUtil.isCClass(customerType)) {
                showNoAuthWhenSetSignature();
            } else {
                Router.getInstance()
                        .buildWithUrl("hmiou://m.54jietiao.com/signature/check_sign_psd")
                        .navigation(getActivity());
            }
        } else if (id == R.id.ll_person_profile) {  //我的资料
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/my_profile")
                    .navigation(mActivity);
        } else if (id == R.id.ll_person_favorite) { //我的收藏
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/news/favorite")
                    .navigation(mActivity);
        } else if (id == R.id.ll_person_changepwd) {    //变更密码
            showChangePasswordMenu();
        } else if (id == R.id.ll_person_cloud_space) {  //云存储空间

        } else if (id == R.id.ll_person_helpcenter) {   //帮助
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/message/helpcenter")
                    .navigation(mActivity);
        } else if (id == R.id.ll_person_about) {    //关于我们
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/about")
                    .navigation(mActivity);
        }
    }

    @Override
    public void showUserAvatar(String avatarUrl, int defaultAvatarResId) {
        if (TextUtils.isEmpty(avatarUrl)) {
            mIvHeader.setImageResource(defaultAvatarResId);
            return;
        }
        ImageLoader.getInstance(mActivity).displayImage(avatarUrl, mIvHeader, defaultAvatarResId, defaultAvatarResId);
    }

    @Override
    public void showUserNickname(String nickname) {
        mTvUserNickName.setText(nickname);
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
    public void showProfileProgress(String progressStr) {
        mTvProfileProgress.setText(progressStr);
    }

    @Override
    public void showNewsFavoriteCount(String favoriteCount) {
        mTvFavoriteCount.setText(favoriteCount);
    }

    @Override
    public void showCloudSpace(String space) {
        mTvCloudSpace.setText(space);
    }

    @Override
    public void showHelpAndFeedbackCount(String feedbackUnreadCount) {
        if (TextUtils.isEmpty(feedbackUnreadCount)) {
            mTvUnReadCount.setVisibility(View.INVISIBLE);
        } else {
            mTvUnReadCount.setVisibility(View.VISIBLE);
            mTvUnReadCount.setText(feedbackUnreadCount);
        }
    }

    /**
     * 当设置签名时，如果没有实名，弹出实名提醒
     */
    private void showNoAuthWhenSetSignature() {
        new IOSAlertDialog.Builder(mActivity)
                .setTitle("设置手写签名")
                .setMessage("通过实名认证后的账户，才能设置手写签名，是否立即认证实名？")
                .setPositiveButton("立即认证", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Router.getInstance()
                                .buildWithUrl("hmiou://m.54jietiao.com/facecheck/authentication")
                                .navigation(getActivity());
                    }
                })
                .setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();


    }

    /**
     * 显示变更密码菜单
     */
    private void showChangePasswordMenu() {
        if (mChangePwdDialog == null) {
            String itemChangeLoginPassword = getString(R.string.personal_changeLoginPassword);
            String itemChangeSignaturePassword = getString(R.string.personal_changeSignaturePassword);
            mChangePwdDialog = new IOSActionSheetTitleDialog.Builder(mActivity)
                    .setCanceledOnTouchOutside(true)
                    .addSheetItem(IOSActionSheetItem.create(itemChangeLoginPassword).setItemClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Router.getInstance()
                                    .buildWithUrl("hmiou://m.54jietiao.com/person/modify_pwd")
                                    .navigation(mActivity);
                        }
                    }))
                    .addSheetItem(IOSActionSheetItem.create(itemChangeSignaturePassword).setItemClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            UserInfo userInfo = UserManager.getInstance(mActivity).getUserInfo();
                            int customerType = userInfo.getType();
                            if (UserDataUtil.isCClass(customerType)) {
                                showNoAuthWhenChangeSignPwd();
                            } else {
                                Router.getInstance()
                                        .buildWithUrl("hmiou://m.54jietiao.com/facecheck/facecheckfindsignpsd")
                                        .navigation(mActivity);
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
        new IOSAlertDialog.Builder(mActivity)
                .setTitle("签约密码")
                .setMessage("通过实名认证后的账户，才能设置签约密码，是否立即实名认证？")
                .setPositiveButton("立即认证", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Router.getInstance()
                                .buildWithUrl("hmiou://m.54jietiao.com/facecheck/authentication")
                                .navigation(mActivity);
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