package com.hm.iou.userinfo.leftmenu;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hm.iou.base.utils.RouterUtil;
import com.hm.iou.base.utils.TraceUtil;
import com.hm.iou.router.Router;
import com.hm.iou.tools.ImageLoader;
import com.hm.iou.tools.ViewConcurrencyUtil;
import com.hm.iou.uikit.ShapedImageView;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author syl
 * @time 2019/1/10 1:37 PM
 */

public class HomeLeftMenuView extends FrameLayout implements HomeLeftMenuContract.View {

    @BindView(R2.id.iv_header)
    ShapedImageView mIvHeader;
    @BindView(R2.id.tv_userNickName)
    TextView mTvUserNickName;
    @BindView(R2.id.tv_userId)
    TextView mTvUserId;
    @BindView(R2.id.tv_info_complete_progress)
    TextView mTvInfoCompleteProgress;
    @BindView(R2.id.iv_heder_arrow)
    ImageView mIvHeaderArrow;
    @BindView(R2.id.iv_flag_info_complete)
    ImageView mIvFlagInfoComplete;
    @BindView(R2.id.ll_top_menu)
    LinearLayout mLlTopMenu;
    @BindView(R2.id.rv_menu)
    RecyclerView mRvMenu;

    private Context mContext;
    HomeLeftMenuPresenter mPresenter;
    MenuAdapter mMenuAdapter;
    private List<ITopMenuItem> mListTopMenuItem = new ArrayList<>();
    private boolean mIfRefreshData = false;//是否刷新数据
    private long mLastUpdateStatisticData;  //记录上一次刷新统计数据的时间


    public HomeLeftMenuView(@NonNull Context context) {
        super(context);
        init();
    }

