package com.hm.iou.userinfo.business.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.file.FileUtil;
import com.hm.iou.base.photo.CompressPictureUtil;
import com.hm.iou.base.photo.ImageCropper;
import com.hm.iou.router.Router;
import com.hm.iou.tools.ImageLoader;
import com.hm.iou.uikit.HMTopBarView;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;
import com.hm.iou.userinfo.business.UserHeaderDetailContract;
import com.hm.iou.userinfo.business.presenter.UserHeaderDetailPresenter;

import java.io.File;
import java.util.List;

import butterknife.BindView;

/**
 * 用户头像
 *
 * @author AFinalStone
 * @time 2018/3/9 下午2:54
 */
public class UserHeaderDetailActivity extends BaseActivity<UserHeaderDetailPresenter> implements UserHeaderDetailContract.View {

    private static final int REQ_CODE_ALBUM = 10;

    @BindView(R2.id.topbar)
    HMTopBarView mTopBar;
    @BindView(R2.id.iv_header)
    ImageView mIvHeader;

    @Override
    protected int getLayoutId() {
        return R.layout.person_activity_user_header_detail;
    }

    @Override
    protected UserHeaderDetailPresenter initPresenter() {
        return new UserHeaderDetailPresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
        mTopBar.setRightText("修改");
        mTopBar.setOnMenuClickListener(new HMTopBarView.OnTopBarMenuClickListener() {
            @Override
            public void onClickTextMenu() {
                Router.getInstance()
                        .buildWithUrl("hmiou://m.54jietiao.com/select_pic/index")
                        .withString("enable_select_max_num", "1")
                        .navigation(mContext, REQ_CODE_ALBUM);
            }

            @Override
            public void onClickImageMenu() {

            }
        });

        mPresenter.getUserAvatar();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_ALBUM) {
            if (resultCode == RESULT_OK) {
                List<String> list = data.getStringArrayListExtra("extra_result_selection_path");
                if (list != null && !list.isEmpty()) {
                    String path = list.get(0);
                    ImageCropper.Helper.with(this)
                            .setCallback(new ImageCropper.Callback() {
                                @Override
                                public void onPictureCropOut(Bitmap bitmap, String tag) {
                                    File fileCrop = new File(FileUtil.getExternalCacheDirPath(mContext) + File.separator + System.currentTimeMillis() + ".jpg");
                                    CompressPictureUtil.saveBitmapToTargetFile(fileCrop, bitmap, Bitmap.CompressFormat.JPEG);
                                    compressPic(fileCrop.getAbsolutePath());
                                }
                            })
                            .create()
                            .crop(path, 150, 150, false, "crop");
                }
            }
        }
    }

    private void compressPic(String fileUrl) {
        CompressPictureUtil.compressPic(this, fileUrl, new CompressPictureUtil.OnCompressListener() {
            public void onCompressPicSuccess(File file) {
                mPresenter.uploadFile(file);
            }
        });
    }

    @Override
    public void showUserAvatar(String url, int defaultAvatarResId) {
        if (TextUtils.isEmpty(url)) {
            mIvHeader.setImageResource(0);
        } else {
            ImageLoader.getInstance(mContext).displayImage(url, mIvHeader);
        }
    }

}
