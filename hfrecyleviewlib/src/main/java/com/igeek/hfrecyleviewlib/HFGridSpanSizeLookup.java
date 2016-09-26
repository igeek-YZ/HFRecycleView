package com.igeek.hfrecyleviewlib;

import android.support.v7.widget.GridLayoutManager;

public class HFGridSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    private GridLayoutManager layoutManager;
    private BasicHFRecyAdapter adapter;

    @Override
    public int getSpanSize(int position) {
        if(layoutManager!=null&&adapter!=null)
            return adapter.needMatchParentWidth(position)?layoutManager.getSpanCount():1;
        return 1;
    }

    public void setLayoutManager(GridLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public BasicHFRecyAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(BasicHFRecyAdapter adapter) {
        this.adapter = adapter;
    }
}
