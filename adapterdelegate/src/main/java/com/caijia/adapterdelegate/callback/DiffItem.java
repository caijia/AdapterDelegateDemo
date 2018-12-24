package com.caijia.adapterdelegate.callback;

import androidx.annotation.NonNull;

/**
 * Created by cai.jia on 2017/5/15 0015
 */

public interface DiffItem<T> {

    boolean areItemsTheSame(@NonNull T oldItem, @NonNull T newItem);

    boolean areContentsTheSame(@NonNull T oldItem, @NonNull T newItem);

    Object getChangePayload(@NonNull T oldItem, @NonNull T newItem);
}
