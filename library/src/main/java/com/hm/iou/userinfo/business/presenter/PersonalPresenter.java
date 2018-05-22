package com.hm.iou.userinfo.business.presenter;


import android.content.Context;
import android.support.annotation.NonNull;

import com.hm.iou.base.mvp.MvpActivityPresenter;
import com.hm.iou.userinfo.business.contract.PersonalContract;

/**
 * Created by AFinalStone on 2017/12/12.
 */

public class PersonalPresenter extends MvpActivityPresenter<PersonalContract.View> implements PersonalContract.Presenter {


    public PersonalPresenter(@NonNull Context context, @NonNull PersonalContract.View view) {
        super(context, view);
    }


    /**
     * 根据微信code判断当前微信是否已经绑定过手机号
     *
     * @param code
     */
    private void isWXHaveBindMobile(String code) {

    }

    /**
     * 进行微信绑定
     *
     * @param wxSn 判断微信是否已经绑定过手机的交易流水号
     */
    private void bindWXAfterLogin(String wxSn) {

    }


    @Override
    public void changeLocation(String location) {

    }

    @Override
    public void canRealNameAuth(String userId, boolean isToChangeSignaturePsd) {

    }

    @Override
    public void logout() {

    }

    @Override
    public void changeNickName(String name) {

    }

    @Override
    public void changeSex(int sex) {

    }

    @Override
    public void getWxCode(Context context) {

    }


}
