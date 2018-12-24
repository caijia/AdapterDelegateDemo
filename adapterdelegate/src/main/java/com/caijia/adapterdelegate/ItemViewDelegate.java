package com.caijia.adapterdelegate;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by cai.jia on 2017/5/9 0009
 */

public abstract class ItemViewDelegate<Item,VH extends RecyclerView.ViewHolder> {

    public abstract VH onCreateViewHolder(LayoutInflater inflater,ViewGroup parent, int viewType);

    public abstract void onBindViewHolder(List<?> dataSource, Item item,
                                          RecyclerView.Adapter adapter, VH holder, int position);

    public void onBindViewHolder(List<?> dataSource, Item item, RecyclerView.Adapter adapter,
                                 VH holder, int position, List<Object> payloads) {
    }

    public Item getItem(List<?> dataSource, RecyclerView.Adapter adapter, VH holder, int position) {
        return dataSource == null || dataSource.isEmpty() ? null : (Item) dataSource.get(position % dataSource.size());
    }

    public abstract boolean isForViewType(@NonNull Object item);

    public void onViewRecycled(VH holder) {
    }

    public boolean onFailedToRecycleView(VH holder) {
        return false;
    }

    public void onViewAttachedToWindow(VH holder) {
    }

    public void onViewDetachedFromWindow(VH holder) {
    }

    public int createCacheViewHolderCount() {
        return 0;
    }

    public int maxRecycledViews() {
        return 5;
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView, int itemType) {
        RecyclerView.RecycledViewPool pool = recyclerView.getRecycledViewPool();
        pool.setMaxRecycledViews(itemType,maxRecycledViews());

        int cacheViewHolderCount = createCacheViewHolderCount();
        if (cacheViewHolderCount != 0) {
            if (itemType != -1) {
                for (int i = 0; i < cacheViewHolderCount; i++) {
                    RecyclerView.ViewHolder holder = recyclerView.getAdapter()
                            .createViewHolder(recyclerView, itemType);
                    pool.putRecycledView(holder);
                }
            }
        }
    }

    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
    }

    public int getSpanCount(GridLayoutManager layoutManager) {
        return 1;
    }
}
