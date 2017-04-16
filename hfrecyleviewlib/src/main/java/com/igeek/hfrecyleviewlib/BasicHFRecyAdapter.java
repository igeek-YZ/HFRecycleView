package com.igeek.hfrecyleviewlib;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.igeek.hfrecyleviewlib.BasicRecyViewHolder.OnFootViewClickListener;
import com.igeek.hfrecyleviewlib.BasicRecyViewHolder.OnFootViewLongClickListener;
import com.igeek.hfrecyleviewlib.BasicRecyViewHolder.OnHeadViewClickListener;
import com.igeek.hfrecyleviewlib.BasicRecyViewHolder.OnHeadViewLongClickListener;
import com.igeek.hfrecyleviewlib.BasicRecyViewHolder.OnItemClickListener;
import com.igeek.hfrecyleviewlib.BasicRecyViewHolder.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 含有头部和底部的适配器
 */
public abstract class BasicHFRecyAdapter<T> extends RecyclerView.Adapter<BasicRecyViewHolder> {

    public OnItemClickListener clickListener;
    public OnItemLongClickListener longClickListener;
    /**
     * headView的点击事件监听
     */
    public OnHeadViewClickListener headClickListener;
    /**
     * footView的点击事件监听
     */
    public OnFootViewClickListener footClickListener;
    /**
     * headView的长按事件监听
     */
    public OnHeadViewLongClickListener headLongClickListener;
    /**
     * footView的长按事件监听
     */
    public OnFootViewLongClickListener footLongClickListener;

    //数据集
    public List<T> datas = new ArrayList<T>(0);
    public SparseArray<View.OnClickListener> subHeadViewListeners = new SparseArray<>(0);
    public SparseArray<View.OnClickListener> subFootViewListeners = new SparseArray<>(0);
    public SparseArray<View.OnClickListener> subDataViewListeners = new SparseArray<>(0);
    public View headView;
    public View footView;

    @Override
    public int getItemViewType(int position) {

        if (headView != null) {
            if (position == 0) {
                return BasicRecyViewHolder.ITEM_TYPE_HEADER;
            }
        }

        if (footView != null) {
            if (headView == null) {
                if (position == datas.size())
                    return BasicRecyViewHolder.ITEM_TYPE_FOOTER;
            } else {
                if (position == datas.size() + 1)
                    return BasicRecyViewHolder.ITEM_TYPE_FOOTER;
            }
        }

        return getDataItemViewType(headView == null ? position : position - 1);
    }

    @Override
    public BasicRecyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        BasicRecyViewHolder viewHolder;
        BasicRecyViewHolder.OnItemClickListener listener;
        BasicRecyViewHolder.OnItemLongClickListener longListener;
        SparseArray<View.OnClickListener> subViewListeners;

