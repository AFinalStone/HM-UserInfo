package com.hm.iou.userinfo.business.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hm.iou.base.BaseActivity;
import com.hm.iou.base.photo.CompressPictureUtil;
import com.hm.iou.base.photo.SelectPicDialog;
import com.hm.iou.router.Router;
import com.hm.iou.sharedata.model.IncomeEnum;
import com.hm.iou.tools.ImageLoader;
import com.hm.iou.uikit.HMTopBarView;
import com.hm.iou.uikit.dialog.HMActionSheetDialog;
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
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 *
 */
public class MyIncomeActivity extends BaseActivity<MyIncomePresenter> implements MyIncomeContract.View {

    private static final int REQ_OPEN_SELECT_PIC = 100;

    @BindView(R2.id.topbar)
    HMTopBarView mTopbarView;
    @BindView(R2.id.tv_income_main)
    TextView mTvIncomeMain;
    @BindView(R2.id.tv_income_second)
    TextView mTvIncomeSecond;
    @BindView(R2.id.rv_income)
    RecyclerView mRecyclerView;

    private ProveDocAdapter mProveDocAdapter;
    private List<BitmapAndFileIdBean> mProveDocList = new ArrayList<>();
    private int mReselectIndex = -1;

    private boolean mProveFileChanged;

    private List<String> mIncomeList = new ArrayList<>();
    private Dialog mMainIncomeDialog;
    private Dialog mSecondIncomeDialog;

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
        String[] incomes = getResources().getStringArray(R.array.person_incomes);
        for (String income : incomes) {
            mIncomeList.add(income);
        }

        mPresenter.getIncomeInfo();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mProveDocAdapter = new ProveDocAdapter();
        mRecyclerView.setAdapter(mProveDocAdapter);
        mProveDocList.add(null);
        mProveDocAdapter.setNewData(mProveDocList);
        mProveDocAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                final BitmapAndFileIdBean data = (BitmapAndFileIdBean) view.getTag();
                if (data == null) {
                    mReselectIndex = -1;
                    List<BitmapAndFileIdBean> list = mProveDocAdapter.getData();
                    Router.getInstance()
                            .buildWithUrl("hmiou://m.54jietiao.com/select_pic/index")
                            .withString("enable_select_max_num", String.valueOf(3 + 1 - list.size()))
                            .navigation(mContext, REQ_OPEN_SELECT_PIC);
                } else {
                    SelectPicDialog.createDialog(MyIncomeActivity.this, data.getFileUrl(), new SelectPicDialog.OnSelectListener() {
                        @Override
                        public void onDelete() {
                            mProveDocList.remove(position);
                            if (mProveDocList.size() == 0) {
                                //为了显示"添加"，增加一个空的数据
                                mProveDocList.add(null);
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
                            Router.getInstance()
                                    .buildWithUrl("hmiou://m.54jietiao.com/select_pic/index")
                                    .withString("enable_select_max_num", String.valueOf(1))
                                    .navigation(mContext, REQ_OPEN_SELECT_PIC);
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
        if (REQ_OPEN_SELECT_PIC == requestCode && RESULT_OK == resultCode) {
            if (mReselectIndex == -1) {//新增
                List<String> listPaths = data.getStringArrayListExtra("extra_result_selection_path");
                Log.d("Photo", " path: " + listPaths);
                Flowable.just(listPaths)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<List<String>, List<String>>() {
                            @Override
                            public List<String> apply(List<String> list) throws Exception {
                                List<String> listFiles = new ArrayList<>();
                                for (String path : list) {
                                    listFiles.add(CompressPictureUtil.compressPic(mContext, path).getAbsolutePath());
                                }
                                return listFiles;
                            }
                        })
                        .subscribe(new Consumer<List<String>>() {
                            @Override
                            public void accept(List<String> list) throws Exception {
                                for (String path : list) {
                                    mPresenter.uploadImage(new File(path));
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                            }
                        });
            } else {//重选
                String path = data.getStringExtra("extra_result_selection_path_first");
                Log.d("Photo", "camera path: " + path);
                CompressPictureUtil.compressPic(this, path, new CompressPictureUtil.OnCompressListener() {
                    public void onCompressPicSuccess(File file) {
                        mPresenter.uploadImage(file);
                    }
                });
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
            showMainIncomeDialog();
        } else if (v.getId() == R.id.ll_income_second) {
            showSecondIncomeDialog();
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

    /**
     * 显示主要收入选择弹窗
     */
    private void showMainIncomeDialog() {
        if (mMainIncomeDialog == null) {
            mMainIncomeDialog = new HMActionSheetDialog.Builder(this)
                    .setTitle("主要收入")
                    .setActionSheetList(mIncomeList)
                    .setOnItemClickListener(new HMActionSheetDialog.OnItemClickListener() {
                        @Override
                        public void onItemClick(int i, String income) {
                            mPresenter.updateUserIncome(getIncomeByIndex(i), true);
                        }
                    })
                    .create();
        }
        mMainIncomeDialog.show();
    }

    /**
     * 显示次要收入选择弹窗
     */
    private void showSecondIncomeDialog() {
        if (mSecondIncomeDialog == null) {
            mSecondIncomeDialog = new HMActionSheetDialog.Builder(this)
                    .setTitle("次要收入")
                    .setActionSheetList(mIncomeList)
                    .setOnItemClickListener(new HMActionSheetDialog.OnItemClickListener() {
                        @Override
                        public void onItemClick(int i, String s) {
                            mPresenter.updateUserIncome(getIncomeByIndex(i), false);
                        }
                    })
                    .create();
        }
        mSecondIncomeDialog.show();
    }

    private IncomeEnum getIncomeByIndex(int index) {
        if (index == 0) {
            return IncomeEnum.None;
        } else if (index == 1) {
            return IncomeEnum.Wages;
        } else if (index == 2) {
            return IncomeEnum.Business;
        } else if (index == 3) {
            return IncomeEnum.Investment;
        } else if (index == 4) {
            return IncomeEnum.Parents;
        } else if (index == 5) {
            return IncomeEnum.Else;
        }
        return IncomeEnum.None;
    }

    class ProveDocAdapter extends BaseQuickAdapter<BitmapAndFileIdBean, BaseViewHolder> {

        public ProveDocAdapter() {
            super(R.layout.person_item_income_img);
        }

        @Override
        protected void convert(BaseViewHolder helper, BitmapAndFileIdBean item) {
            ImageView iv = helper.getView(R.id.iv_income_prove);
            if (item == null) {
                iv.setImageResource(R.mipmap.uikit_ic_circle_add);
            } else {
                ImageLoader.getInstance(mContext).displayImage(item.getFileUrl(), iv,
                        R.drawable.uikit_bg_pic_loading_place, R.drawable.uikit_bg_pic_loading_error);
            }
            iv.setTag(item);
            helper.addOnClickListener(R.id.iv_income_prove);
        }
    }

}