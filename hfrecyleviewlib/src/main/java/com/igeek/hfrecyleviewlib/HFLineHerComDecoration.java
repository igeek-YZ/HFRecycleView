package com.igeek.hfrecyleviewlib;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.igeek.hfrecyleviewlib.utils.DensityUtils;

public class HFLineHerComDecoration extends RecyclerView.ItemDecoration {

    private int mWidth;
    private Paint mPaint;

    public HFLineHerComDecoration(int dp, int color) {
        mWidth = DensityUtils.dp2px(dp);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(0);
        mPaint.setColor(color);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        final int childCount = parent.getChildCount();
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        RecyclerView.Adapter adapter = parent.getAdapter();

        for (int index = 0; index < childCount; index++) {
            View child = parent.getChildAt(index);
            final int left=child.getLeft()- mWidth;
            final int right=left+mWidth;

            boolean isDraw = true;
            if (adapter instanceof BasicHFRecyAdapter) {
                if (index == childCount - 1)
                    isDraw = ((BasicHFRecyAdapter) adapter).getFootView() == null;
            }

            if (isDraw){
                c.drawRect(left,top,right,bottom,mPaint);
                if(index==childCount-1){
                    final int lastChildLeft=child.getRight();
                    final int lasthildRight=lastChildLeft+mWidth;
                    c.drawRect(lastChildLeft,top,lasthildRight,bottom,mPaint);
                }
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        final int postion=parent.getChildLayoutPosition(view);

        outRect.set(mWidth, 0, 0, 0);

        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter instanceof BasicHFRecyAdapter) {
            if( postion == adapter.getItemCount() - 1){
                if (((BasicHFRecyAdapter) adapter).getFootView() != null){
                    outRect.set(0, 0, 0, 0);
                }else{
                    outRect.set(mWidth, 0, mWidth, 0);
                }
            }
        }
    }
}
