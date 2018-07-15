package com.quoctrungdhqn.shiportalandroid.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.quoctrungdhqn.shiportalandroid.R;
import com.quoctrungdhqn.shiportalandroid.data.response.UserDetailResponse;
import com.quoctrungdhqn.shiportalandroid.utils.Utils;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainAdapter extends RecyclerView.Adapter {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROGRESS = 0;
    private Context mContext;
    private List<UserDetailResponse> mDataList;

    // variable for loadmore
    private int mFirstVisibleItem = 0;
    private int mVisibleItemCount = 0;
    private int mTotalItemCount = 0;
    private boolean mLoading = false;
    private OnLoadMoreListener mOnLoadMoreListener;
    private RecyclerEventHandler recyclerEventHandler;

    public MainAdapter(Context context, List<UserDetailResponse> dataList, RecyclerView recyclerView) {
        mContext = context;
        this.mDataList = dataList;
        setupLoadMore(recyclerView);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_user, parent, false);
            vh = new MainViewHolder(view);
            return vh;
        } else if (viewType == VIEW_PROGRESS) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_loadmore, parent, false);
            vh = new ProgressViewHolder(view);
            return vh;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MainViewHolder) {
            ((MainViewHolder) holder).bindData(mDataList.get(position));
            ((MainViewHolder) holder).itemView.setOnClickListener(v -> onUserClicked(mDataList.get(position)));
        }
    }

    private void setupLoadMore(RecyclerView recyclerView) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    mTotalItemCount = linearLayoutManager.getItemCount();
                    mVisibleItemCount = linearLayoutManager.getChildCount();
                    mFirstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                    if (!mLoading && mTotalItemCount <= (mFirstVisibleItem + mVisibleItemCount)) {
                        if (mOnLoadMoreListener != null) {
                            mOnLoadMoreListener.onLoadMore();
                        }
                        mLoading = true;
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataList.get(position) == null ? VIEW_PROGRESS : VIEW_ITEM;
    }

    public void setLoadMore(boolean isFinalPage) {
        mLoading = isFinalPage;
    }

    public void setLoadMoreListener(OnLoadMoreListener onLoadmoreListener) {
        this.mOnLoadMoreListener = onLoadmoreListener;
    }

    public void clearData() {
        if (mDataList != null) {
            mDataList.clear();
            notifyDataSetChanged();
        }
    }

    public List<UserDetailResponse> getUsers() {
        return mDataList;
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.avatar)
        ImageView avatar;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvDescription)
        TextView tvDescription;

        MainViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindData(UserDetailResponse user) {
            tvName.setText(String.format(Locale.getDefault(), "%s %s", user.getBusinessName(), user.getLastName()));
            tvDescription.setText(TextUtils.isEmpty(user.getDescription()) ? "" : user.getDescription());
            String profile_pic_url = "https://secure.gravatar.com/avatar/" + Utils.md5Hex(user.getEmail());
            Glide.with(mContext).load(profile_pic_url).into(avatar);
        }
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.progressBar)
        ProgressBar progressBar;

        ProgressViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setRecyclerEventHandler(RecyclerEventHandler recyclerEventHandler) {
        this.recyclerEventHandler = recyclerEventHandler;
    }

    private void onUserClicked(UserDetailResponse user) {
        recyclerEventHandler.onClickUser(user);
    }

    public interface RecyclerEventHandler {
        void onClickUser(UserDetailResponse userDetailResponse);
    }
}