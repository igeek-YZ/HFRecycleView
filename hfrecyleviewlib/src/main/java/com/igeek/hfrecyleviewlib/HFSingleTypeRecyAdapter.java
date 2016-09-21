package com.igeek.hfrecyleviewlib;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 *  单个type类型的适配器
 */
public abstract class HFSingleTypeRecyAdapter<T,H extends BasicRecyViewHolder> extends BasicHFRecyAdapter<T> {

    private int resId;

    public HFSingleTypeRecyAdapter(int resId) {
        this.resId = resId;
    }

    @Override
    public BasicRecyViewHolder createHeaderViewHolder(View headView) {
        return new SimpleRecyViewHolder(headView);
    }

    @Override
    public BasicRecyViewHolder createFooterViewHolder(View footView) {
        return new SimpleRecyViewHolder(footView);
    }

    @Override
    public int getDataItemViewType(int position) {
        return BasicRecyViewHolder.ITEM_TYPE_DATA;
    }

    @Override
    public BasicRecyViewHolder createViewTypeHolder(ViewGroup parent, int viewType) {
        return buildViewHolder(LayoutInflater.from(parent.getContext()).inflate(resId,parent,false));
    }

    @Override
    public void bindDataToViewHolder(BasicRecyViewHolder holder, int position, int viewType) {
        bindDataToHolder((H) holder,datas.get(position),position);
    }

    public abstract H buildViewHolder(View itemView);

    public abstract void bindDataToHolder(H holder,T t,int position);


}
