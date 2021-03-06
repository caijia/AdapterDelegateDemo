package com.github.adapterdelegatedemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.caijia.adapterdelegate.ItemViewDelegate;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by cai.jia on 2017/5/12 0012
 */

public class TextViewDelegate extends ItemViewDelegate<TextObj,TextViewDelegate.TextVH> {

    @Override
    public TextVH onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_multi_type_text, parent, false);
        return new TextVH(view);
    }

    @Override
    public void onBindViewHolder(List<?> dataSource, TextObj textObj,
                                 RecyclerView.Adapter adapter, TextVH holder, int position) {
        holder.textView.setText(textObj.getText());
    }

    @Override
    public boolean isForViewType(@NonNull Object obj) {
        return obj instanceof TextObj;
    }

    static class TextVH extends RecyclerView.ViewHolder{

        TextView textView;

        public TextVH(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }
}
