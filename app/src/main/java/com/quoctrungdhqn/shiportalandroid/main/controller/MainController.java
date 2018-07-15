package com.quoctrungdhqn.shiportalandroid.main.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.quoctrungdhqn.shiportalandroid.R;
import com.quoctrungdhqn.shiportalandroid.data.response.UserDetailResponse;
import com.quoctrungdhqn.shiportalandroid.detail.UserDetailActivity;
import com.quoctrungdhqn.shiportalandroid.main.adapter.MainAdapter;
import com.quoctrungdhqn.shiportalandroid.main.presenter.MainControllerContract;
import com.quoctrungdhqn.shiportalandroid.main.presenter.MainControllerPresenter;
import com.quoctrungdhqn.shiportalandroid.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainController extends Controller implements MainControllerContract.View {
    private static final String TAG = "MainController";

    private Unbinder unbinder;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    private MainControllerContract.Presenter mPresenter;

    // variable for load more
    private int nextPage = 1;
    private boolean isFinalPage;

    private MainAdapter mMainAdapter;
    private List<UserDetailResponse> mUsers = null;
    private boolean isRestoreViewState = false;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.controller_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (!isRestoreViewState) {
            Log.d(TAG, "onCreateView");
            mPresenter = new MainControllerPresenter(this, getActivity());
            initViews();
        }

        return view;
    }

    private void initViews() {
        mUsers = new ArrayList<>();
        setRecyclerViewAdapter();
        setLoadMore();
        refreshData();
        setupOnClickItem();
    }

    private void setupOnClickItem() {
        mMainAdapter.setRecyclerEventHandler(user -> {
            Intent intent = new Intent(getActivity(), UserDetailActivity.class);
            intent.putExtra(UserDetailActivity.EXTRA_NAME, user);
            startActivity(intent);
        });
    }

    private void setRecyclerViewAdapter() {
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), mLinearLayoutManager.getOrientation()));
        mMainAdapter = new MainAdapter(getActivity(), mUsers, mRecyclerView);
        mRecyclerView.setAdapter(mMainAdapter);

        // Set up load more
        mMainAdapter.setLoadMoreListener(() -> {
            mRecyclerView.post(() -> {
                mUsers.add(null);
                mMainAdapter.notifyItemInserted(mUsers.size() - 1);
            });
            if (isFinalPage) {
                hideLoadMore();
            } else {
                mPresenter.getUserList(nextPage);
            }
        });
    }

    private void setLoadMore() {
        this.isFinalPage = false;
        this.nextPage = 1;
        if (mUsers != null) {
            mUsers.clear();
            mMainAdapter.clearData();
        }
        mMainAdapter.setLoadMore(!isFinalPage);
        if (!isFinalPage) {
            isFinalPage = true;
            showLoading();
            mPresenter.getUserList(this.nextPage);
        } else {
            hideLoadMore();
        }
    }

    private void hideLoadMore() {
        if (mUsers.size() > 0 && mUsers.get(mUsers.size() - 1) == null) {
            mUsers.remove(mUsers.size() - 1);
            mMainAdapter.notifyItemRemoved(mUsers.size());
        }
    }

    private void refreshData() {
        // Fetch users page = 1
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefresh.setOnRefreshListener(this::setLoadMore);
    }

    @Override
    public void appendDataList(List<UserDetailResponse> users, int nextPage, boolean isFinalPage) {
        hideLoadMore();
        mSwipeRefresh.setRefreshing(false);
        this.nextPage = nextPage;
        this.isFinalPage = isFinalPage;

        mMainAdapter.setLoadMore(this.isFinalPage);
        if (mUsers.size() > 0) {
            if (mUsers.get(mUsers.size() - 1) == null) {
                mUsers.remove(mUsers.size() - 1);
                mMainAdapter.notifyItemRemoved(mUsers.size());
            }
        }
        mUsers.addAll(users);
        mMainAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(MainControllerContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoading() {
        mSwipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mSwipeRefresh.setRefreshing(false);
        hideLoadMore();
    }

    @Override
    public void showError(String message) {
        Utils.showBasicDialog(getActivity(), null, message, (dialog, which) -> dialog.dismiss());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    // Save view state when user rotates device
    @Override
    protected void onSaveViewState(@NonNull View view, @NonNull Bundle outState) {
        super.onSaveViewState(view, outState);
        Log.d(TAG, "onSaveViewState");
        isRestoreViewState = true;
        List<UserDetailResponse> users = mMainAdapter.getUsers();
        outState.putParcelableArrayList("PARCELABLE_USERS", (ArrayList<? extends Parcelable>) users);

        Parcelable recyclerViewState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable("PARCELABLE_RECYCLER_VIEW_STATE", recyclerViewState);

        // Save data for load more
        outState.putBoolean("IS_FINAL_PAGE", isFinalPage);
    }

    // Restore view state when user rotates device
    @Override
    protected void onRestoreViewState(@NonNull View view, @NonNull Bundle savedViewState) {
        super.onRestoreViewState(view, savedViewState);
        if (savedViewState != null) {
            Log.d(TAG, "onRestoreViewState");
            isRestoreViewState = true;
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            List<UserDetailResponse> stateUsers = savedViewState.getParcelableArrayList("PARCELABLE_USERS");
            mMainAdapter = new MainAdapter(getActivity(), stateUsers, mRecyclerView);

            Parcelable recyclerViewState = savedViewState.getParcelable("PARCELABLE_RECYCLER_VIEW_STATE");
            mRecyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);

            // Retrieve data for load more
            boolean isFinalPageStated = savedViewState.getBoolean("IS_FINAL_PAGE");

            setRecyclerViewAdapter();
            setupOnClickItem();
            refreshData();

            mMainAdapter.setLoadMore(isFinalPageStated);
        }
    }

}
