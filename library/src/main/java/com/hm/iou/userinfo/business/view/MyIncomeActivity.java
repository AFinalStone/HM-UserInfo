package com.hm.iou.userinfo.business.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.photo.CompressPictureUtil;
import com.hm.iou.base.photo.PhotoUtil;
import com.hm.iou.base.photo.SelectPicDialog;
import com.hm.iou.sharedata.model.IncomeEnum;
import com.hm.iou.tools.ImageLoader;
import com.hm.iou.uikit.HMTopBarView;
import com.hm.iou.uikit.dialog.IOSActionSheetItem;
import com.hm.iou.uikit.dialog.IOSActionSheetTitleDialog;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;
import com.hm.iou.userinfo.bean.BitmapAndFileIdBean;
import com.hm.iou.userinfo.business.MyIncomeContract;
import com.hm.iou.userinfo.business.presenter.MyIncomePresenter;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *
 */
public class MyIncomeActivity extends BaseActivity<MyIncomePresenter> implements MyIncomeContract.View {

    private static final int REQ_CODE_ALBUM = 10;
    private static final int REQ_CODE_CAMERA = 11;

    @BindView(R2.id.topbar)
    HMTopBarView mTopbarView;
    @BindView(R2.id.tv_income_main)
    TextView mTvIncomeMain;
    @BindView(R2.id.tv_income_second)
    TextView mTvIncomeSecond;
    @BindView(R2.id.rv_income)
    RecyclerView mRecyclerView;

    private IOSActionSheetTitleDialog mDialog;
    private boolean mSelectMainIncome;

    private ProveDocAdapter mProveDocAdapter;
    private List<BitmapAndFileIdBean> mProveDocList = new ArrayList<>();
    private int mReselectIndex = -1;

    private boolean mProveFileChanged;

    @Override
    protected int getLayoutId() {
        return R.layout.person_activity_my_income;
    }

    @Override
    protected MyIncomePresenter initPresenter() {
        return new MyIncomePresenter(this, this);
    }

