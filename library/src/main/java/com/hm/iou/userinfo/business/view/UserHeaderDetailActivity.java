package com.hm.iou.userinfo.business.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.file.FileUtil;
import com.hm.iou.base.photo.CompressPictureUtil;
import com.hm.iou.base.photo.ImageCropper;
import com.hm.iou.base.photo.PhotoUtil;
import com.hm.iou.tools.DensityUtil;
import com.hm.iou.tools.ImageLoader;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;
import com.hm.iou.userinfo.business.UserHeaderDetailContract;
import com.hm.iou.userinfo.business.presenter.UserHeaderDetailPresenter;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 用户头像
 *
 * @author AFinalStone
 * @time 2018/3/9 下午2:54
 */
public class UserHeaderDetailActivity extends BaseActivity<UserHeaderDetailPresenter> implements UserHeaderDetailContract.View {

    private static final int REQ_CODE_ALBUM = 10;
    private static final int REQ_CODE_CAMERA = 11;

    @BindView(R2.id.iv_header)
    ImageView mIvHeader;

    //裁剪控件
    private ImageCropper mImageCropper;

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
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mIvHeader.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = getResources().getDisplayMetrics().widthPixels;
        mIvHeader.setLayoutParams(params);

        mPresenter.getUserAvatar();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_CAMERA) {
            if (resultCode == RESULT_OK) {
                String path = PhotoUtil.getCameraPhotoPath();
                initImageCropper();
                mImageCropper.crop(path, 150, 150, false, "crop");
            }
        } else if (requestCode == REQ_CODE_ALBUM) {
            if (resultCode == RESULT_OK) {
                String path = PhotoUtil.getPath(this, data.getData());
                initImageCropper();
                mImageCropper.crop(path, 150, 150, false, "crop");
            }
        }
    }

    @OnClick({R2.id.tv_avatar_album, R2.id.tv_avatar_camera})
    public void onClick(View view) {
        if (view.getId() == R.id.tv_avatar_album) {
            PhotoUtil.openAlbum(this, REQ_CODE_ALBUM);
        } else if (view.getId() == R.id.tv_avatar_camera) {
            PhotoUtil.openCamera(this, REQ_CODE_CAMERA);
        }
    }

    private void initImageCropper() {
        if (mImageCropper == null) {
            int displayDeviceHeight = getResources().getDisplayMetrics().heightPixels - DensityUtil.dip2px(this, 53);
            mImageCropper = ImageCropper.Helper.with(this).setTranslucentStatusHeight(displayDeviceHeight).create();
            mImageCropper.setCallback(new ImageCropper.Callback() {
                @Override
                public void onPictureCropOut(Bitmap bitmap, String tag) {
                    File fileCrop = new File(FileUtil.getExternalCacheDirPath(mContext) + File.separator + System.currentTimeMillis() + ".jpg");
                    CompressPictureUtil.saveBitmapToTargetFile(fileCrop, bitmap, Bitmap.CompressFormat.JPEG);
                    compressPic(fileCrop.getAbsolutePath());
                }
            });
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
            mIvHeader.setImageResource(defaultAvatarResId);
        } else {
            ImageLoader.getInstance(mContext).displayImage(url, mIvHeader, defaultAvatarResId, defaultAvatarResId);
        }
    }

}
