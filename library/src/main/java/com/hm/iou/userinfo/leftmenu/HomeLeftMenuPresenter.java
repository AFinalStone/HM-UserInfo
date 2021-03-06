package com.hm.iou.userinfo.leftmenu;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hm.iou.base.comm.CommApi;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.base.version.CheckVersionResBean;
import com.hm.iou.logger.Logger;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.event.BindBankSuccessEvent;
import com.hm.iou.sharedata.event.CommBizEvent;
import com.hm.iou.sharedata.event.RealNameEvent;
import com.hm.iou.sharedata.event.UpdateUserInfoEvent;
import com.hm.iou.sharedata.model.PersonalCenterInfo;
import com.hm.iou.sharedata.model.UserExtendInfo;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.sharedata.model.UserThirdPlatformInfo;
import com.hm.iou.tools.ACache;
import com.hm.iou.tools.SystemUtil;
import com.hm.iou.userinfo.api.PersonApi;
import com.hm.iou.userinfo.bean.HomeLeftMenuBean;
import com.hm.iou.userinfo.bean.InnerCustomerResBean;
import com.hm.iou.userinfo.bean.UserCenterStatisticBean;
import com.hm.iou.userinfo.business.presenter.UserDataUtil;
import com.hm.iou.userinfo.event.UpdateAliPayEvent;
import com.hm.iou.userinfo.event.UpdateAvatarEvent;
import com.hm.iou.userinfo.event.UpdateEmailEvent;
import com.hm.iou.userinfo.event.UpdateIncomeEvent;
import com.hm.iou.userinfo.event.UpdateMobileEvent;
import com.hm.iou.userinfo.event.UpdateNicknameAndSexEvent;
import com.hm.iou.userinfo.event.UpdateVipEvent;
import com.hm.iou.userinfo.event.UpdateWeixinEvent;
import com.hm.iou.userinfo.util.UserInfoCompleteProgressUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.InputStream;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author syl
 * @time 2019/1/10 4:58 PM
 */

public class HomeLeftMenuPresenter implements HomeLeftMenuContract.Presenter {

    private Context mContext;
    private HomeLeftMenuView mView;
    private Disposable mStatisticDisposable;
    private Disposable mThirdPlatformInfoDisposable;//获取第三方平台的状态
    private Disposable mPersonalInfo;//获取个人中心的用户信息
    private Disposable mIsCustomerServiceDisposable;

    //    private long mLastUpdateStatisticData = System.currentTimeMillis();  //记录上一次刷新统计数据的时间
    private boolean mNeedRefresh = false;

    private boolean mIsRealNameFlag;
    private boolean mHasNewVersionFlag;
    private boolean mInfoCompleteFlag;

    public HomeLeftMenuPresenter(Context context, HomeLeftMenuView view) {
        this.mContext = context;
        mView = view;
        EventBus.getDefault().register(this);
    }

