package com.hm.iou.userinfo.leftmenu;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hm.iou.base.utils.RxUtil;
import com.hm.iou.logger.Logger;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.model.SexEnum;
import com.hm.iou.sharedata.model.UserExtendInfo;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.sharedata.model.UserThirdPlatformInfo;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.api.PersonApi;
import com.hm.iou.userinfo.bean.HomeLeftMenuBean;
import com.hm.iou.userinfo.bean.UserCenterStatisticBean;
import com.hm.iou.userinfo.business.presenter.UserDataUtil;
import com.hm.iou.userinfo.util.UserInfoCompleteProgressUtil;

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

    public HomeLeftMenuPresenter(Context context, HomeLeftMenuView view) {
        this.mContext = context;
        mView = view;
    }

    public void onDestroy() {
        if (mStatisticDisposable != null && !mStatisticDisposable.isDisposed()) {
            mStatisticDisposable.dispose();
        }
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
        String realName = userInfo.getName();
        if (!TextUtils.isEmpty(realName)) {
            mView.showHaveAuthentication();
        }
        //邮箱
        String email = userInfo.getMailAddr();
        if (!TextUtils.isEmpty(email)) {
            mView.showHaveBindEmail();
        }
        //主要收入,次要收入
        String mainIncome = UserDataUtil.getIncomeNameByType(userInfo.getMainIncome());
        String secondIncome = UserDataUtil.getIncomeNameByType(userInfo.getSecondIncome());
        if (TextUtils.isEmpty(mainIncome) || TextUtils.isEmpty(secondIncome)) {
            mView.showHaveSetWork();
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
                        mView.showMyCollectCount(myCollectNum == 0 ? "共0篇" : "共" + myCollectNum + "篇");
                        mView.showCloudSpace(UserDataUtil.formatUserCloudSpace(data.getUserSpaceSize()));
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
                mView.showHaveBindBank();
                return;
            }
        }
        if (mThirdPlatformInfoDisposable != null && !mThirdPlatformInfoDisposable.isDisposed()) {
            mThirdPlatformInfoDisposable.dispose();
            mThirdPlatformInfoDisposable = null;
        }
        mThirdPlatformInfoDisposable = PersonApi.getUserThirdPlatformInfo()
                .map(RxUtil.<UserThirdPlatformInfo>handleResponse())
                .subscribe(new Consumer<UserThirdPlatformInfo>() {
                    @Override
                    public void accept(UserThirdPlatformInfo thirdInfo) throws Exception {
                        Logger.d("user" + thirdInfo.getBankInfoResp().toString());
                        UserThirdPlatformInfo.BankInfoRespBean bankInfoRespBean = thirdInfo.getBankInfoResp();
                        if (bankInfoRespBean != null && 1 == bankInfoRespBean.getIsBinded()) {
                            mView.showHaveBindBank();
                        }
                        //存储绑定银行卡信息
                        UserExtendInfo extendInfo = UserManager.getInstance(mContext).getUserExtendInfo();
                        extendInfo.setThirdPlatformInfo(thirdInfo);
                        UserManager.getInstance(mContext).updateOrSaveUserExtendInfo(extendInfo);
                        //更新进度
                        showInfoCompleteProgress();
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
        int defaultAvatarResId;
        if (sex == SexEnum.MALE.getValue()) {
            defaultAvatarResId = R.mipmap.uikit_icon_header_man;
        } else if (sex == SexEnum.FEMALE.getValue()) {
            defaultAvatarResId = R.mipmap.uikit_icon_header_wuman;
        } else {
            defaultAvatarResId = R.mipmap.uikit_icon_header_unknow;
        }
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
        mView.showProfileProgress(progress);
    }

    @Override
    public void init() {
        getUserProfile();
        //顶部模块列表
        HomeLeftMenuBean homeLeftMenuBean = readDataFromAssert();
        mView.showTopMenus(DataUtil.convertHomeModuleBeanToIModuleData(mContext, homeLeftMenuBean.getTopModules()));
        //菜单列表
        List<IListMenuItem> list = DataUtil.convertHomeModuleBeanToIMenuItem(mContext, homeLeftMenuBean.getListMenus());
        mView.showListMenus(list);
        getStatisticData();
        getUserThirdPlatformInfo();
    }

    @Override
    public void refreshData() {
        getUserProfile();
        getStatisticData();
        getUserThirdPlatformInfo();
    }
}