    @Override
    protected void initEventAndData(Bundle bundle) {
        mTopbarView.setOnBackClickListener(new HMTopBarView.OnTopBarBackClickListener() {
            @Override
            public void onClickBack() {
                onBackPressed();
            }
        });
        mPresenter.getIncomeInfo();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mProveDocAdapter = new ProveDocAdapter();
        mRecyclerView.setAdapter(mProveDocAdapter);
        mProveDocList.add(null);
        mProveDocAdapter.setNewData(mProveDocList);
        mProveDocAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                final BitmapAndFileIdBean data = (BitmapAndFileIdBean) view.getTag();
                if (data == null) {
                    mReselectIndex = -1;
                    PhotoUtil.showSelectDialog(MyIncomeActivity.this, REQ_CODE_CAMERA, REQ_CODE_ALBUM);
                } else {
                    SelectPicDialog.createDialog(MyIncomeActivity.this, data.getFileUrl(), new SelectPicDialog.OnSelectListener() {
                        @Override
                        public void onDelete() {
                            mProveDocList.remove(position);
                            if (mProveDocList.size() == 0) {
                                //为了显示"添加"，增加一个空的数据
                                mProveDocList.add( null);
                            } else if (mProveDocList.size() > 0 && mProveDocList.get(mProveDocList.size() - 1) != null) {
                                //为了显示"添加"，增加一个空的数据
                                mProveDocList.add(null);
                            }
                            mProveDocAdapter.setNewData(mProveDocList);
                            mProveFileChanged = true;
                        }

                        @Override
                        public void onReSelect() {
                            mReselectIndex = position;
                            PhotoUtil.showSelectDialog(MyIncomeActivity.this, REQ_CODE_CAMERA, REQ_CODE_ALBUM);
                        }
                    }).show();
                }
            }
        });

        mPresenter.getProveDocList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_CAMERA) {
            if (resultCode == RESULT_OK) {
                String path = PhotoUtil.getCameraPhotoPath();
                compressPic(path);
            }
        } else if (requestCode == REQ_CODE_ALBUM) {
            if (resultCode == RESULT_OK) {
                String path = PhotoUtil.getPath(this, data.getData());
                compressPic(path);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mProveFileChanged) {
            JSONArray attachFileList = new JSONArray();
            for (BitmapAndFileIdBean item : mProveDocList) {
                if (item != null) {
                    attachFileList.put(item.getFileId());
                }
            }
            mPresenter.updateProveDocList(attachFileList);
            return;
        }
        finish();
    }

    @OnClick(value = {R2.id.ll_income_main, R2.id.ll_income_second})
    void onClick(View v) {
        if (v.getId() == R.id.ll_income_main) {
            mSelectMainIncome = true;
            showDialogSelectIncome();
        } else if (v.getId() == R.id.ll_income_second) {
            mSelectMainIncome = false;
            showDialogSelectIncome();
        }
    }

    @Override
    public void showMainIncome(String mainIncome) {
        mTvIncomeMain.setText(mainIncome);
    }

    @Override
    public void showSecondIncome(String secondIncome) {
        mTvIncomeSecond.setText(secondIncome);
    }

    @Override
    public void showProveDocList(List<BitmapAndFileIdBean> list) {
        mProveDocList.clear();
        if (list != null) {
            mProveDocList.addAll(list);
        }
        if (mProveDocList.size() < 3) {
            mProveDocList.add(null);
        }
        mProveDocAdapter.setNewData(mProveDocList);
    }

    @Override
    public void addNewProveImage(BitmapAndFileIdBean data) {
        if (mReselectIndex < 0) {
            //新增加
            if (!mProveDocList.isEmpty() && mProveDocList.get(mProveDocList.size() - 1) == null) {
                mProveDocList.remove(mProveDocList.size() - 1);
            }
            mProveDocList.add(data);
            if (mProveDocList.size() < 3) {
                mProveDocList.add(null);
            }
            mProveDocAdapter.setNewData(mProveDocList);
        } else {
            //重选
            if (mReselectIndex < mProveDocList.size()) {
                mProveDocList.remove(mReselectIndex);
                mProveDocList.add(mReselectIndex, data);
                mProveDocAdapter.setNewData(mProveDocList);
            }
        }
        mProveFileChanged = true;
    }

    private void showDialogSelectIncome() {
        if (mDialog == null) {
            String menu01 = getString(R.string.person_myIncome_nothing);
            String menu02 = getString(R.string.person_myIncome_salary);
            String menu03 = getString(R.string.person_myIncome_business);
            String menu04 = getString(R.string.person_myIncome_invest);
            String menu05 = getString(R.string.person_myIncome_ParentsSupport);
            String menu06 = getString(R.string.person_myIncome_other);
            mDialog = new IOSActionSheetTitleDialog.Builder(mContext)
                    .addSheetItem(IOSActionSheetItem.create(menu01).setItemClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setIncome(menu01, IncomeEnum.None);
                            dialog.dismiss();
                        }
                    }))
                    .addSheetItem(IOSActionSheetItem.create(menu02).setItemClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setIncome(menu02, IncomeEnum.Wages);
                            dialog.dismiss();
                        }
                    }))
                    .addSheetItem(IOSActionSheetItem.create(menu03).setItemClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setIncome(menu03, IncomeEnum.Business);
                            dialog.dismiss();
                        }
                    }))
                    .addSheetItem(IOSActionSheetItem.create(menu04).setItemClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setIncome(menu04, IncomeEnum.Investment);
                            dialog.dismiss();
                        }
                    }))
                    .addSheetItem(IOSActionSheetItem.create(menu05).setItemClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setIncome(menu05, IncomeEnum.Parents);
                            dialog.dismiss();
                        }
                    }))
                    .addSheetItem(IOSActionSheetItem.create(menu06).setItemClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setIncome(menu06, IncomeEnum.Else);
                            dialog.dismiss();
                        }
                    }))
                    .show();
        } else {
            mDialog.show();
        }
    }

    private void setIncome(String desc, IncomeEnum incomeEnum) {
        mPresenter.updateUserIncome(incomeEnum, mSelectMainIncome);
    }

    private void compressPic(String fileUrl) {
        CompressPictureUtil.compressPic(this, fileUrl, new CompressPictureUtil.OnCompressListener() {
            public void onCompressPicSuccess(File file) {
                mPresenter.uploadImage(file);
            }
        });
    }

    class ProveDocAdapter extends BaseQuickAdapter<BitmapAndFileIdBean, BaseViewHolder> {

        public ProveDocAdapter() {
            super(R.layout.person_item_income_img);
        }

        @Override
        protected void convert(BaseViewHolder helper, BitmapAndFileIdBean item) {
            ImageView iv = helper.getView(R.id.iv_income_prove);
            if (item == null) {
                iv.setImageResource(R.mipmap.person_ic_add_photo);
            } else {
                ImageLoader.getInstance(mContext).displayImage(item.getFileUrl(), iv,
                        R.drawable.uikit_bg_pic_loading_place, R.drawable.uikit_bg_pic_loading_error);
            }
            iv.setTag(item);
            helper.addOnClickListener(R.id.iv_income_prove);
        }
    }

}