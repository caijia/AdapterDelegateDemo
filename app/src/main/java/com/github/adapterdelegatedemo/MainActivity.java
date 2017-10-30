package com.github.adapterdelegatedemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.caijia.adapterdelegate.LoadMoreDelegationAdapter;
import com.caijia.adapterdelegate.delegate.LoadMoreDelegate;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoadMoreDelegate.OnLoadMoreDelegateListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private LoadMoreDelegationAdapter mAdapter;
    private int page = 1;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(this);

        mAdapter = new LoadMoreDelegationAdapter(true, this);
        mAdapter.delegateManager.addDelegate(new TextViewDelegate());
        mAdapter.delegateManager.addDelegate(new ImageDelegate());

        recyclerView.setItemAnimator(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.RecycledViewPool pool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(pool);

        recyclerView.setAdapter(mAdapter);

        //设置数据
        mAdapter.refreshOrLoadMoreDiffItems(page, getItems("Item", 15));
    }

    /**
     * 加载更多
     *
     * @param recyclerView
     */
    @Override
    public void onLoadMore(RecyclerView recyclerView) {
        if (mAdapter == null) {
            return;
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ++page;
                mAdapter.hasNextPage(hasNext());
                mAdapter.refreshOrLoadMoreItems(page, getItems("loadMore Item " + page + "-", 15));
            }
        }, 2000);
    }

    /**
     * 加载更多失败后,点击重试
     */
    @Override
    public void onLoadMoreClickRetry() {

    }

    private boolean hasNext() {
        return page < 4;
    }

    private List<Object> getItems(String s, int count) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(new TextObj(s + (i + 1)));
        }
        for (int i = 0; i < 5; i++) {
            list.add(new ImageObj(s + (i + 1)));
        }
        return list;
    }

    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                page = 1;
                mAdapter.refreshOrLoadMoreDiffItems(page, getItems("refresh Item " + page + "-", 15));
                refreshLayout.setRefreshing(false);
            }
        }, 2000);
    }
}