    public void onDestroy() {
        cancelRequest(mStatisticDisposable);
        cancelRequest(mThirdPlatformInfoDisposable);
        cancelRequest(mPersonalInfo);
        cancelRequest(mIsCustomerServiceDisposable);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void init() {
        //顶部模块列表
        HomeLeftMenuBean homeLeftMenuBean = readDataFromAssert();
        mView.showTopMenus(DataUtil.convertHomeModuleBeanToIModuleData(mContext, homeLeftMenuBean.getTopModules()));
        //菜单列表
        List<IListMenuItem> list = DataUtil.convertHomeModuleBeanToIMenuItem(mContext, homeLeftMenuBean.getListMenus());
        mView.showListMenus(list);

        mNeedRefresh = false;
        refreshData();
//        mLastUpdateStatisticData = System.currentTimeMillis();
    }

    @Override
    public void onResume() {
        if (mNeedRefresh) {
            mNeedRefresh = false;
            refreshData();
//            mLastUpdateStatisticData = System.currentTimeMillis();
        } else {
/*            if (System.currentTimeMillis() - mLastUpdateStatisticData > 30000) {
                refreshData();
                mLastUpdateStatisticData = System.currentTimeMillis();
            }*/
        }
    }

    @Override
    public void refreshData() {
        getUserProfile();
        getStatisticData();
        getUserThirdPlatformInfo();
        getUpdateInfo();
        getPersonalInfo();
        notifyRedCount();
    }

    /**
     * 判断是否为内部客户账号
     */
    @Override
    public void isHeimaStaff() {
        cancelRequest(mIsCustomerServiceDisposable);
        mIsCustomerServiceDisposable = PersonApi.isInnerCustomerService().map(RxUtil.<InnerCustomerResBean>handleResponse())
                .subscribe(new Consumer<InnerCustomerResBean>() {
                    @Override
                    public void accept(InnerCustomerResBean data) throws Exception {
                        if (data != null) {
                            if (data.isBackendUser()) {
                                int c = data.getDealEventCount();
                                mView.showHeimaStaffItem(String.format("待处理%s条", c > 999 ? "999+" : c + ""));
                            } else {
                                mView.hideHeimaStaffItem();
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    /**
     * 通知应该显示的红点数
     */
    private void notifyRedCount() {
        int redCount = 0;
        if (mHasNewVersionFlag)
            redCount++;
        if (mIsRealNameFlag)
            redCount++;
        if (mInfoCompleteFlag)
            redCount++;
        EventBus.getDefault().post(new CommBizEvent("userInfo_homeLeftMenu_redFlagCount", String.valueOf(redCount)));
    }

    /**
     * 从assert文件中读取数据
     *
     * @return
     */
    private HomeLeftMenuBean readDataFromAssert() {
        AssetManager manager = mContext.getAssets();
        try {
            InputStream inputStream = manager.open("userinfo_home_left_menu_data.json");
            int length = inputStream.available();
            byte[] buffer = new byte[length];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer);
            Gson gson = new Gson();
            HomeLeftMenuBean bean = gson.fromJson(json, new TypeToken<HomeLeftMenuBean>() {
            }.getType());
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void getUserProfile() {
        UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        //显示用户头像
        showUserAvatar(userInfo);
        showNicknameAndUserId(userInfo);
        showInfoCompleteProgress();
        //真实姓名
        if (!UserDataUtil.isCClass(userInfo.getType())) {
            mView.updateTopMenuIcon(ModuleType.AUTHENTICATION.getValue(), Color.WHITE);
            String userName = userInfo.getName();
            if (TextUtils.isEmpty(userName)) {
                userName = "已实名";
            }
            mView.updateListMenu(ModuleType.AUTHENTICATION.getValue(), userName, null);
            mIsRealNameFlag = false;
        } else {
            mView.updateListMenu(ModuleType.AUTHENTICATION.getValue(), null, "认证");
            mIsRealNameFlag = true;
        }
        //邮箱
        String email = userInfo.getMailAddr();
        if (!TextUtils.isEmpty(email)) {
            mView.updateTopMenuIcon(ModuleType.EMAIL.getValue(), Color.WHITE);
        }
        //任务
        mView.updateTopMenuIcon(ModuleType.TASK.getValue(), Color.WHITE);

        //110-会员用户
        if (userInfo.getMemType() == 110) {
            mView.updateVipStatus("已开通");
        } else {
            mView.updateVipStatus("未开通");
        }
    }

    private void cancelRequest(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    /**
     * 获取个人中心用户相关信息，例如收藏数量，云空间
     */
    public void getStatisticData() {
        cancelRequest(mStatisticDisposable);
        mStatisticDisposable = PersonApi.getUserCenterStatistic()
                .map(RxUtil.<UserCenterStatisticBean>handleResponse())
                .subscribe(new Consumer<UserCenterStatisticBean>() {
                    @Override
                    public void accept(UserCenterStatisticBean data) throws Exception {
                        int myCollectNum = data.getMyCollect();
                        mView.updateListMenu(ModuleType.MY_COLLECT.getValue(), myCollectNum == 0 ? "共0篇" : "共" + myCollectNum + "篇", null);
                        mView.updateListMenu(ModuleType.MY_CLOUD_SPACE.getValue(), UserDataUtil.formatUserCloudSpace(data.getUserSpaceSize()), null);
                        mView.updateListMenu(ModuleType.COUPON.getValue(), data.getCouponCount() > 0 ? data.getCouponCount() + "张" : "暂无", null);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    /**
     * 获取个人中心用户的摘要信息
     */
    private void getPersonalInfo() {
        cancelRequest(mPersonalInfo);
        mPersonalInfo = CommApi.getPersonalCenter()
                .map(RxUtil.<PersonalCenterInfo>handleResponse())
                .subscribe(new Consumer<PersonalCenterInfo>() {
                    @Override
                    public void accept(PersonalCenterInfo personalCenterInfo) throws Exception {
                        PersonalCenterInfo.SignRespBean signRespBean = personalCenterInfo.getSignResp();
                        if (signRespBean != null && signRespBean.isWriteSign()) {
                            mView.updateTopMenuIcon(ModuleType.SIGHATURE_LIST.getValue(), Color.WHITE);
                        }
                        //存储个人中心摘要信息
                        UserManager userManager = UserManager.getInstance(mContext);
                        userManager.setAuthLawyer(personalCenterInfo.isLawyer());
                        UserExtendInfo userExtendInfo = userManager.getUserExtendInfo();
                        userExtendInfo.setPersonalCenterInfo(personalCenterInfo);
                        //更新资料完整进度
                        showInfoCompleteProgress();
                        notifyRedCount();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    /**
     * 获取第三方平台的认证信息
     */
    private void getUserThirdPlatformInfo() {
        UserThirdPlatformInfo userThirdPlatformInfo = UserManager.getInstance(mContext).getUserExtendInfo().getThirdPlatformInfo();
        if (userThirdPlatformInfo != null) {
            UserThirdPlatformInfo.BankInfoRespBean bankInfoRespBean = userThirdPlatformInfo.getBankInfoResp();
            if (bankInfoRespBean != null && 1 == bankInfoRespBean.getIsBinded()) {
                mView.updateTopMenuIcon(ModuleType.BANK_CARD.getValue(), Color.WHITE);
                return;
            }
        }
        cancelRequest(mThirdPlatformInfoDisposable);
        mThirdPlatformInfoDisposable = PersonApi.getUserThirdPlatformInfo()
                .map(RxUtil.<UserThirdPlatformInfo>handleResponse())
                .subscribe(new Consumer<UserThirdPlatformInfo>() {
                    @Override
                    public void accept(UserThirdPlatformInfo thirdInfo) throws Exception {
                        Logger.d("user" + thirdInfo.getBankInfoResp().toString());
                        UserThirdPlatformInfo.BankInfoRespBean bankInfoRespBean = thirdInfo.getBankInfoResp();
                        if (bankInfoRespBean != null && 1 == bankInfoRespBean.getIsBinded()) {
                            mView.updateTopMenuIcon(ModuleType.BANK_CARD.getValue(), Color.WHITE);
                        }
                        //存储绑定银行卡信息
                        UserExtendInfo extendInfo = UserManager.getInstance(mContext).getUserExtendInfo();
                        extendInfo.setThirdPlatformInfo(thirdInfo);
                        UserManager.getInstance(mContext).updateOrSaveUserExtendInfo(extendInfo);
                        //更新资料完整进度
                        showInfoCompleteProgress();
                        notifyRedCount();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    /**
     * 显示用户头像
     *
     * @param userInfo
     */
    private void showUserAvatar(UserInfo userInfo) {
        String avatarUrl = userInfo.getAvatarUrl();
        //头像
        int sex = userInfo.getSex();
        int defaultAvatarResId = UserDataUtil.getDefaultAvatarBySex(sex);
        mView.showAvatar(avatarUrl, defaultAvatarResId);
    }

    /**
     * 显示昵称和id
     *
     * @param userInfo
     */
    private void showNicknameAndUserId(UserInfo userInfo) {
        String nickname = userInfo.getNickName();
        String userShowId = "ID：" + userInfo.getShowId();
        mView.showNickname(nickname);
        mView.showUserId(userShowId);
    }

    /**
     * 显示资料完成度
     */
    private void showInfoCompleteProgress() {
        int progress = UserInfoCompleteProgressUtil.getProfileProgress(mContext);
        if (progress != 100) {
            mInfoCompleteFlag = true;
        } else {
            mInfoCompleteFlag = false;
        }
        mView.showProfileProgress(progress);
    }

    /**
     * 获取版本更新信息
     */
    private void getUpdateInfo() {
        ACache cache = ACache.get(mContext, "update");
        String appVer = SystemUtil.getCurrentAppVersionName(mContext);
        CheckVersionResBean versionResBean = (CheckVersionResBean) cache.getAsObject(appVer);
        if (versionResBean != null) {
            mHasNewVersionFlag = true;
            mView.updateListMenu(ModuleType.ABOUT_SOFT.getValue(), null, "更新");
        } else {
            mHasNewVersionFlag = false;
            String version = "版本" + SystemUtil.getCurrentAppVersionName(mContext);
            mView.updateListMenu(ModuleType.ABOUT_SOFT.getValue(), version, null);
        }
    }

    /**
     * 更新头像
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdateAvatar(UpdateAvatarEvent event) {
        mNeedRefresh = true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdateNicknameAndSex(UpdateNicknameAndSexEvent event) {
        mNeedRefresh = true;
    }

    /**
     * 更新手机号
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdateMobile(UpdateMobileEvent event) {
        mNeedRefresh = true;
    }

    /**
     * 更新收入情况
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdateIncome(UpdateIncomeEvent event) {
        mNeedRefresh = true;
    }

    /**
     * 银行卡绑定成功
     *
     * @param bindBankSuccessEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvenBindBankSuccess(BindBankSuccessEvent bindBankSuccessEvent) {
        mNeedRefresh = true;
    }

    /**
     * 微信绑定事件通知
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBindWeixin(UpdateWeixinEvent event) {
        mNeedRefresh = true;
    }

    /**
     * 邮箱更换通知
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdateEmail(UpdateEmailEvent event) {
        mNeedRefresh = true;
    }

    /**
     * 实名认证成功
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRealName(RealNameEvent event) {
        mNeedRefresh = true;
    }

    /**
     * 用户设置或者修改了手写签章
     *
     * @param commBizEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvenbusChangeSignature(CommBizEvent commBizEvent) {
        if ("Signature_changeSignature".equals(commBizEvent.key)) {
            mNeedRefresh = true;
        }
    }

    /**
     * 更新支付宝账号
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdateAliPay(UpdateAliPayEvent event) {
        mNeedRefresh = true;
    }

    /**
     * 用户更新了个人信息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventUpdateAliPay(UpdateUserInfoEvent event) {
        mNeedRefresh = true;
    }

    /**
     * VIP状态发生变化
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventVipStatusUpdated(UpdateVipEvent event) {
        UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
        //110-会员用户
        if (userInfo.getMemType() == 110) {
            mView.updateVipStatus("已开通");
        } else {
            mView.updateVipStatus("未开通");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCommBiz(CommBizEvent event) {
        if ("bind_email_succ".equals(event.key)) {              //绑定邮箱成功
            mNeedRefresh = true;
        } else if ("News_doCollectNews".equals(event.key)) {    //资讯收藏或者取消收藏
            mNeedRefresh = true;
        } else if ("check_version_event".equals(event.key)) {   //检测版本更新
            getUpdateInfo();
            notifyRedCount();
        }
    }

}
