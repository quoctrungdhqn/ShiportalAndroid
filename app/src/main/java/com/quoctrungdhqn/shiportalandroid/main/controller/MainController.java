package com.quoctrungdhqn.shiportalandroid.main.controller;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quoctrungdhqn.shiportalandroid.R;
import com.quoctrungdhqn.shiportalandroid.base.BaseController;
import com.quoctrungdhqn.shiportalandroid.data.response.UserDetailResponse;
import com.quoctrungdhqn.shiportalandroid.main.adapter.MainAdapter;
import com.quoctrungdhqn.shiportalandroid.main.presenter.MainControllerContract;
import com.quoctrungdhqn.shiportalandroid.main.presenter.MainControllerPresenter;
import com.quoctrungdhqn.shiportalandroid.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainController extends BaseController implements MainControllerContract.View {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    private MainControllerContract.Presenter mPresenter;
    private int nextPage = 1;
    private boolean isFinalPage;
    private MainAdapter mMainAdapter;
    private List<UserDetailResponse> mUsers = null;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.controller_home, container, false);
        ButterKnife.bind(this, view);

        mPresenter = new MainControllerPresenter(this, getActivity());

        mUsers = new ArrayList<>();

        initViews();

        return view;
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
    }

    private void initViews() {
        setRecyclerViewAdapter();
        setLoadMore();
        refreshData();
    }

    private void setRecyclerViewAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), linearLayoutManager.getOrientation()));
        mMainAdapter = new MainAdapter(getActivity(), mUsers, mRecyclerView);
        mRecyclerView.setAdapter(mMainAdapter);

        // Set up load more
        mMainAdapter.setLoadMoreListener(() -> {
            mRecyclerView.post(() -> {
                mUsers.add(null);
                mMainAdapter.notifyItemInserted(mUsers.size() - 1);
            });
            mPresenter.getUserList(nextPage);
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
        }
    }

    private void hideLoadMore() {
        if (mUsers.size() > 0 && mUsers.get(mUsers.size() - 1) == null) {
            mUsers.remove(mUsers.size() - 1);
            mMainAdapter.notifyItemRemoved(mUsers.size());
        }
    }

    private void refreshData() {
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
    }

    @Override
    public void showError(String message) {
        DialogUtils.showBasicDialog(getActivity(), null, message, (dialog, which) -> dialog.dismiss());
    }
}
