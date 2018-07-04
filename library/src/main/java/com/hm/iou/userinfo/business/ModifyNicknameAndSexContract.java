package com.hm.iou.userinfo.business;

import com.hm.iou.base.mvp.BaseContract;

/**
 * Created by hjy on 2018/5/23.
 */

public class ModifyNicknameAndSexContract {

    public interface View extends BaseContract.BaseView {

        void showNickname(String nickname);

        void showSex(String sexStr);
    }

    public interface Presenter extends BaseContract.BasePresenter {

        void init();

        void selectSex(int sex);

        void updateNicknameAndSex(String nickname);

    }

}
