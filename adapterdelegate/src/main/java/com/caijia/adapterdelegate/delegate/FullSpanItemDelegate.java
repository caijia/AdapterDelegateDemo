package com.caijia.adapterdelegate.delegate;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import com.caijia.adapterdelegate.ItemViewDelegate;


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
