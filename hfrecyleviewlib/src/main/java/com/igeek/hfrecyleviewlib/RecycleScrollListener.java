package com.igeek.hfrecyleviewlib;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * recycleView滚动添加刷新和加载
 */
public abstract class RecycleScrollListener extends RecyclerView.OnScrollListener {

    private int firstVisibleItemPosition;
    private int lastVisibleItemPosition;
    private int visitCount;
    private int itemCount;
    private boolean isRefresh;
    private boolean isLoading;

    public RecycleScrollListener() {
        super();
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        itemCount = recyclerView.getLayoutManager().getItemCount();
        visitCount = recyclerView.getLayoutManager().getChildCount();
        if (!isRefresh && firstVisibleItemPosition == 0) {
            isRefresh = true;
            refresh();
        } else {
            if (!isLoading && visitCount > 0 && lastVisibleItemPosition == itemCount - 1) {
                isLoading = true;
                loadMore();
            }
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager.getClass() == LinearLayoutManager.class) {
            LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
            firstVisibleItemPosition = manager.findFirstCompletelyVisibleItemPosition();
            lastVisibleItemPosition = manager.findLastVisibleItemPosition();
        } else if (layoutManager.getClass() == GridLayoutManager.class) {
            GridLayoutManager manager = (GridLayoutManager) layoutManager;
            firstVisibleItemPosition = manager.findFirstCompletelyVisibleItemPosition();
            lastVisibleItemPosition = manager.findLastVisibleItemPosition();
        } else if (layoutManager.getClass() == StaggeredGridLayoutManager.class) {
            StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) layoutManager;
            int[] positions = new int[manager.getSpanCount()];
            positions = manager.findFirstCompletelyVisibleItemPositions(positions);
//            String fristPosition=BuildIntArraysToStr(positions);
            firstVisibleItemPosition=findMinValue(positions);
            positions=manager.findLastVisibleItemPositions(positions);
//            String lastPosition=BuildIntArraysToStr(positions);
            lastVisibleItemPosition=findMaxValue(positions);
        }
    }

    public int findMinValue(int[] values) {

        int minVal = values[0];
        for (Integer value : values) {
            if (minVal > value)
                minVal = value;
        }

        return minVal;
    }

    public int findMaxValue(int[] values) {

        int maxVal = values[0];
        for (Integer value : values) {
            if (maxVal < value)
                maxVal = value;
        }

        return maxVal;
    }

    public String BuildIntArraysToStr(int[] values) {
        StringBuffer buffer = new StringBuffer("[");
        for (Integer value : values) {
            buffer.append(value + " , ");
        }

        return buffer.deleteCharAt(buffer.length()-1).append("]").toString();
    }

    public abstract void loadMore();

    public abstract void refresh();

    public boolean isRefresh() {
        return isRefresh;
    }

    public void finished() {
        this.isRefresh = false;
        this.isLoading = false;
    }

    public void refreshFinished() {
        this.isRefresh = false;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void loadFinished() {
        this.isLoading = false;
    }
}
