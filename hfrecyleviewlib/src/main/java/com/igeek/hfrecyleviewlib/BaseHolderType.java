package com.igeek.hfrecyleviewlib;

/**
 * Created by igeek
 */
public abstract class BaseHolderType<T,VH extends BasicRecyViewHolder> implements HolderTypeData<VH> {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public int getType() {
        return getType(data);
    }

    @Override
    public void bindDatatoHolder(VH vh, int postion, int type) {
        bindDataToHolder(vh,data,postion);
    }

    public abstract int getType(T t);

    public abstract void bindDataToHolder(VH vh,T t,int postion);

}
