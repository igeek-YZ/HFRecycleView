package com.igeek.hfrecyleviewlib;

import android.view.ViewGroup;

public interface  HolderTypeData<VH extends BasicRecyViewHolder> {

    int getType();

    BasicRecyViewHolder buildHolder(ViewGroup parent);

    void bindDatatoHolder(VH vh,int postion,int type);
}
