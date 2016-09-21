package com.igeek.hfrecyleviewlib;

import android.view.ViewGroup;

/**
 * Created by A10-02 on 2016/6/30.
 */
public interface  HolderTypeData<VH extends BasicRecyViewHolder> {

    int getType();

    BasicRecyViewHolder buildHolder(ViewGroup parent);

    void bindDatatoHolder(VH vh,int postion,int type);
}
