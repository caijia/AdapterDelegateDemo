package com.caijia.adapterdelegate.delegate;

import android.view.ViewGroup;

import com.caijia.adapterdelegate.ItemViewDelegate;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


/**
 * 如果加入头和尾时,有必要继承这个类
 * Created by cai.jia on 2017/5/16 0016
 */

public abstract class FullSpanItemDelegate<T, VH extends RecyclerView.ViewHolder>
        extends ItemViewDelegate<T, VH> {

    @Override
    public int getSpanCount(GridLayoutManager layoutManager) {
        return layoutManager.getSpanCount();
    }

    @Override
    public void onViewAttachedToWindow(VH holder) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
            lp.setFullSpan(true);
        }
    }
}
