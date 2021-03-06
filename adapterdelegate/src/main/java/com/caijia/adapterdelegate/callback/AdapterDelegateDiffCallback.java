package com.caijia.adapterdelegate.callback;

import android.text.TextUtils;

import com.caijia.adapterdelegate.ItemViewDelegate;
import com.caijia.adapterdelegate.ItemViewDelegateManager;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

public class AdapterDelegateDiffCallback extends DiffUtil.Callback {

    private List<?> oldList;
    private List<?> newList;
    private ItemViewDelegateManager delegateManager;

    public void setOldAndNewList(ItemViewDelegateManager delegateManager,
                                 List<?> oldList, List<?> newList) {
        this.delegateManager = delegateManager;
        this.oldList = oldList;
        this.newList = newList;
    }

    private boolean equals(Object oldItem, Object newItem) {
        return oldItem != null && newItem != null
                && oldItem.equals(newItem)
                && TextUtils.equals(oldItem.getClass().getCanonicalName(),
                newItem.getClass().getCanonicalName());
    }

    @Override
    public int getOldListSize() {
        return oldList == null ? 0 : oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList == null ? 0 : newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Object oldItem = oldList.get(oldItemPosition);
        Object newItem = newList.get(newItemPosition);
        ItemViewDelegate delegate = delegateManager.findItemDelegate(newItem);
        if (delegate == null) {
            return true;
        }

        if (!equals(oldItem, newItem)) {
            return false;
        }

        return !(delegate instanceof DiffItem) || ((DiffItem) delegate).areItemsTheSame(oldItem, newItem);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Object oldItem = oldList.get(oldItemPosition);
        Object newItem = newList.get(newItemPosition);
        ItemViewDelegate delegate = delegateManager.findItemDelegate(newItem);
        if (delegate == null) {
            return true;
        }

        if (!equals(oldItem, newItem)) {
            return false;
        }

        return !(delegate instanceof DiffItem) || ((DiffItem) delegate).areContentsTheSame(oldItem, newItem);
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        Object oldItem = oldList.get(oldItemPosition);
        Object newItem = newList.get(newItemPosition);
        ItemViewDelegate delegate = delegateManager.findItemDelegate(newItem);
        if (delegate == null || !(delegate instanceof DiffItem)) {
            return null;
        }

        if (!equals(oldItem, newItem)) {
            return false;
        }
        return ((DiffItem)delegate).getChangePayload(oldItem, newItem);
    }
}