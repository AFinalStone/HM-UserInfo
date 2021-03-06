package com.hm.iou.userinfo.business.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.sharedata.UserManager;
import com.hm.iou.sharedata.model.SexEnum;
import com.hm.iou.sharedata.model.UserInfo;
import com.hm.iou.tools.ToastUtil;
import com.hm.iou.uikit.HMTopBarView;
import com.hm.iou.uikit.dialog.HMAlertDialog;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;
import com.hm.iou.userinfo.business.ModifyNicknameAndSexContract;
import com.hm.iou.userinfo.business.presenter.ModifyNicknameAndSexPresenter;
import com.hm.iou.userinfo.business.presenter.UserDataUtil;
import com.hm.iou.userinfo.common.HMTextWatcher;

import butterknife.BindView;

/**
 * Created by hjy on 2018/5/23.
 */

public class ModifyNicknameAndSexActivity extends BaseActivity<ModifyNicknameAndSexPresenter> implements ModifyNicknameAndSexContract.View, View.OnClickListener {


    @BindView(R2.id.topbar)
    HMTopBarView mTopBarView;
    @BindView(R2.id.tv_nickName)
    EditText mEtNickName;
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
        mEtNickName.requestFocus();
        mEtNickName.addTextChangedListener(new HMTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (TextUtils.isEmpty(charSequence)) {
                    mBtnSubmit.setEnabled(false);
                } else {
                    mBtnSubmit.setEnabled(true);
                }
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
        mEtNickName.setSelection(mEtNickName.length());
    }

    @Override
    public void showSex(String sexStr) {
        mTvSex.setText(sexStr);
    }

    @Override
    public void showModifySuccToast() {
        ToastUtil.showStatusView(this, "修改成功");
    }

    /**
     * 显示用户已经实名过
     */
    private void showUserHasAuthDialog() {
        String msg = getString(R.string.person_userNameSex_noChangeSex02);
        String ok = "知道了";
        new HMAlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton(ok)
                .create()
                .show();
    }

    /**
     * 已修改过的用户，不能再次修改
     */
    private void showCannotModifySexDialog() {
        String msg = getString(R.string.person_userNameSex_noChangeSex03);
        String ok = "知道了";
        new HMAlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton(ok)
                .create()
                .show();
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
