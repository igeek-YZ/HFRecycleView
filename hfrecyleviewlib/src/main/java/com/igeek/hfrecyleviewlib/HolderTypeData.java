package com.igeek.hfrecyleviewlib;

import android.view.ViewGroup;

/**
 * Created by A10-02 on 2016/6/30.
 */
public abstract class HolderTypeData<VH extends BasicRecyViewHolder> {

    public abstract int getType();

    public abstract BasicRecyViewHolder buildHolder(ViewGroup parent);

    public abstract void bindDatatoHolder(VH vh,int postion,int type);
}
