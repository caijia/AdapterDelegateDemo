package com.caijia.adapterdelegate.helper;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by cai.jia on 2017/5/12 0012
 */

public class LoadMoreHelper {

    public static LoadMoreHelper newInstance() {
        return new LoadMoreHelper();
    }

    private ScrollToBottomListener scrollToBottomListener;

    public void attachToRecyclerView(RecyclerView recyclerView, OnLoadMoreListener loadMoreListener) {
        if (recyclerView == null || loadMoreListener == null) {
            return;
        }

        if (scrollToBottomListener != null) {
            recyclerView.removeOnScrollListener(scrollToBottomListener);

        }else{
            scrollToBottomListener = new ScrollToBottomListener();
        }

        scrollToBottomListener.setLoadMoreListener(loadMoreListener);
        recyclerView.addOnScrollListener(scrollToBottomListener);
    }

    private static class ScrollToBottomListener extends RecyclerView.OnScrollListener{

        private boolean scrolled;
        private OnLoadMoreListener loadMoreListener;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();

            if (scrolled && visibleItemCount > 0) {
                scrolled = false;
                boolean horizontalScroll = layoutManager.canScrollHorizontally();
                boolean verticalScroll = layoutManager.canScrollVertically();

                if (verticalScroll && !recyclerView.canScrollVertically(1)
                        || horizontalScroll && !recyclerView.canScrollHorizontally(1)) {

                    if (loadMoreListener != null) {
                        loadMoreListener.onLoadMore(recyclerView);
                    }
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (dx != 0 || dy != 0) {
                scrolled = true;
            }
        }

        private void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
            this.loadMoreListener = loadMoreListener;
        }
    }

    public interface OnLoadMoreListener{

        void onLoadMore(RecyclerView recyclerView);

    }
}
