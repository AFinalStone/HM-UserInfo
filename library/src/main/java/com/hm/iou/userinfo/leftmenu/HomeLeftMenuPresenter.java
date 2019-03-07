package com.hm.iou.userinfo.leftmenu;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.base.version.CheckVersionResBean;
import com.hm.iou.logger.Logger;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.event.BindBankSuccessEvent;
import com.hm.iou.sharedata.event.CommBizEvent;
import com.hm.iou.sharedata.model.UserExtendInfo;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.sharedata.model.UserThirdPlatformInfo;
import com.hm.iou.tools.ACache;
import com.hm.iou.tools.SystemUtil;
import com.hm.iou.userinfo.api.PersonApi;
import com.hm.iou.userinfo.bean.HomeLeftMenuBean;
import com.hm.iou.userinfo.bean.UserCenterStatisticBean;
import com.hm.iou.userinfo.business.presenter.UserDataUtil;
import com.hm.iou.userinfo.event.UpdateAvatarEvent;
import com.hm.iou.userinfo.event.UpdateEmailEvent;
import com.hm.iou.userinfo.event.UpdateIncomeEvent;
import com.hm.iou.userinfo.event.UpdateMobileEvent;
import com.hm.iou.userinfo.event.UpdateNicknameAndSexEvent;
import com.hm.iou.userinfo.event.UpdateWeixinEvent;
import com.hm.iou.userinfo.util.UserInfoCompleteProgressUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.reactivestreams.Subscriber;

import java.io.InputStream;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author syl
 * @time 2019/1/10 4:58 PM
 */

public class HomeLeftMenuPresenter implements HomeLeftMenuContract.Presenter {

    private Context mContext;
    private HomeLeftMenuView mView;
    private Disposable mStatisticDisposable;
    private Disposable mThirdPlatformInfoDisposable;//获取第三方平台的状态
    private int mRedFlagCount;//红点数量

    private long mLastUpdateStatisticData = System.currentTimeMillis();  //记录上一次刷新统计数据的时间
    private boolean mNeedRefresh = false;

    public HomeLeftMenuPresenter(Context context, HomeLeftMenuView view) {
        this.mContext = context;
        mView = view;
        EventBus.getDefault().register(this);
    }

    public void onDestroy() {
        if (mStatisticDisposable != null && !mStatisticDisposable.isDisposed()) {
            mStatisticDisposable.dispose();
            mStatisticDisposable = null;
        }
        if (mThirdPlatformInfoDisposable != null && !mThirdPlatformInfoDisposable.isDisposed()) {
            mThirdPlatformInfoDisposable.dispose();
            mThirdPlatformInfoDisposable = null;
        }
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
        mLastUpdateStatisticData = System.currentTimeMillis();
    }

    @Override
    public void onResume() {
        if (mNeedRefresh) {
            mNeedRefresh = false;
            refreshData();
            mLastUpdateStatisticData = System.currentTimeMillis();
        } else {
            if (System.currentTimeMillis() - mLastUpdateStatisticData > 30000) {
                refreshData();
                mLastUpdateStatisticData = System.currentTimeMillis();
            }
        }
    }

    @Override
    public void refreshData() {
        getStatisticData();
        Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(FlowableEmitter<Boolean> e) throws Exception {
                mRedFlagCount = 0;
                getUserThirdPlatformInfo();
                Logger.d("获取第三方平台信息结束");
                e.onNext(true);
            }
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aVoid) throws Exception {
                        getUpdateInfo();
                        getUserProfile();
                        Logger.d("发送userInfo_homeLeftMenu_redFlagCount通知事件==" + mRedFlagCount);
                        EventBus.getDefault().post(new CommBizEvent("userInfo_homeLeftMenu_redFlagCount", String.valueOf(mRedFlagCount)));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
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
        } else {
            mView.updateListMenu(ModuleType.AUTHENTICATION.getValue(), null, "认证");
            mRedFlagCount++;
        }

        //邮箱
        String email = userInfo.getMailAddr();
        if (!TextUtils.isEmpty(email)) {
            mView.updateTopMenuIcon(ModuleType.EMAIL.getValue(), Color.WHITE);
        }
        //主要收入,次要收入
        if (userInfo.getMainIncome() > 0) {
            mView.updateTopMenuIcon(ModuleType.WORK.getValue(), Color.WHITE);
        }
    }

    /**
     * 获取个人中心用户相关信息，例如收藏数量，云空间
     */
    public void getStatisticData() {
        if (mStatisticDisposable != null && !mStatisticDisposable.isDisposed()) {
            mStatisticDisposable.dispose();
            mStatisticDisposable = null;
        }
        mStatisticDisposable = PersonApi.getUserCenterStatistic()
                .map(RxUtil.<UserCenterStatisticBean>handleResponse())
                .subscribe(new Consumer<UserCenterStatisticBean>() {
                    @Override
                    public void accept(UserCenterStatisticBean data) throws Exception {
                        int myCollectNum = data.getMyCollect();
                        mView.updateListMenu(ModuleType.MY_COLLECT.getValue(), myCollectNum == 0 ? "共0篇" : "共" + myCollectNum + "篇", null);
                        mView.updateListMenu(ModuleType.MY_CLOUD_SPACE.getValue(), UserDataUtil.formatUserCloudSpace(data.getUserSpaceSize()), null);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    /**
     * 获取第三方平台的认证信息
     * 这个方法是异步的，等这个方法获取到
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
        if (mThirdPlatformInfoDisposable != null && !mThirdPlatformInfoDisposable.isDisposed()) {
            mThirdPlatformInfoDisposable.dispose();
            mThirdPlatformInfoDisposable = null;
        }
        mThirdPlatformInfoDisposable = PersonApi.getUserThirdPlatformInfo()
                .map(RxUtil.<UserThirdPlatformInfo>handleResponse())
                .map(new Function<UserThirdPlatformInfo, Boolean>() {

                    @Override
                    public Boolean apply(UserThirdPlatformInfo thirdInfo) throws Exception {
                        //存储绑定银行卡信息
                        UserExtendInfo extendInfo = UserManager.getInstance(mContext).getUserExtendInfo();
                        extendInfo.setThirdPlatformInfo(thirdInfo);
                        UserManager.getInstance(mContext).updateOrSaveUserExtendInfo(extendInfo);
                        Logger.d("存储绑定银行卡信息");
                        UserThirdPlatformInfo.BankInfoRespBean bankInfoRespBean = thirdInfo.getBankInfoResp();
                        if (bankInfoRespBean != null && 1 == bankInfoRespBean.getIsBinded()) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean flag) throws Exception {
                        if (flag) {
                            mView.updateTopMenuIcon(ModuleType.BANK_CARD.getValue(), Color.WHITE);
                        }
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
        String userShowId = userInfo.getShowId();
        mView.showNickname(nickname);
        mView.showUserId(userShowId);
    }

    /**
     * 显示资料完成度
     */
    private void showInfoCompleteProgress() {
        int progress = UserInfoCompleteProgressUtil.getProfileProgress(mContext);
        if (progress != 100) {
            mRedFlagCount++;
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
            mRedFlagCount++;
            mView.updateListMenu(ModuleType.ABOUT_SOFT.getValue(), null, "更新");
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
    public void onEventRealName(UpdateEmailEvent event) {
        mNeedRefresh = true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCommBiz(CommBizEvent event) {
        if ("bind_email_succ".equals(event.key)) {              //绑定邮箱成功
            mNeedRefresh = true;
        } else if ("News_doCollectNews".equals(event.key)) {    //资讯收藏或者取消收藏
            mNeedRefresh = true;
        }
    }

}