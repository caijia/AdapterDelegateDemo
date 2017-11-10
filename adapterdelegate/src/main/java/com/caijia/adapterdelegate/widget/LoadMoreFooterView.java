package com.caijia.adapterdelegate.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.caijia.adapterdelegate.R;

/**
 * Created by cai.jia on 2016/6/7 0007.
 */
public class LoadMoreFooterView extends FrameLayout {

    private Status mStatus;

    private View mLoadingView;

    private TextView mErrorView;

    private TextView mTheEndView;

    private TextView loadingTextTv;

    private ProgressBar progressBar;

    private OnRetryListener mOnRetryListener;

    public LoadMoreFooterView(Context context) {
        this(context, null);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_delegate_load_more, this, true);

        mLoadingView = findViewById(R.id.loadingView);
        loadingTextTv = mLoadingView.findViewById(R.id.text);
        mErrorView = findViewById(R.id.errorView);
        mTheEndView = findViewById(R.id.theEndView);
        progressBar = findViewById(R.id.progressBar);

        TypedArray a = null;
        try {
            a = context.obtainStyledAttributes(attrs, R.styleable.LoadMoreFooterView);
            String loadingText = a.getString(R.styleable.LoadMoreFooterView_fv_loading_text);
            String errorText = a.getString(R.styleable.LoadMoreFooterView_fv_error_text);
            String endText = a.getString(R.styleable.LoadMoreFooterView_fv_end_text);
            int color = a.getResourceId(R.styleable.LoadMoreFooterView_fv_lf_color, -1);

            if (!TextUtils.isEmpty(loadingText)) {
                loadingTextTv.setText(loadingText);
            }

            if (!TextUtils.isEmpty(errorText)) {
                mErrorView.setText(loadingText);
            }

            if (!TextUtils.isEmpty(endText)) {
                mTheEndView.setText(loadingText);
            }

            if (color != -1) {
                int newColor = getResources().getColor(color);
                loadingTextTv.setTextColor(newColor);
                mErrorView.setTextColor(newColor);
                mTheEndView.setTextColor(newColor);
            }
        } finally {
            if (a != null) {
                a.recycle();
            }
        }

        mErrorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnRetryListener != null) {
                    mOnRetryListener.onRetry(LoadMoreFooterView.this);
                }
            }
        });

        setStatus(Status.GONE);
        setProgressColor(mTheEndView.getCurrentTextColor());
    }

    public void setStatusText(CharSequence loadingText, CharSequence emptyText, CharSequence errorText) {
        setText(loadingTextTv, loadingText);
        setText(mTheEndView, emptyText);
        setText(mErrorView, errorText);
    }

    private void setText(TextView textView, CharSequence text) {
        if (textView == null || TextUtils.isEmpty(text)) {
            return;
        }
        textView.setText(text);
    }

    public void setStatusTextColor(int loadColor, int emptyColor, int errorColor) {
        setTextColor(loadingTextTv, loadColor);
        setTextColor(mTheEndView, emptyColor);
        setTextColor(mErrorView, errorColor);
    }

    private void setStatusTextColor(int color) {
        setStatusTextColor(color, color, color);
    }

    private void setTextColor(TextView textView, int color) {
        if (textView == null || color == 0) {
            return;
        }
        textView.setTextColor(color);
    }

    public void setStatusTextSize(int loadTextSize, int emptyTextSize, int errorTextSize) {
        setStatusTextSize(loadingTextTv, loadTextSize);
        setStatusTextSize(mTheEndView, emptyTextSize);
        setStatusTextSize(mErrorView, errorTextSize);
    }

    public void setStatusTextSize(int textSize) {
        setStatusTextSize(textSize, textSize, textSize);
    }

    private void setStatusTextSize(TextView textView, int textSize) {
        if (textView == null || textSize == 0) {
            return;
        }
        textView.setTextSize(textSize);
    }

    public void setProgressColor(int color){
        if (color == 0) {
            return;
        }
        progressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

    public void setOnRetryListener(OnRetryListener listener) {
        this.mOnRetryListener = listener;
    }

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status status) {
        if (status == null) {
            return;
        }
        this.mStatus = status;
        change();
    }

    public boolean canLoadMore() {
        return mStatus == Status.GONE || mStatus == Status.ERROR;
    }

    private void change() {
        switch (mStatus) {
            case GONE:
//                mLoadingView.setVisibility(GONE);
                mErrorView.setVisibility(GONE);
                mTheEndView.setVisibility(GONE);
                break;

            case LOADING:
                mLoadingView.setVisibility(VISIBLE);
                mErrorView.setVisibility(GONE);
                mTheEndView.setVisibility(GONE);
                break;

            case ERROR:
                mLoadingView.setVisibility(GONE);
                mErrorView.setVisibility(VISIBLE);
                mTheEndView.setVisibility(GONE);
                break;

            case THE_END:
                mLoadingView.setVisibility(GONE);
                mErrorView.setVisibility(GONE);
                mTheEndView.setVisibility(VISIBLE);
                break;
        }
    }

    public enum Status {
        GONE, LOADING, ERROR, THE_END
    }

    public interface OnRetryListener {
        void onRetry(LoadMoreFooterView view);
    }
}
