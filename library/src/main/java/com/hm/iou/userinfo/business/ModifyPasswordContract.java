package com.hm.iou.userinfo.business;

import com.hm.iou.base.mvp.BaseContract;

/**
 * Created by hjy on 2018/5/23.
 */

public interface ModifyPasswordContract {

    interface View extends BaseContract.BaseView {

    }

    interface Presenter extends BaseContract.BasePresenter {

        void modifyPwd(String oldPwd, String newPwd);

    }

}
