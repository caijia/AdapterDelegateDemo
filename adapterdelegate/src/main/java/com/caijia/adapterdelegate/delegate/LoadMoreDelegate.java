package com.caijia.adapterdelegate.delegate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
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
    private int loadColor, emptyColor, errorColor;
    private int loadTextSize, emptyTextSize, errorTextSize;
    private CharSequence loadingText, emptyText, errorText;
    private int height;
    private int progressColor;

    public LoadMoreDelegate(@Nullable OnLoadMoreDelegateListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public LoadMoreVH onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_delegate_load_more, parent, false);
        return new LoadMoreVH(view, height == 0 ? dpToPx(parent.getContext(), 46) : height);
    }

    private int dpToPx(Context context, float dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics()));
    }

    @Override
    public void onBindViewHolder(List<?> dataSource, LoadMoreItem loadMoreItem,
                                 RecyclerView.Adapter adapter, LoadMoreVH holder, int position) {
        holder.loadMoreView.setStatusText(loadingText, emptyText, errorText);
        holder.loadMoreView.setStatusTextColor(loadColor, emptyColor, errorColor);
        holder.loadMoreView.setStatusTextSize(loadTextSize, emptyTextSize, errorTextSize);
        holder.loadMoreView.setProgressColor(progressColor);
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
            onLoadMoreListener.onLoadMore(recyclerView);
        }
    }

    public void loadMoreStatus(LoadMoreFooterView.Status status) {
        if (loadMoreVH == null) {
            return;
        }
        LoadMoreFooterView loadMoreView = loadMoreVH.loadMoreView;
        loadMoreView.setStatus(status);
    }

    @Override
    public void onRetry(LoadMoreFooterView view) {
        if (onLoadMoreListener != null) {
            view.setStatus(LoadMoreFooterView.Status.LOADING);
            onLoadMoreListener.onLoadMoreClickRetry();
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
    public void onAttachedToRecyclerView(RecyclerView recyclerView, int itemType) {
        if (recyclerView != null) {
            LoadMoreHelper.newInstance().attachToRecyclerView(recyclerView, this);
        }
    }

    public void setStatusText(CharSequence loadingText, CharSequence emptyText, CharSequence errorText) {
        this.loadingText = loadingText;
        this.emptyText = emptyText;
        this.errorText = errorText;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }

    public void setStatusTextColor(int textColor) {
        setStatusTextColor(textColor, textColor, textColor);
    }

    public void setStatusTextColor(int loadColor, int emptyColor, int errorColor) {
        this.loadColor = loadColor;
        this.emptyColor = emptyColor;
        this.errorColor = errorColor;
    }

    public void setStatusTextSize(int textSize) {
        setStatusTextSize(textSize, textSize, textSize);
    }

    public void setStatusTextSize(int loadTextSize, int emptyTextSize, int errorTextSize) {
        this.loadTextSize = loadTextSize;
        this.emptyTextSize = emptyTextSize;
        this.errorTextSize = errorTextSize;
    }

    public void setLoadMoreHeight(@Px int height) {
        this.height = height;
    }

    public interface OnLoadMoreDelegateListener {

        void onLoadMore(RecyclerView recyclerView);

        void onLoadMoreClickRetry();
    }

    static class LoadMoreVH extends RecyclerView.ViewHolder {

        LoadMoreFooterView loadMoreView;

        LoadMoreVH(View itemView, int height) {
            super(itemView);
            loadMoreView = itemView.findViewById(R.id.load_more_view);
            itemView.getLayoutParams().height = height;
        }
    }

    public static class LoadMoreItem {

    }
}
