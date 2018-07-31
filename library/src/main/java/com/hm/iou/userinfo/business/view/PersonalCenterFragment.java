package com.hm.iou.userinfo.business.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hm.iou.base.BaseFragment;
import com.hm.iou.logger.Logger;
import com.hm.iou.router.Router;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.sharedata.model.UserThirdPlatformInfo;
import com.hm.iou.tools.ImageLoader;
import com.hm.iou.uikit.HMTopBarView;
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
    @BindView(R2.id.iv_bindBank)
    ImageView mIvBindBank;               //实名认证标志
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
    @BindView(R2.id.tv_person_charge)
    TextView mTvCardCharge;

    private boolean mClickFavorite;
    private boolean mClickFeedback;

    private long mLastUpdateStatisticData;  //记录上一次刷新统计数据的时间
    PersonalDialogHelper mPersonalDialogHelper;

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
        mPersonalDialogHelper = new PersonalDialogHelper(mActivity);
        mPresenter.init();
        mPresenter.getStatisticData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Logger.i("=====personal center onHiddenChanged: " + hidden);
        //切换过来时，需要刷新，但是防止频繁切换刷新
        if (!hidden && System.currentTimeMillis() - mLastUpdateStatisticData > 15000) {
            mClickFavorite = false;
            mClickFeedback = false;
            //去刷新收藏数、未读反馈数等
            mPresenter.getStatisticData();
            mLastUpdateStatisticData = System.currentTimeMillis();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.i("======personal center onResume=========");
        if (mClickFavorite || mClickFeedback) {
            mClickFavorite = false;
            mClickFeedback = false;
            //去刷新收藏数、未读反馈数等
            mPresenter.getStatisticData();
        }
    }

    @OnClick({R2.id.iv_header, R2.id.ll_header, R2.id.iv_authentication, R2.id.iv_bindBank, R2.id.ll_person_signature, R2.id.ll_person_profile, R2.id.ll_person_favorite, R2.id.ll_person_charge,
            R2.id.ll_person_cloud_space, R2.id.ll_person_helpcenter, R2.id.ll_person_about})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ll_header || id == R.id.iv_header) {
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/my_profile")
                    .navigation(mActivity);
        } else if (id == R.id.iv_authentication) {
            UserInfo userInfo = UserManager.getInstance(getActivity()).getUserInfo();
            int customerType = userInfo.getType();
            if (UserDataUtil.isCClass(customerType)) {
                Router.getInstance()
                        .buildWithUrl("hmiou://m.54jietiao.com/facecheck/authentication")
                        .navigation(getActivity());
            } else {
                mPersonalDialogHelper.showHaveAuthtication();
            }
        } else if (id == R.id.iv_bindBank) {
            UserInfo userInfo = UserManager.getInstance(getActivity()).getUserInfo();
            int customerType = userInfo.getType();
            if (UserDataUtil.isCClass(customerType)) {
                mPersonalDialogHelper.showBinkBankNeedAuthen();
            } else {
                UserThirdPlatformInfo userThirdPlatformInfo = UserManager.getInstance(mActivity).getUserExtendInfo().getThirdPlatformInfo();
                if (userThirdPlatformInfo != null) {
                    UserThirdPlatformInfo.BankInfoRespBean bankInfoRespBean = userThirdPlatformInfo.getBankInfoResp();
                    if (bankInfoRespBean == null || 0 == bankInfoRespBean.getIsBinded()) {
                        Router.getInstance()
                                .buildWithUrl("hmiou://m.54jietiao.com/pay/user_bind_bank")
                                .navigation(mActivity);
                    } else {
                        mPersonalDialogHelper.showBinkBankInfo(bankInfoRespBean.getBankName(), bankInfoRespBean.getBankCard(), bankInfoRespBean.getBankCardType(), bankInfoRespBean.getBankPhone());
                    }
                }
            }
        } else if (id == R.id.ll_person_signature) {
            UserInfo userInfo = UserManager.getInstance(getActivity()).getUserInfo();
            int customerType = userInfo.getType();
            if (UserDataUtil.isCClass(customerType)) {
                mPersonalDialogHelper.showNoAuthWhenSetSignature();
            } else {
                Router.getInstance()
                        .buildWithUrl("hmiou://m.54jietiao.com/signature/check_sign_psd")
                        .withString("url", "hmiou://m.54jietiao.com/signature/signature_list")
                        .navigation(getActivity());
            }
        } else if (id == R.id.ll_person_profile) {  //我的资料
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/my_profile")
                    .navigation(mActivity);
        } else if (id == R.id.ll_person_favorite) { //我的收藏
            mClickFavorite = true;
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/news/favorite")
                    .navigation(mActivity);
        } else if (id == R.id.ll_person_charge) {    //变更密码
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/pay/time_card_recharge")
                    .navigation(mActivity);
        } else if (id == R.id.ll_person_cloud_space) {  //云存储空间
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/cloud_space")
                    .navigation(mActivity);
        } else if (id == R.id.ll_person_helpcenter) {   //帮助
            mClickFeedback = true;
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
    public void showBindBankImg(int resId) {
        mIvBindBank.setImageResource(resId);
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

}