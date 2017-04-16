package com.igeek.hfrecyleviewlib.utils;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * 计算RecyclerView当前的可见位置
 * Created by 杨召
 */
public class RecyclerViewUtil {

    public static int findFirstVisibleItemPosition(RecyclerView recyclerView){
        int position=-1;
        if(recyclerView==null||recyclerView.getLayoutManager()==null){
            return position;
        }
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager.getClass() == LinearLayoutManager.class) {
            LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
            position=manager.findFirstVisibleItemPosition();
        } else if (layoutManager.getClass() == GridLayoutManager.class) {
            GridLayoutManager manager = (GridLayoutManager) layoutManager;
            position=manager.findFirstVisibleItemPosition();
        } else if (layoutManager.getClass() == StaggeredGridLayoutManager.class) {
            StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) layoutManager;
            int[] positions = new int[manager.getSpanCount()];
            manager.findFirstVisibleItemPositions(positions);
            position=findMinValue(positions);
        }
        return position;
    }

    public static int findLastVisibleItemPosition(RecyclerView recyclerView){
        int position=-1;
        if(recyclerView==null||recyclerView.getLayoutManager()==null){
            return -1;
        }
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager.getClass() == LinearLayoutManager.class) {
            LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
            position=manager.findLastVisibleItemPosition();
        } else if (layoutManager.getClass() == GridLayoutManager.class) {
            GridLayoutManager manager = (GridLayoutManager) layoutManager;
            position=manager.findLastVisibleItemPosition();
        } else if (layoutManager.getClass() == StaggeredGridLayoutManager.class) {
            StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) layoutManager;
            int[] positions = new int[manager.getSpanCount()];
            manager.findLastVisibleItemPositions(positions);
            position=findMaxValue(positions);
        }
        return position;
    }

    public static int findMinValue(int[] values) {

        int minVal = values[0];
        for (Integer value : values) {
            if (minVal > value)
                minVal = value;
        }

        return minVal;
    }

    public static int findMaxValue(int[] values) {

        int maxVal = values[0];
        for (Integer value : values) {
            if (maxVal < value)
                maxVal = value;
        }

        return maxVal;
    }
}
