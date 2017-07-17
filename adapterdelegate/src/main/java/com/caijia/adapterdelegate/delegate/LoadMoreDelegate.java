package com.caijia.adapterdelegate.delegate;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.caijia.adapterdelegate.ItemViewDelegate;
import com.caijia.adapterdelegate.R;
import com.caijia.adapterdelegate.helper.LoadMoreHelper;
import com.caijia.adapterdelegate.widget.LoadMoreFooterView;

import java.util.List;

/**
 * Created by cai.jia on 2017/5/12 0012
 */

public class LoadMoreDelegate
        extends ItemViewDelegate<LoadMoreDelegate.LoadMoreItem, LoadMoreDelegate.LoadMoreVH>
        implements LoadMoreHelper.OnLoadMoreListener, LoadMoreFooterView.OnRetryListener {

    private LoadMoreVH loadMoreVH;
    private OnLoadMoreDelegateListener onLoadMoreListener;

    public LoadMoreDelegate(@Nullable OnLoadMoreDelegateListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public LoadMoreVH onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_delegate_load_more, parent, false);
        return new LoadMoreVH(view);
    }

    @Override
    public void onBindViewHolder(List<?> dataSource, LoadMoreItem loadMoreItem,
                                 RecyclerView.Adapter adapter, LoadMoreVH holder, int position) {
    }

    @Override
    public boolean isForViewType(@NonNull Object item) {
        return item instanceof LoadMoreItem;
    }

    @Override
    public void onLoadMore(RecyclerView recyclerView) {
        final LoadMoreVH fLoadMoreVH = loadMoreVH;
        if (fLoadMoreVH == null) {
            return;
        }

        LoadMoreFooterView loadMoreView = fLoadMoreVH.loadMoreView;
        if (!loadMoreView.canLoadMore()) {
            return;
        }

        loadMoreView.setStatus(LoadMoreFooterView.Status.LOADING);
        if (onLoadMoreListener != null) {
            onLoadMoreListener.onLoadMore(recyclerView,loadMoreView);
        }
    }

    @Override
    public void onRetry(LoadMoreFooterView view) {
        if (onLoadMoreListener != null) {
            view.setStatus(LoadMoreFooterView.Status.LOADING);
            onLoadMoreListener.onLoadMoreClickRetry(view);
        }
    }

    @Override
    public int getSpanCount(GridLayoutManager layoutManager) {
        return layoutManager.getSpanCount();
    }

    @Override
    public void onViewAttachedToWindow(LoadMoreVH holder) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
            lp.setFullSpan(true);
        }
        this.loadMoreVH = holder;
        holder.loadMoreView.setOnRetryListener(this);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView,int itemType) {
        if (recyclerView != null) {
            LoadMoreHelper.newInstance().attachToRecyclerView(recyclerView, this);
        }
    }

    public interface OnLoadMoreDelegateListener {

        void onLoadMore(RecyclerView recyclerView, LoadMoreFooterView loadMoreView);

        void onLoadMoreClickRetry(LoadMoreFooterView loadMoreView);
    }

    static class LoadMoreVH extends RecyclerView.ViewHolder {

        LoadMoreFooterView loadMoreView;

        LoadMoreVH(View itemView) {
            super(itemView);
            loadMoreView = (LoadMoreFooterView) itemView.findViewById(R.id.load_more_view);
        }
    }

    public static class LoadMoreItem{

    }
}
