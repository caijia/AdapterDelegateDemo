package com.github.adapterdelegatedemo.adapterDelegate;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.github.adapterdelegatedemo.adapterDelegate.callback.AdapterDelegateDiffCallback;
import com.github.adapterdelegatedemo.adapterDelegate.delegate.LoadMoreDelegate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cai.jia on 2017/5/15 0015
 */

public class LoadMoreDelegationAdapter extends AbsDelegationAdapter {

    private static final int MIN_PAGE = 1;
    private List<Object> totalList;
    private boolean loadMore;
    private LoadMoreDelegate.LoadMoreItem loadMoreItem;
    private AdapterDelegateDiffCallback diffCallback;
    private int minPage = MIN_PAGE;

    public LoadMoreDelegationAdapter(boolean loadMore,
                                     @Nullable LoadMoreDelegate.OnLoadMoreDelegateListener l) {
        this(loadMore, MIN_PAGE, l);
    }

    public LoadMoreDelegationAdapter(boolean loadMore, int minPage,
                                     @Nullable LoadMoreDelegate.OnLoadMoreDelegateListener l) {
        init(loadMore, minPage < 0 ? MIN_PAGE : minPage, l);
    }

    public LoadMoreDelegationAdapter(boolean loadMore,
                                     @Nullable LoadMoreDelegate.OnLoadMoreDelegateListener l,
                                     @NonNull ItemViewDelegateManager delegateManager) {
        this(loadMore, MIN_PAGE, l, delegateManager);
    }

    public LoadMoreDelegationAdapter(boolean loadMore, int minPage,
                                     @Nullable LoadMoreDelegate.OnLoadMoreDelegateListener l,
                                     @NonNull ItemViewDelegateManager delegateManager) {
        super(delegateManager);
        init(loadMore, minPage < 0 ? MIN_PAGE : minPage, l);
    }

    private void init(boolean loadMore, int minPage, LoadMoreDelegate.OnLoadMoreDelegateListener l) {
        this.minPage = minPage;
        diffCallback = new AdapterDelegateDiffCallback();
        this.loadMore = loadMore;
        loadMoreItem = new LoadMoreDelegate.LoadMoreItem();
        totalList = new ArrayList<>();
        setDataSource(totalList);
        delegateManager.addDelegate(new LoadMoreDelegate(l));
    }

    private void addLoadMoreItem() {
        if (loadMore) {
            totalList.add(loadMoreItem);
        }
    }

    public void setLoadMore(boolean loadMore) {
        this.loadMore = loadMore;
    }

    public void refreshOrLoadMoreItems(int page, @NonNull List<?> items) {
        if (page < minPage) {
            return;
        }

        if (page == minPage) {
            updateItems(items);

        } else {
            appendItems(items);
        }
    }

    public void refreshOrLoadMoreDiffItems(int page, @NonNull List<?> items) {
        if (page < minPage) {
            return;
        }

        if (page == minPage) {
            updateDiffItems(items);

        } else {
            appendDiffItems(items);
        }
    }

    public void updateDiffItems(@NonNull List<?> items) {
        if (totalList.isEmpty()) {
            updateItems(items);

        } else {
            List<Object> oldList = new ArrayList<>();
            oldList.addAll(totalList);

            List<Object> newList = new ArrayList<>();
            newList.addAll(items);
            if (loadMore) {
                newList.add(loadMoreItem);
            }

            diffCallback.setOldAndNewList(delegateManager, oldList, newList);
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffCallback);
            result.dispatchUpdatesTo(this);

            totalList.clear();
            totalList.addAll(newList);
        }
    }

    public void appendDiffItems(@NonNull List<?> items) {
        if (totalList.isEmpty()) {
            appendItems(items);

        } else {
            List<Object> oldList = new ArrayList<>();
            oldList.addAll(totalList);

            List<Object> newList = new ArrayList<>();
            newList.addAll(totalList);
            newList.remove(loadMoreItem);
            newList.addAll(items);
            if (loadMore) {
                newList.add(loadMoreItem);
            }

            diffCallback.setOldAndNewList(delegateManager, oldList, newList);
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffCallback);
            result.dispatchUpdatesTo(this);

            totalList.clear();
            totalList.addAll(newList);
        }
    }

    public void updateItems(@NonNull List<?> items) {
        totalList.clear();
        totalList.addAll(items);
        addLoadMoreItem();
        notifyDataSetChanged();
    }

    public void appendItems(@NonNull List<?> items) {
        totalList.remove(loadMoreItem);
        totalList.addAll(items);
        addLoadMoreItem();
        notifyDataSetChanged();
    }

    public void appendItem(@NonNull Object item) {
        totalList.remove(loadMoreItem);
        totalList.add(item);
        addLoadMoreItem();
        notifyDataSetChanged();
    }
}
