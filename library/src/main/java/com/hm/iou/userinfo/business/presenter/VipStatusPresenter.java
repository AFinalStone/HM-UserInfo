//package com.hm.iou.userinfo.business.presenter;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//
//import com.hm.iou.base.mvp.MvpActivityPresenter;
//import com.hm.iou.base.utils.CommSubscriber;
//import com.hm.iou.base.utils.RxUtil;
//import com.hm.iou.sharedata.UserManager;
//import com.hm.iou.sharedata.model.BaseResponse;
//import com.hm.iou.sharedata.model.UserInfo;
//import com.hm.iou.tools.MoneyFormatUtil;
//import com.hm.iou.userinfo.NavigationHelper;
//import com.hm.iou.userinfo.api.PersonApi;
//import com.hm.iou.userinfo.bean.MemberBean;
//import com.hm.iou.userinfo.bean.PayPackageResBean;
//import com.hm.iou.userinfo.bean.req.GetPayPackageListReqBean;
//import com.hm.iou.userinfo.business.VipStatusContract;
//import com.hm.iou.userinfo.event.UpdateVipEvent;
//import com.trello.rxlifecycle2.android.ActivityEvent;
//
//import org.greenrobot.eventbus.EventBus;
//
//import java.util.List;
//
//import static com.hm.iou.userinfo.business.view.VipStatusActivity.REQ_CODE_PAY;
//
//public class VipStatusPresenter extends MvpActivityPresenter<VipStatusContract.View> implements VipStatusContract.Presenter {
//
//    public VipStatusPresenter(@NonNull Context context, @NonNull VipStatusContract.View view) {
//        super(context, view);
//    }
//
//    @Override
//    public void init() {
//        UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
//        showUserAvatar(userInfo);
//        getMemberInfo();
//    }
//
//    /**
//     * 显示用户头像
//     *
//     * @param userInfo
//     */
//    private void showUserAvatar(UserInfo userInfo) {
//        String avatarUrl = userInfo.getAvatarUrl();
//        //头像
//        int sex = userInfo.getSex();
//        int defaultAvatarResId = UserDataUtil.getDefaultAvatarBySex(sex);
//        mView.showAvatar(avatarUrl, defaultAvatarResId);
//    }
//
//    @Override
//    public void getMemberInfo() {
//        mView.showDataLoading(true);
//        PersonApi.getMemberInfo()
//                .compose(getProvider().<BaseResponse<MemberBean>>bindUntilEvent(ActivityEvent.DESTROY))
//                .map(RxUtil.<MemberBean>handleResponse())
//                .subscribeWith(new CommSubscriber<MemberBean>(mView) {
//                    @Override
//                    public void handleResult(MemberBean memberBean) {
//                        mView.showDataLoading(false);
//                        if (memberBean.getMemType() == 110) {   //如果是VIP
//                            mView.showVipUserInfoView();
//
//                            String endDate = memberBean.getEndDate();
//                            if (endDate != null && endDate.length() >= 10) {
//                                String y = endDate.substring(0, 4);
//                                String m = endDate.substring(5, 7);
//                                String d = endDate.substring(8, 10);
//                                String validDate = String.format("VIP会员有效期至：%s年%s月%s日", y, m, d);
//                                mView.showVipValidDate(validDate);
//                            }
//
//                            //更新用户VIP信息
//                            UserInfo userInfo = UserManager.getInstance(mContext).getUserInfo();
//                            userInfo.setMemType(memberBean.getMemType());
//                            UserManager.getInstance(mContext).updateOrSaveUserInfo(userInfo);
//
//                            EventBus.getDefault().post(new UpdateVipEvent());
//
//                        } else {
//                            mView.showCommUserInfoView();
//                        }
//                    }
//
//                    @Override
//                    public void handleException(Throwable throwable, String s, String s1) {
//                        mView.showDataLoadError();
//                    }
//                });
//    }
//
//    @Override
//    public void getPayInfo() {
//        mView.showLoadingView();
//        GetPayPackageListReqBean reqBean = new GetPayPackageListReqBean();
//        reqBean.setChannel(1);
//        reqBean.setScene(3);
//        PersonApi.getPackageList(reqBean)
//                .compose(getProvider().<BaseResponse<PayPackageResBean>>bindUntilEvent(ActivityEvent.DESTROY))
//                .map(RxUtil.<PayPackageResBean>handleResponse())
//                .subscribeWith(new CommSubscriber<PayPackageResBean>(mView) {
//                    @Override
//                    public void handleResult(PayPackageResBean payPackageResBean) {
//                        mView.dismissLoadingView();
//                        List<PayPackageResBean.PackageRespListBean> list = payPackageResBean.getPackageRespList();
//                        if (list != null || !list.isEmpty()) {
//                            PayPackageResBean.PackageRespListBean data = list.get(0);
//                            String payMoney = null;
//                            try {
//                                payMoney = MoneyFormatUtil.changeF2Y(data.getActualPrice());
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                mView.toastMessage("发生异常，请稍后再试");
//                                return;
//                            }
//                            NavigationHelper.toPayVipPage(mContext, payMoney, data.getPackageId() + "", REQ_CODE_PAY);
//                        }
//                    }
//
//                    @Override
//                    public void handleException(Throwable throwable, String s, String s1) {
//                        mView.dismissLoadingView();
//                    }
//                });
//    }
//}