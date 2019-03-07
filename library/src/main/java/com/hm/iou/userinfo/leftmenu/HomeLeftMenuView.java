package com.hm.iou.userinfo.leftmenu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hm.iou.base.utils.RouterUtil;
import com.hm.iou.router.Router;
import com.hm.iou.tools.ImageLoader;
import com.hm.iou.tools.ViewConcurrencyUtil;
import com.hm.iou.uikit.ShapedImageView;
import com.hm.iou.userinfo.NavigationHelper;
import com.hm.iou.userinfo.R;
import com.hm.iou.userinfo.R2;

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
    @BindView(R2.id.rv_top_menu)
    RecyclerView mRvTopMenu;        //横向的菜单
    @BindView(R2.id.rv_menu)        //竖向的菜单
    RecyclerView mRvListMenu;

    private Context mContext;
    private HomeLeftMenuPresenter mPresenter;

    private ListMenuAdapter mListMenuAdapter;
    private TopMenuAdapter mTopMenuAdapter;

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

        mRvTopMenu.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
        mTopMenuAdapter = new TopMenuAdapter(mContext);
        mTopMenuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (ViewConcurrencyUtil.isFastClicks()) {
                    return;
                }
                ITopMenuItem item = (ITopMenuItem) adapter.getData().get(position);
                if (item == null) {
                    return;
                }
                String linkUrl = item.getIModuleRouter();
                RouterUtil.clickMenuLink(mContext, linkUrl);
            }
        });
        mRvTopMenu.setAdapter(mTopMenuAdapter);

        //列表菜单
        mRvListMenu.setLayoutManager(new LinearLayoutManager(mContext));
        mListMenuAdapter = new ListMenuAdapter(mContext);
        mListMenuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
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
        mRvListMenu.setAdapter(mListMenuAdapter);

        //初始化数据
        mPresenter = new HomeLeftMenuPresenter(mContext, this);
        mPresenter.init();
    }

    public void onResume() {
        mPresenter.onResume();
    }

    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

    public void refreshView() {
        mPresenter.onResume();
    }

    @OnClick({R2.id.rl_header, R2.id.tv_more_set, R2.id.tv_feedback})
    public void onClick(View view) {
        int id = view.getId();
        if (R.id.rl_header == id) {
            if (ViewConcurrencyUtil.isFastClicks()) {
                return;
            }
            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/my_profile")
                    .navigation(mContext);
        } else if (R.id.tv_more_set == id) {
            if (ViewConcurrencyUtil.isFastClicks()) {
                return;
            }
/*            Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/person/my_profile")
                    .navigation(mContext);*/
            NavigationHelper.toMoreSettingPage(mContext);
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
        } else {
            mIvFlagInfoComplete.setVisibility(GONE);
            mTvInfoCompleteProgress.setVisibility(VISIBLE);
            mTvInfoCompleteProgress.setText(progress + "%");
        }
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
    public void showTopMenus(List<ITopMenuItem> list) {
        if(list == null){
            return;
        }
        mRvTopMenu.setLayoutManager(new GridLayoutManager(mContext,list.size()));
        mTopMenuAdapter.setNewData(list);
    }

    @Override
    public void updateTopMenuIcon(String menuId, int iconColor) {
        List<ITopMenuItem> list = mTopMenuAdapter.getData();
        if (list == null)
            return;
        for (int i = 0; i < list.size(); i++) {
            final ITopMenuItem item = list.get(i);
            if (menuId.equals(item.getIModuleId())) {
                item.setIMenuColor(iconColor);
                mTopMenuAdapter.notifyItemChanged(i);
                break;
            }
        }
    }

    @Override
    public void showListMenus(List<IListMenuItem> list) {
        mListMenuAdapter.setNewData(list);
    }

    @Override
    public void updateListMenu(String menuId, String desc, String redMsg) {
        List<IListMenuItem> list = mListMenuAdapter.getData();
        if (list == null) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            final IListMenuItem item = list.get(i);
            if (menuId.equals(item.getIModuleId())) {
                item.setIMenuDesc(desc);
                item.setIMenuRedMsg(redMsg);
                mListMenuAdapter.notifyItemChanged(i);
                break;
            }
        }
    }

}