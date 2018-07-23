package com.hm.iou.userinfo.business.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.hm.iou.sharedata.model.SexEnum;
import com.hm.iou.userinfo.R;


/**
 * @author AFinalStone
 * @time 2018/1/26 下午8:25
 */
public class SelectUserSexDialog extends Dialog {

    private boolean mCheckBoxMan = false;
    private boolean mCheckBoxWuMan = false;

    private SelectUserSexDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }


    public static SelectUserSexDialog CreateDialog(Context context, final OnChangeSexListener onChangeSexListener) {

        final SelectUserSexDialog mDialog = new SelectUserSexDialog(context, R.style.UikitAlertDialogStyle_FromBottom);
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.person_dialog_select_user_sex, null);
        final ImageView mCheckBoxMan = view.findViewById(R.id.mCheckBox_man);
        final ImageView mCheckBoxWuMan = view.findViewById(R.id.mCheckBox_wuMan);
        view.findViewById(R.id.linearLayout_man).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialog.mCheckBoxMan) {
                    mDialog.mCheckBoxMan = false;
                    mCheckBoxMan.setImageResource(R.mipmap.uikit_icon_check_default);
                } else {
                    mDialog.mCheckBoxMan = true;
                    mCheckBoxMan.setImageResource(R.mipmap.uikit_icon_check_green);
                    mDialog.mCheckBoxWuMan = false;
                    mCheckBoxWuMan.setImageResource(R.mipmap.uikit_icon_check_default);
                }
            }
        });
        view.findViewById(R.id.linearLayout_wuMan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialog.mCheckBoxWuMan) {
                    mDialog.mCheckBoxWuMan = false;
                    mCheckBoxWuMan.setImageResource(R.mipmap.uikit_icon_check_default);
                } else {
                    mDialog.mCheckBoxWuMan = true;
                    mCheckBoxWuMan.setImageResource(R.mipmap.uikit_icon_check_green);
                    mDialog.mCheckBoxMan = false;
                    mCheckBoxMan.setImageResource(R.mipmap.uikit_icon_check_default);
                }
            }
        });

        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if (mDialog.mCheckBoxMan) {
                    onChangeSexListener.changeSexListener(SexEnum.MALE.getValue());
                } else if (mDialog.mCheckBoxWuMan) {
                    onChangeSexListener.changeSexListener(SexEnum.FEMALE.getValue());
                }
            }
        });
        // 定义Dialog布局和参数
        mDialog.setContentView(view);
        Window dialogWindow = mDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        //获取设备的宽度
        WindowManager windowManager = dialogWindow.getWindowManager();
        Display mDisplay = windowManager.getDefaultDisplay();
        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(mDisplay.getWidth());
        mDialog.setCanceledOnTouchOutside(true);
        return mDialog;
    }

    public interface OnChangeSexListener {
        void changeSexListener(int sexEnum);
    }
}
