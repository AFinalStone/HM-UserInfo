package com.hm.iou.userinfo.business.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.model.SexEnum;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.uikit.ClearEditText;
import com.hm.iou.uikit.HMTopBarView;
import com.hm.iou.uikit.dialog.IOSAlertDialog;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;
import com.hm.iou.userinfo.business.ModifyNicknameAndSexContract;
import com.hm.iou.userinfo.business.presenter.ModifyNicknameAndSexPresenter;
import com.hm.iou.userinfo.business.presenter.UserDataUtil;
import com.jakewharton.rxbinding2.widget.RxTextView;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

/**
 * Created by hjy on 2018/5/23.
 */

public class ModifyNicknameAndSexActivity extends BaseActivity<ModifyNicknameAndSexPresenter> implements ModifyNicknameAndSexContract.View, View.OnClickListener {


    @BindView(R2.id.topbar)
    HMTopBarView mTopBarView;
    @BindView(R2.id.tv_nickName)
    ClearEditText mEtNickName;
    @BindView(R2.id.tv_sex)
    TextView mTvSex;
    @BindView(R2.id.btn_nickname_submit)
    Button mBtnSubmit;

    @Override
    protected int getLayoutId() {
        return R.layout.person_activity_nickname_sex;
    }

    @Override
    protected ModifyNicknameAndSexPresenter initPresenter() {
        return new ModifyNicknameAndSexPresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        findViewById(R.id.btn_nickname_submit).setOnClickListener(this);
        findViewById(R.id.tv_sex).setOnClickListener(this);
        mBtnSubmit.setEnabled(false);
        mPresenter.init();
        RxTextView.textChanges(mEtNickName).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence charSequence) throws Exception {
                mBtnSubmit.setEnabled(true);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_nickname_submit) {
            String nickname = mEtNickName.getText().toString();
            mPresenter.updateNicknameAndSex(nickname);
        } else if (v.getId() == R.id.tv_sex) {
            UserInfo userInfo = UserManager.getInstance(this).getUserInfo();
            if (!UserDataUtil.isCClass(userInfo.getType())) {
                //如果已实名过
                showUserHasAuthDialog();
                return;
            }
            int sex = userInfo.getSex();
            if (sex == SexEnum.MALE.getValue() || sex == SexEnum.FEMALE.getValue()) {
                showCannotModifySexDialog();
            } else {
                showSelectUserSexDialog();
            }
        }
    }

    @Override
    public void showNickname(String nickname) {
        mEtNickName.setText(nickname);
    }

    @Override
    public void showSex(String sexStr) {
        mTvSex.setText(sexStr);
    }

    /**
     * 显示用户已经实名过
     */
    private void showUserHasAuthDialog() {
        String noChangeSex02 = getString(R.string.person_userNameSex_noChangeSex02);
        String ok = "确定";
        new IOSAlertDialog
                .Builder(mContext)
                .setMessage(noChangeSex02)
                .setNegativeButton(ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * 已修改过的用户，不能再次修改
     */
    private void showCannotModifySexDialog() {
        String noChangeSex03 = getString(R.string.person_userNameSex_noChangeSex03);
        String ok = "确定";
        new IOSAlertDialog
                .Builder(mContext)
                .setMessage(noChangeSex03)
                .setNegativeButton(ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void showSelectUserSexDialog() {
        SelectUserSexDialog.OnChangeSexListener onChangeSexListener = new SelectUserSexDialog.OnChangeSexListener() {
            @Override
            public void changeSexListener(int sexEnum) {
                mBtnSubmit.setEnabled(true);
                mPresenter.selectSex(sexEnum);
            }
        };
        SelectUserSexDialog dialogSelectUserSex = SelectUserSexDialog.CreateDialog(mContext, onChangeSexListener);
        dialogSelectUserSex.show();
    }

}