        if (viewType == BasicRecyViewHolder.ITEM_TYPE_HEADER) {
            LayoutParams lp = new LayoutParams(parent.getMeasuredWidth(), LayoutParams.WRAP_CONTENT);
            headView.setLayoutParams(lp);
            viewHolder = createHeaderViewHolder(headView);
            subViewListeners = subHeadViewListeners;
            listener = headClickListener;
            longListener = headLongClickListener;
        } else if (viewType == BasicRecyViewHolder.ITEM_TYPE_FOOTER) {
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            footView.setLayoutParams(lp);
            viewHolder = createFooterViewHolder(footView);
            subViewListeners = subFootViewListeners;
            listener = footClickListener;
            longListener = footLongClickListener;
        } else {
            viewHolder = createViewTypeHolder(parent, viewType);
            subViewListeners = subDataViewListeners;
            listener = clickListener;
            longListener = longClickListener;
        }
        viewHolder.setViewType(viewType);
        viewHolder.setClickListener(listener);
        viewHolder.setLongClickListener(longListener);
        updateSubViewClickEvent(viewHolder, subViewListeners);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BasicRecyViewHolder holder, int position) {

        final int type = getItemViewType(position);

        if (type == BasicRecyViewHolder.ITEM_TYPE_HEADER) {
            bindDataToHeadViewHolder(holder);
        } else if (type == BasicRecyViewHolder.ITEM_TYPE_FOOTER) {
            bindDataToFootViewHolder(holder);
        } else {
            bindDataToViewHolder(holder, position - (headView == null ? 0 : 1), type);
        }
    }

    @Override
    public void onViewAttachedToWindow(BasicRecyViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        LayoutParams lp = holder.itemView.getLayoutParams();

        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            boolean needFull = holder.getViewType() == BasicRecyViewHolder.ITEM_TYPE_HEADER ||
                    holder.getViewType() == BasicRecyViewHolder.ITEM_TYPE_FOOTER;
            ((StaggeredGridLayoutManager.LayoutParams) lp).setFullSpan(needFull);
        }
    }

    @Override
    public int getItemCount() {
        if (datas.size() > 0) {
            return datas.size() + (headView == null ? 0 : 1) + (footView == null ? 0 : 1);
        } else {
            return datas.size() + (headView == null ? 0 : 1);
        }
    }

    /**
     * 返回数据的集合
     *
     * @return
     */
    public int getDataCount() {
        return datas.size();
    }

    public void updateSubViewClickEvent(BasicRecyViewHolder viewHolder, SparseArray<View.OnClickListener> subViewListeners) {
        for (int index = 0; index < subViewListeners.size(); index++) {
            int key_subResId = subViewListeners.keyAt(index);
            viewHolder.setSubViewClickListener(key_subResId, subViewListeners.get(key_subResId));
        }
    }

    /**
     * 获取每种数据结构的视图type
     *
     * @param position 这里的位置相对于headView的位置减少1(如果headView不为空的话)
     * @return 返回给定数据结构所需展现视图的类型
     */
    public abstract int getDataItemViewType(int position);

    /**
     * 创建头部ViewHolder
     *
     * @param headView recycleview header view (frist child view)
     * @return
     */
    public BasicRecyViewHolder createHeaderViewHolder(View headView) {
        return null;
    }

    /**
     * 创建底部ViewHolder
     *
     * @param footView recycleview footer view (last child view)
     * @return
     */
    public BasicRecyViewHolder createFooterViewHolder(View footView) {
        return null;
    }

    /**
     * 创建数据ViewHolder
     *
     * @param viewType recycleview child data view type
     * @return
     */
    public abstract BasicRecyViewHolder createViewTypeHolder(ViewGroup parent, int viewType);

    /**
     * 绑定数据到头部视图
     *
     * @param holder
     */
    public void bindDataToHeadViewHolder(BasicRecyViewHolder holder) {

    }

    /**
     * 绑定数据到底部视图
     *
     * @param holder
     */
    public void bindDataToFootViewHolder(BasicRecyViewHolder holder) {

    }

    /**
     * 绑定数据到视图上
     *
     * @param holder   数据的复用holder
     * @param position 如果含有头部视图，此时的position等于实际的position-1(如果 headView 不为Null)
     * @param viewType 视图类型
     */
    public abstract void bindDataToViewHolder(BasicRecyViewHolder holder, int position, int viewType);

    /**
     * 获取所有的数据集合
     *
     * @return
     */
    public List<T> getDatas() {
        return datas;
    }

    /**
     * 获取指定位置的数据
     */
    public T getData(int position) {
        if (position < 0 || position > getItemCount() - (footView == null ? 0 : 1))
            return null;
        else
            return datas.get(position);
    }

    public int getPositon(int adapterPosition) {
        return adapterPosition - (headView == null ? 0 : 1);
    }

    /**
     * 清楚所有的数据
     */
    public void clear() {
        datas.clear();
        notifyItemRangeRemoved(0 + (headView == null ? 0 : 1), datas.size());
    }

    /**
     * 刷新页面的元素
     *
     * @param datas
     */
    public void refreshDatas(List<T> datas) {
        if (datas != null) {
            this.datas=datas;
            notifyDataSetChanged();
//            final int offest=(headView == null ? 0 : 1);
//            notifyItemRangeChanged(0+offest, datas.size());
        }
    }

    /**
     * 添加信息的元素
     */
    public void insertData(int position, T data) {
        if (position >= 0 && position < getItemCount() - (footView == null ? 0 : 1) && data != null) {
            this.datas.add(position, data);
            notifyItemInserted(position + (headView == null ? 0 : 1));
        }
    }

    /**
     * 添加信息的元素
     */
    public void insertDatas(int position, List<T> datas) {
        if (position >= 0 && position < getItemCount() - (footView == null ? 0 : 1) && datas != null) {
            this.datas.addAll(position, datas);
            notifyItemRangeInserted(position + (headView == null ? 0 : 1), datas.size());
        }
    }

    /**
     * 加载更多的元素
     *
     * @param data
     */
    public void appendData(T data) {
        if (data != null) {
            final int oldSize = this.datas.size();
            this.datas.add(data);
            notifyItemInserted(oldSize + (headView == null ? 0 : 1));
        }
    }

    /**
     * 加载更多的元素
     */
    public void appendDatas(List<T> datas) {
        if (datas != null && datas.size() > 0) {
            this.datas.addAll(datas);
            final int offest = (headView == null ? 0 : 1);
            notifyItemRangeInserted(this.datas.size() - datas.size() + offest, datas.size());
        }
    }

    /**
     * 移除指定位置的数据
     */
    public void removeData(int position) {
        if (position >= 0 && position < getItemCount() - (footView == null ? 0 : 1)) {
            final int offest = (headView == null ? 0 : 1);
            this.datas.remove(position);
            notifyItemRemoved(position + offest);
        }
    }

    /**
     * 显示头部视图
     */
    public void setHeadVisibility(int visibility) {
        if (headView != null && headView.getVisibility() != visibility) {
            headView.setVisibility(visibility);
            notifyItemChanged(0);
        }
    }

    /**
     * 隐藏底部视图
     */
    public void setFootVisibility(int visibility) {
        if (footView != null && footView.getVisibility() != visibility) {
            footView.setVisibility(visibility);
            notifyItemChanged(datas.size() + (headView == null ? 0 : 1));
        }
    }

    /**
     * 更新头部视图
     *
     * @param newView 新的头部视图
     */
    public void updateHeadView(View newView) {

        if (newView == null || newView == headView)
            return;

        boolean isEmptyView = this.headView == null;

        this.headView = newView;

        if (isEmptyView)
            notifyDataSetChanged();
        else
            notifyItemChanged(0);
    }

    /**
     * 更新底部视图
     *
     * @param newView 新的底部视图
     */
    public void updateFootView(View newView) {

        if (newView == null || newView == footView)
            return;

        boolean isEmptyView = this.footView == null;
        this.footView = newView;
        if (isEmptyView)
            notifyDataSetChanged();
        else
            notifyItemChanged(datas.size() + (headView == null ? 0 : 1));
    }

    public boolean needMatchParentWidth(int position) {

        if (getHeadView() != null && position == 0) return true;

        if (getFootView() != null && position == (datas.size() + (getHeadView() == null ? 0 : 1)))
            return true;

        return false;

    }

    public View getHeadView() {
        return headView;
    }

    public void setHeadView(View headView) {
        this.headView = headView;
    }

    public View getFootView() {
        return footView;
    }

    public void setFootView(View footView) {
        this.footView = footView;
    }

    //添加头部子视图的点击事件
    public void addHeadSubViewListener(int resId, View.OnClickListener listener) {
        subHeadViewListeners.put(resId, listener);
    }

    //添加数据子视图的点击事件
    public void addSubViewListener(int resId, View.OnClickListener listener) {
        subDataViewListeners.put(resId, listener);
    }

    //添加底部子视图的点击事件
    public void addFootSubViewListener(int resId, View.OnClickListener listener) {
        subFootViewListeners.put(resId, listener);
    }

    public void setItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public void setItemClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public void setHeadClickListener(OnHeadViewClickListener headClickListener) {
        this.headClickListener = headClickListener;
    }

    public void setFootClickListener(OnFootViewClickListener footClickListener) {
        this.footClickListener = footClickListener;
    }

    public void setHeadLongClickListener(OnHeadViewLongClickListener headLongClickListener) {
        this.headLongClickListener = headLongClickListener;
    }

    public void setFootLongClickListener(OnFootViewLongClickListener footLongClickListener) {
        this.footLongClickListener = footLongClickListener;
    }

    public static class SimpleRecyViewHolder extends BasicRecyViewHolder {

        public SimpleRecyViewHolder(View itemView) {
            super(itemView);
        }

        public SimpleRecyViewHolder(View itemView, OnItemClickListener clickListener, OnItemLongClickListener longClickListener) {
            super(itemView, clickListener, longClickListener);
        }

    }

}
