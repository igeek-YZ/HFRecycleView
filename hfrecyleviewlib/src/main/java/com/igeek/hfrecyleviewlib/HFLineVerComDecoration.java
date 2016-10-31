package com.igeek.hfrecyleviewlib;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.igeek.hfrecyleviewlib.utils.DensityUtils;

public class HFLineVerComDecoration extends RecyclerView.ItemDecoration{

    private int mHeight;
    private Paint mPaint;

    public HFLineVerComDecoration(int dp, int color) {
        mHeight = DensityUtils.dp2px(dp);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mHeight);
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int childCount=parent.getChildCount();
        RecyclerView.Adapter adapter=parent.getAdapter();
        for(int index=0;index<childCount;index++){
            View child=parent.getChildAt(index);
            boolean isDraw=true;
            if(index==childCount-1&&(adapter instanceof BasicHFRecyAdapter)){
                isDraw=((BasicHFRecyAdapter) adapter).getFootView()==null;
            }
            if(isDraw)
                c.drawLine(child.getLeft(),child.getBottom(),child.getRight(),child.getBottom()+mHeight,mPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.set(0,0,0,mHeight);

        RecyclerView.Adapter adapter=parent.getAdapter();
        if(adapter instanceof BasicHFRecyAdapter){
            if(((BasicHFRecyAdapter) adapter).getFootView()!=null&&parent.getChildLayoutPosition(view)==parent.getChildCount()-1){
                outRect.set(0,0,0,0);
            }
        }

    }
}