    public HomeLeftMenuView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HomeLeftMenuView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mContext = getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.person_layout_home_left_menu, this, true);
        ButterKnife.bind(this, view);
        mIvHeaderArrow.setColorFilter(mContext.getResources().getColor(R.color.uikit_text_sub_content));
        //列表菜单
        mRvMenu.setLayoutManager(new LinearLayoutManager(mContext));
        mMenuAdapter = new MenuAdapter(mContext);
        mMenuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (ViewConcurrencyUtil.isFastClicks()) {
                    return;
                }
                IListMenuItem item = (IListMenuItem) adapter.getData().get(position);
                if (item == null) {
                    return;
                }
                String linkUrl = item.getIModuleRouter();
                RouterUtil.clickMenuLink(mContext, linkUrl);
            }
        });
        mRvMenu.setAdapter(mMenuAdapter);
        //初始化数据
        mPresenter = new HomeLeftMenuPresenter(mContext, this);
        mPresenter.init();
    }

    public void onResume() {
        mIfRefreshData = true;
        if (System.currentTimeMillis() - mLastUpdateStatisticData > 15000) {
            refreshView();
            mLastUpdateStatisticData = System.currentTimeMillis();
        }
    }

    /**
     * 刷新页面
     */
    public void refreshView() {
        if (mIfRefreshData) {
            mPresenter.refreshData();
            mIfRefreshData = false;
        }
    }

    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

    @OnClick({R2.id.rl_header, R2.id.tv_more_set, R2.id.tv_feedback})
    public void onClick(View view) {
        int id = view.getId();
        if (R.id.rl_header == id) {
            if (ViewConcurrencyUtil.isFastClicks()) {
                return;
            }
            TraceUtil.onEvent(mContext, "my_avatar_click");
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/my_profile")
                    .navigation(mContext);
        } else if (R.id.tv_more_set == id) {
            if (ViewConcurrencyUtil.isFastClicks()) {
                return;
            }
        } else if (R.id.tv_feedback == id) {
            if (ViewConcurrencyUtil.isFastClicks()) {
                return;
            }
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/helper_center")
                    .navigation(mContext);
        }
    }


    @Override
    public void showProfileProgress(int progress) {
        if (progress == 100) {
            mIvFlagInfoComplete.setVisibility(VISIBLE);
            mTvInfoCompleteProgress.setVisibility(GONE);
        }
        mTvInfoCompleteProgress.setText(progress + "%");
    }

    @Override
    public void showAvatar(String url, int defIconResId) {
        if (TextUtils.isEmpty(url)) {
            mIvHeader.setImageResource(defIconResId);
            return;
        }
        ImageLoader.getInstance(mContext).displayImage(url, mIvHeader, defIconResId, defIconResId);

    }

    @Override
    public void showUserId(String id) {
        mTvUserId.setText(id);
    }

    @Override
    public void showNickname(String nickname) {
        mTvUserNickName.setText(nickname);
    }

    @Override
    public void showHaveAuthentication() {
        List<IListMenuItem> list = mMenuAdapter.getData();
        for (int i = 0; i < list.size(); i++) {
            final IListMenuItem item = list.get(i);
            if (ModuleType.AUTHENTICATION.getValue().equals(item.getIModuleId())) {
                IListMenuItem newItem = new IListMenuItem() {
                    @Override
                    public String getIModuleName() {
                        return item.getIModuleName();
                    }

                    @Override
                    public String getIModuleId() {
                        return item.getIModuleId();
                    }

                    @Override
                    public String getIModuleRouter() {
                        return item.getIModuleRouter();
                    }

                    @Override
                    public String getIMenuRedMsg() {
                        return "";
                    }

                    @Override
                    public String getIMenuDesc() {
                        return "已实名";
                    }
                };
                list.set(i, newItem);
                mMenuAdapter.notifyItemChanged(i);
                break;
            }
        }
        for (int i = 0; i < mListTopMenuItem.size(); i++) {
            final ITopMenuItem item = mListTopMenuItem.get(i);
            if (ModuleType.AUTHENTICATION.getValue().equals(item.getIModuleId())) {
                ITopMenuItem newItem = new ITopMenuItem() {
                    @Override
                    public String getIModuleName() {
                        return item.getIModuleName();
                    }

                    @Override
                    public String getIModuleId() {
                        return item.getIModuleId();
                    }

                    @Override
                    public int getIModuleColor() {
                        return Color.WHITE;
                    }

                    @Override
                    public String getIModuleImage() {
                        return item.getIModuleImage();
                    }

                    @Override
                    public String getIModuleRouter() {
                        return item.getIModuleRouter();
                    }
                };
                mListTopMenuItem.set(i, newItem);
                showTopMenus(mListTopMenuItem);
                break;
            }
        }
    }

    @Override
    public void showHaveBindBank() {
        for (int i = 0; i < mListTopMenuItem.size(); i++) {
            final ITopMenuItem item = mListTopMenuItem.get(i);
            if (ModuleType.BANK_CARD.getValue().equals(item.getIModuleId())) {
                ITopMenuItem newItem = new ITopMenuItem() {
                    @Override
                    public String getIModuleName() {
                        return item.getIModuleName();
                    }

                    @Override
                    public String getIModuleId() {
                        return item.getIModuleId();
                    }

                    @Override
                    public int getIModuleColor() {
                        return Color.WHITE;
                    }

                    @Override
                    public String getIModuleImage() {
                        return item.getIModuleImage();
                    }

                    @Override
                    public String getIModuleRouter() {
                        return item.getIModuleRouter();
                    }
                };
                mListTopMenuItem.set(i, newItem);
                showTopMenus(mListTopMenuItem);
            }
        }
    }

    @Override
    public void showHaveBindEmail() {
        for (int i = 0; i < mListTopMenuItem.size(); i++) {
            final ITopMenuItem item = mListTopMenuItem.get(i);
            if (ModuleType.EMAIL.getValue().equals(item.getIModuleId())) {
                ITopMenuItem newItem = new ITopMenuItem() {
                    @Override
                    public String getIModuleName() {
                        return item.getIModuleName();
                    }

                    @Override
                    public String getIModuleId() {
                        return item.getIModuleId();
                    }

                    @Override
                    public int getIModuleColor() {
                        return Color.WHITE;
                    }

                    @Override
                    public String getIModuleImage() {
                        return item.getIModuleImage();
                    }

                    @Override
                    public String getIModuleRouter() {
                        return item.getIModuleRouter();
                    }
                };
                mListTopMenuItem.set(i, newItem);
                showTopMenus(mListTopMenuItem);
            }
        }
    }

    @Override
    public void showHaveSetWork() {
        for (int i = 0; i < mListTopMenuItem.size(); i++) {
            final ITopMenuItem item = mListTopMenuItem.get(i);
            if (ModuleType.WORK.getValue().equals(item.getIModuleId())) {
                ITopMenuItem newItem = new ITopMenuItem() {
                    @Override
                    public String getIModuleName() {
                        return item.getIModuleName();
                    }

                    @Override
                    public String getIModuleId() {
                        return item.getIModuleId();
                    }

                    @Override
                    public int getIModuleColor() {
                        return Color.WHITE;
                    }

                    @Override
                    public String getIModuleImage() {
                        return item.getIModuleImage();
                    }

                    @Override
                    public String getIModuleRouter() {
                        return item.getIModuleRouter();
                    }
                };
                mListTopMenuItem.set(i, newItem);
                showTopMenus(mListTopMenuItem);
            }
        }
    }

    @Override
    public void showMyCollectCount(final String myCollectCount) {
        List<IListMenuItem> list = mMenuAdapter.getData();
        for (int i = 0; i < list.size(); i++) {
            final IListMenuItem item = list.get(i);
            if (ModuleType.MY_COLLECT.getValue().equals(item.getIModuleId())) {
                IListMenuItem newItem = new IListMenuItem() {
                    @Override
                    public String getIModuleName() {
                        return item.getIModuleName();
                    }

                    @Override
                    public String getIModuleId() {
                        return item.getIModuleId();
                    }

                    @Override
                    public String getIModuleRouter() {
                        return item.getIModuleRouter();
                    }

                    @Override
                    public String getIMenuRedMsg() {
                        return "";
                    }

                    @Override
                    public String getIMenuDesc() {
                        return myCollectCount;
                    }
                };
                list.set(i, newItem);
                mMenuAdapter.notifyItemChanged(i);
                return;
            }
        }
    }

    @Override
    public void showCloudSpace(final String space) {
        List<IListMenuItem> list = mMenuAdapter.getData();
        for (int i = 0; i < list.size(); i++) {
            final IListMenuItem item = list.get(i);
            if (ModuleType.MY_CLOUD_SPACE.getValue().equals(item.getIModuleId())) {
                IListMenuItem newItem = new IListMenuItem() {
                    @Override
                    public String getIModuleName() {
                        return item.getIModuleName();
                    }

                    @Override
                    public String getIModuleId() {
                        return item.getIModuleId();
                    }

                    @Override
                    public String getIModuleRouter() {
                        return item.getIModuleRouter();
                    }

                    @Override
                    public String getIMenuRedMsg() {
                        return "";
                    }

                    @Override
                    public String getIMenuDesc() {
                        return space;
                    }
                };
                list.set(i, newItem);
                mMenuAdapter.notifyItemChanged(i);
                return;
            }
        }
    }

    @Override
    public void showTopMenus(List<ITopMenuItem> list) {
        mLlTopMenu.removeAllViews();
        mListTopMenuItem = list;
        if (list == null || list.isEmpty()) {
            return;
        }
        for (ITopMenuItem item : mListTopMenuItem) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.person_layout_home_left_menu_top_item, mLlTopMenu, false);
            mLlTopMenu.addView(view);
            view.setTag(item);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ITopMenuItem bean = (ITopMenuItem) v.getTag();
                    String linkUrl = bean.getIModuleRouter();
                    RouterUtil.clickMenuLink(mContext, linkUrl);
                }
            });
            TextView tvModule = view.findViewById(R.id.tv_module_name);
            tvModule.setText(item.getIModuleName());
            tvModule.setTextColor(item.getIModuleColor());
            ImageView ivModule = view.findViewById(R.id.iv_module_image);
            ivModule.setColorFilter(item.getIModuleColor());
            ImageLoader.getInstance(mContext).displayImage(item.getIModuleImage(), ivModule);
        }
    }

    @Override
    public void showListMenus(List<IListMenuItem> list) {
        mMenuAdapter.setNewData(list);
    }

}
