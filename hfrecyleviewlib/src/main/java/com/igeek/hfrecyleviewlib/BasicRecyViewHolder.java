package com.igeek.hfrecyleviewlib;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

public abstract class BasicRecyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    //view的类型
    private int viewType;
    //头部
    public static final int ITEM_TYPE_HEADER = -1;
    //数据部分
    public static final int ITEM_TYPE_DATA = -2;
    //底部
    public static final int ITEM_TYPE_FOOTER = -3;

    /**
     * viewholder的点击事件监听
     */
    private OnItemClickListener clickListener;
    /**
     * viewholder的长按事件监听
     */
    private OnItemLongClickListener longClickListener;

    private SparseArray<View.OnClickListener> subClickListenrs = new SparseArray<View.OnClickListener>(0);
    private SparseArray<View.OnLongClickListener> subLongClickListenrs = new SparseArray<View.OnLongClickListener>(0);

    public BasicRecyViewHolder(View itemView) {
        super(itemView);
    }

    public BasicRecyViewHolder(View itemView, OnItemClickListener clickListener, OnItemLongClickListener longClickListener) {
        super(itemView);
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;

        if (this.clickListener != null)
            itemView.setOnClickListener(this);
        if (this.longClickListener != null)
            itemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {

        View.OnClickListener subClickListener = subClickListenrs.get(v.getId());

        if (subClickListener != null) {
            subClickListener.onClick(v);
        } else {
            if (viewType == ITEM_TYPE_HEADER) {
                if (clickListener != null)
                    ((OnHeadViewClickListener) clickListener).onRecycleHeadClick(itemView, v);
            } else if (viewType == ITEM_TYPE_FOOTER) {
                if (clickListener != null)
                    ((OnFootViewClickListener) clickListener).onReCycleFootClick(itemView, v);
            } else {
                if (clickListener != null)
                    clickListener.OnItemClick(v, getLayoutPosition());
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {

        View.OnLongClickListener sublongClickListener = subLongClickListenrs.get(v.getId());

        if (sublongClickListener != null) {
            sublongClickListener.onLongClick(v);
        } else {
            if (viewType == ITEM_TYPE_HEADER) {
                if (longClickListener != null)
                    ((OnHeadViewLongClickListener) clickListener).onRecycleHeadLongClick(itemView, v);
            } else if (viewType == ITEM_TYPE_FOOTER) {
                if (longClickListener != null)
                    ((OnFootViewLongClickListener) clickListener).onReCycleFootLongClick(itemView, v);
            } else {
                if (longClickListener != null)
                    longClickListener.OnItemLongClick(v, getLayoutPosition());
            }
        }
        return longClickListener != null||sublongClickListener!=null;
    }


    public interface OnItemClickListener {
        void OnItemClick(View v, int adapterPosition);
    }

    public interface OnItemLongClickListener {
        void OnItemLongClick(View v, int adapterPosition);
    }

    public interface OnHeadViewClickListener extends BasicRecyViewHolder.OnItemClickListener {
        void onRecycleHeadClick(View view, View clickView);
    }

    public interface OnFootViewClickListener extends BasicRecyViewHolder.OnItemClickListener {
        void onReCycleFootClick(View view, View clickView);
    }

    public interface OnHeadViewLongClickListener extends BasicRecyViewHolder.OnItemLongClickListener {
        void onRecycleHeadLongClick(View view, View clickView);
    }

    public interface OnFootViewLongClickListener extends BasicRecyViewHolder.OnItemLongClickListener {
        void onReCycleFootLongClick(View view, View clickView);
    }

    public OnItemClickListener getClickListener() {
        return clickListener;
    }

    public OnItemLongClickListener getLongClickListener() {
        return longClickListener;
    }


    //设置单击回调
    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
        if (this.clickListener != null)
            itemView.setOnClickListener(this);
    }

    //设置长按回调
    public void setLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
        if (this.longClickListener != null)
            itemView.setOnLongClickListener(this);
    }

    /**
     * 为itemView 的子控件添加点击事件的处理
     *
     * @param resId itemView 子视图的 android:id
     */
    public void setSubViewClickListener(int resId, View.OnClickListener listener) {
        View view = itemView.findViewById(resId);
        if (view != null) {
            view.setOnClickListener(this);
            subClickListenrs.put(resId, listener);
        }
    }

    /**
     * 为itemView 的子控件添加长按事件的处理
     *
     * @param resId itemView 子视图的 android:id
     */
    public void setSubViewLongClickListener(int resId, View.OnLongClickListener listener) {
        View view = itemView.findViewById(resId);
        if (view != null) {
            view.setOnLongClickListener(this);
            subLongClickListenrs.put(resId, listener);
        }
    }

    /**
     * 如果发生引用导致内存泄露，那就在宿主销毁之前重置下吧
     */
    public void resetAllListeners() {
        this.clickListener = null;
        this.longClickListener = null;
        this.subClickListenrs.clear();
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

}
