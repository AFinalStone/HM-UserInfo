package com.hm.iou.userinfo.business.contract;


import android.content.Context;

import com.hm.iou.base.mvp.BaseContract;

/**
 * Created by syl on 2017年12月4日 17:23:10
 */
public interface PersonalContract {


    interface View extends BaseContract.BaseView {


    }

    interface Presenter extends BaseContract.BasePresenter {


        /**
         * 修改常住地址
         *
         * @param location
         */
        void changeLocation(String location);

        /**
         * 是否可以进行实名认证
         *
         * @param userId
         * @param isToChangeSignaturePsd
         */
        void canRealNameAuth(String userId, boolean isToChangeSignaturePsd);

        /**
         * 注销
         */
        void logout();

        /**
         * 修改用户昵称
         *
         * @param name
         */
        void changeNickName(String name);

        /**
         * 修改用户性别
         *
         * @param sex
         */
        void changeSex(int sex);

        /**
         * 获取当前手机微信的Code
         *
         * @param context
         */
        void getWxCode(Context context);

    }

}
