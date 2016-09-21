package com.igeek.hfrecyleviewlib;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.igeek.hfrecyleviewlib.utils.DensityUtils;


/**
 *
 */
public class HFGridMultiTypeGapDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG=HFGridMultiTypeGapDecoration.class.getSimpleName();
    private int gapSize;
    private boolean offsetTopEnabled;

    public HFGridMultiTypeGapDecoration() {
        gapSize = DensityUtils.dp2px(8);
    }

    public HFGridMultiTypeGapDecoration(int gapSize) {
        this.gapSize = DensityUtils.dp2px(gapSize);
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
        int adapterPostion=parent.getChildAdapterPosition(view);

        int totalChildCount = parent.getAdapter().getItemCount();
        int spanCount = 0;
        int spanSize=1;

        GridLayoutManager.SpanSizeLookup sizeLookup=null;

        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager manager=(GridLayoutManager) parent.getLayoutManager();
            spanCount = manager.getSpanCount();
            sizeLookup=manager.getSpanSizeLookup();
            spanSize=sizeLookup.getSpanSize(adapterPostion);
        } else if (parent.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            new IllegalAccessError("暂时不支持此类型");
        }

        if (spanCount == 0||sizeLookup==null) return;

        if (isHeadView(parent, adapterPostion)) {
            //头部视图
            outRect.set(0,0,0,0);
        } else if (isFootView(parent, adapterPostion, totalChildCount)) {
            //底部视图
            outRect.set(0,0,0,0);
        } else{
            //数据视图
            if(spanSize==spanCount){
                //视图占满一行，不做偏移处理
                outRect.set(0,0,0,0);
            }else{

                int left,top,right,buttom;

                buttom=gapSize;

                int lastFullSpanCountPos=getLastFullSpanCountPostion(sizeLookup,spanCount,adapterPostion);

                //检查是否位于网格中的最后一列
                boolean isLastCol=isLastGridCol(spanCount, position,lastFullSpanCountPos);

                //这里这样分割主要为了让每个grid当中的item的宽度都是保持一致
                right=isLastCol?gapSize:gapSize/2;
                left=isLastCol?gapSize/2:gapSize;
                //检查是否允许网格中的第一行元素的marginTop是否允许设置值 -true标识允许
                top=isOffsetTopEnabled()&&isFristGridRow(spanCount, position,lastFullSpanCountPos)?gapSize:0;

                outRect.set(left,top,right,buttom);
            }
        }
    }

    //寻找最近一个占据spanCount列的位置
    public int getLastFullSpanCountPostion(GridLayoutManager.SpanSizeLookup sizeLookup, int spanCount,int adapterPostion){

        for(int index=adapterPostion;index>=0;index--){
            if(sizeLookup.getSpanSize(index)==spanCount)
                return index;
        }

        return -1;
    }

    //是否为最后一列数据
    public boolean isLastGridCol(int spanCount, int position,int lastFullSpanCountPos) {
        return (position-lastFullSpanCountPos)%spanCount==0;
    }

    //是否为第一行数据
    public boolean isFristGridRow(int spanCount, int position,int lastFullSpanCountPos) {
        return (position-lastFullSpanCountPos)<=spanCount;
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

    public boolean isOffsetTopEnabled() {
        return offsetTopEnabled;
    }

    public void setOffsetTopEnabled(boolean offsetTopEnabled) {
        this.offsetTopEnabled = offsetTopEnabled;
    }
}
