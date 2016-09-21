package com.igeek.hfrecyleviewlib;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.igeek.hfrecyleviewlib.utils.DensityUtils;


/**
 */
public class HFLineHerComDecoration extends RecyclerView.ItemDecoration {

    private int mHeight;
    private Paint mPaint;

    public HFLineHerComDecoration(int dp, int color) {
        mHeight = DensityUtils.dp2px(dp);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mHeight);
        mPaint.setColor(color);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        RecyclerView.Adapter adapter = parent.getAdapter();
        for (int index = 0; index < childCount; index++) {
            View child = parent.getChildAt(index);
            final int left=child.getLeft();
            final int right= left + mHeight;
            final int top=child.getTop();
            final int bottom=child.getBottom();
            boolean isDraw = true;
            if (adapter instanceof BasicHFRecyAdapter) {
                if (index == childCount - 1)
                    isDraw = ((BasicHFRecyAdapter) adapter).getFootView() == null;
            }
            if (isDraw)
                c.drawLine(left, top,right, bottom, mPaint);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int childCount = parent.getChildCount();
        RecyclerView.Adapter adapter = parent.getAdapter();
        if(adapter.getItemCount()>1&&childCount==adapter.getItemCount()){
            View child = parent.getChildAt(childCount-1);
            final int left=child.getLeft();
            final int right= left + mHeight;
            final int top=child.getTop();
            final int bottom=child.getBottom();
            boolean isDraw = true;
            if (adapter instanceof BasicHFRecyAdapter) {
                isDraw = ((BasicHFRecyAdapter) adapter).getFootView() == null;
            }
            if (isDraw)
                c.drawLine(left,top, right, bottom, mPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        final int postion=parent.getChildLayoutPosition(view);

        outRect.set(mHeight, 0, 0, 0);

        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter instanceof BasicHFRecyAdapter) {
            if( postion == adapter.getItemCount() - 1){
                if (((BasicHFRecyAdapter) adapter).getFootView() != null){
                    outRect.set(0, 0, 0, 0);
                }else{
                    outRect.set(mHeight, 0, mHeight, 0);
                }
            }
        }
    }
}
