package com.github.adapterdelegatedemo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.adapterdelegatedemo.adapterDelegate.ItemViewDelegate;

import java.util.List;

/**
 * Created by cai.jia on 2017/5/12 0012
 */

public class ImageDelegate extends ItemViewDelegate<ImageObj,ImageDelegate.TextVH> {

    @Override
    public TextVH onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_multi_type_text, parent, false);
        System.out.println("ImageDelegate");
        return new TextVH(view);
    }

    @Override
    public void onBindViewHolder(List<?> dataSource, ImageObj textObj,
                                 RecyclerView.Adapter adapter, TextVH holder, int position) {
        holder.textView.setText("ImageDelegate "+textObj.getText());
    }

    @Override
    public boolean isForViewType(@NonNull Object obj) {
        return obj instanceof ImageObj;
    }

    @Override
    public int createCacheViewHolderCount() {
        return 6;
    }

    @Override
    public int maxRecycledViews() {
        return 6;
    }

    static class TextVH extends RecyclerView.ViewHolder{

        TextView textView;

        public TextVH(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }
}
