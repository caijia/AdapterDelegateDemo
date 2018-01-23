package com.caijia.adapterdelegate;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v7.util.DiffUtil;

import com.caijia.adapterdelegate.callback.AdapterDelegateDiffCallback;
import com.caijia.adapterdelegate.delegate.LoadMoreDelegate;
import com.caijia.adapterdelegate.widget.LoadMoreFooterView;

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
    private LoadMoreDelegate loadMoreDelegate;
    private boolean hasNextPage = true;
    private boolean isError;

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
        loadMoreDelegate = new LoadMoreDelegate(l);
        delegateManager.addDelegate(loadMoreDelegate);
    }

    private void addLoadMoreItem() {
        if (loadMore) {
            totalList.add(loadMoreItem);
        }
    }

    public void setLoadMore(boolean loadMore) {
        this.loadMore = loadMore;
    }

    public void hasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public void loadMoreError(boolean isError) {
        this.isError = isError;
    }

    private void setLoadMoreStatus(int page, @Nullable List<?> items) {
        if (isError) {
            loadMoreDelegate.loadMoreStatus(LoadMoreFooterView.Status.ERROR);
            return;
        }

        if (page > minPage && (!hasNextPage || items == null || items.isEmpty())) {
            loadMoreDelegate.loadMoreStatus(LoadMoreFooterView.Status.THE_END);

        } else {
            loadMoreDelegate.loadMoreStatus(LoadMoreFooterView.Status.GONE);
        }
    }

    public void refreshOrLoadMoreItems(int page, @Nullable List<?> items) {
        setLoadMoreStatus(page, items);
        if (page < minPage) {
            return;
        }

        if (page == minPage) {
            updateItems(items);

        } else {
            appendItems(items);
        }
    }

    public void refreshOrLoadMoreDiffItems(int page, @Nullable List<?> items) {
        setLoadMoreStatus(page, items);
        if (page < minPage) {
            return;
        }

        if (page == minPage) {
            updateDiffItems(items);

        } else {
            appendDiffItems(items);
        }
    }

    public void updateDiffItems(@Nullable List<?> items) {
        if (totalList.isEmpty()) {
            updateItems(items);

        } else {
            List<Object> oldList = new ArrayList<>();
            oldList.addAll(totalList);

            List<Object> newList = new ArrayList<>();
            if (items != null) {
                newList.addAll(items);
            }
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

    private void appendDiffItems(@Nullable List<?> items) {
        if (totalList.isEmpty()) {
            appendItems(items);

        } else {
            List<Object> oldList = new ArrayList<>();
            oldList.addAll(totalList);

            List<Object> newList = new ArrayList<>();
            newList.addAll(totalList);
            newList.remove(loadMoreItem);
            if (items != null) {
                newList.addAll(items);
            }
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

    public void updateItems(@Nullable List<?> items) {
        if (items == null) {
            return;
        }
        totalList.clear();
        totalList.addAll(items);
        addLoadMoreItem();
        notifyDataSetChanged();
    }

    private void appendItems(@Nullable List<?> items) {
        if (items == null) {
            return;
        }
        totalList.remove(loadMoreItem);
        totalList.addAll(items);
        addLoadMoreItem();
        notifyDataSetChanged();
    }

    private void appendItem(@Nullable Object item) {
        if (item == null) {
            return;
        }
        totalList.remove(loadMoreItem);
        totalList.add(item);
        addLoadMoreItem();
        notifyDataSetChanged();
    }

    public void clearItems() {
        totalList.clear();
    }

    public void setStatusText(CharSequence loadingText, CharSequence emptyText, CharSequence errorText) {
        loadMoreDelegate.setStatusText(loadingText, emptyText, errorText);
    }

    public void setStatusTextColor(int textColor) {
        setStatusTextColor(textColor, textColor, textColor);
    }

    public void setStatusTextColor(int loadColor, int emptyColor, int errorColor) {
        loadMoreDelegate.setStatusTextColor(loadColor, emptyColor, errorColor);
    }

    public void setStatusTextSize(int textSize) {
        setStatusTextSize(textSize, textSize, textSize);
    }

    public void setStatusTextSize(int loadTextSize, int emptyTextSize, int errorTextSize) {
        loadMoreDelegate.setStatusTextSize(loadTextSize, emptyTextSize, errorTextSize);
    }

    public void setLoadMoreHeight(@Px int height) {
        loadMoreDelegate.setLoadMoreHeight(height);
    }

    public void setProgressColor(@ColorInt int color) {
        loadMoreDelegate.setProgressColor(color);
    }
}
