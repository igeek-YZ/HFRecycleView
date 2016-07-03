package com.igeek.hfrecyleviewlib;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.igeek.hfrecyleviewlib.utils.DensityUtils;


/**
 */
public class HFLineVerComDecoration extends RecyclerView.ItemDecoration{

    private int mHeight;
    private Paint mPaint;

    public HFLineVerComDecoration(int dp, int color) {
        mHeight = DensityUtils.dp2px(dp);
        mPaint = new Paint();
        mPaint.setColor(color);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        View child=parent.getChildAt(0);
        if(child!=null)
        c.drawLine(child.getLeft(), child.getTop()+ mHeight, child.getRight(), child.getTop(), mPaint);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int childCount=parent.getChildCount();
        for(int index=0;index<childCount;index++){
            View child=parent.getChildAt(index);
            c.drawLine(child.getLeft(),child.getBottom(),child.getRight(),child.getBottom()+mHeight,mPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if(parent.getChildAdapterPosition(view)==0){
            outRect.set(0,mHeight,0,mHeight);
        }else{
            outRect.set(0,0,0,mHeight);
        }
    }
}
