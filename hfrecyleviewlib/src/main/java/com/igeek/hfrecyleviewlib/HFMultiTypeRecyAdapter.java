package com.igeek.hfrecyleviewlib;

import android.view.ViewGroup;

public class HFMultiTypeRecyAdapter extends BasicHFRecyAdapter<HolderTypeData> {

    @Override
    public int getDataItemViewType(int position) {
        return datas.get(position).getType();
    }

    @Override
    public BasicRecyViewHolder createViewTypeHolder(ViewGroup parent, int viewType) {
        for(HolderTypeData typeData:datas){
            if(typeData.getType()==viewType){
                return typeData.buildHolder(parent);
            }
        }
        return null;
    }

    @Override
    public void bindDataToViewHolder(BasicRecyViewHolder holder, int position, int viewType) {
        datas.get(position).bindDatatoHolder(holder,viewType);
    }
}
