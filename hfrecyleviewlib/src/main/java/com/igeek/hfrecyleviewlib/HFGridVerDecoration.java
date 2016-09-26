package com.igeek.hfrecyleviewlib;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.igeek.hfrecyleviewlib.utils.DensityUtils;

public class HFGridVerDecoration extends RecyclerView.ItemDecoration {

    private int gapSize;

    public HFGridVerDecoration() {
        gapSize = DensityUtils.dp2px(8);
    }

    public HFGridVerDecoration(int gapSize) {
        this.gapSize = gapSize;

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildLayoutPosition(view);

        int totalChildCount = parent.getAdapter().getItemCount();
        int spanCount = 0;
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
        } else if (parent.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) parent.getLayoutManager()).getSpanCount();
        }

        if (spanCount == 0) return;

        if (isHeadView(parent, position)) {
            //头部视图
        } else if (isFootView(parent, position, totalChildCount)) {
            //底部视图
        } else{
            //数据视图
            int left,top,right,buttom;

            left=buttom=gapSize;

            right=isLastDataCol(parent,spanCount, position)?gapSize:0;

            top=isFristDataRow(parent,spanCount,position)?gapSize:0;

            outRect.set(left,top,right,buttom);
        }
    }

    //是否为最后一列数据
    public boolean isLastDataCol(RecyclerView recyclerView, int spanCount, int position) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter instanceof BasicHFRecyAdapter) {
            int offest=((BasicHFRecyAdapter) adapter).getHeadView() != null ? 1 : 0;
            return (position + 1-offest) % spanCount == 0;
        } else {
            return (position + 1) % spanCount == 0;
        }
    }

    //是否为第一行数据
    public boolean isFristDataRow(RecyclerView recyclerView, int spanCount, int position) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter instanceof BasicHFRecyAdapter) {
            int offest=((BasicHFRecyAdapter) adapter).getHeadView() != null ? 1 : 0;
            return position >= offest && position - offest <= spanCount - 1;
        } else {
            return position <= spanCount - 1;
        }
    }

    public boolean isHeadView(RecyclerView recyclerView, int position) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter instanceof BasicHFRecyAdapter) {
            return ((BasicHFRecyAdapter) adapter).getHeadView() != null && position == 0;
        }
        return false;
    }

    public boolean isFootView(RecyclerView recyclerView, int position, int totalCount) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter instanceof BasicHFRecyAdapter) {
            return ((BasicHFRecyAdapter) adapter).getFootView() != null && position == totalCount - 1;
        }
        return false;
    }

}
