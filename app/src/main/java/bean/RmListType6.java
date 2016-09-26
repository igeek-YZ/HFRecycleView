package bean;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.igeek.hfrecycleviewtest.R;
import com.igeek.hfrecyleviewlib.BaseHolderType;
import com.igeek.hfrecyleviewlib.BasicRecyViewHolder;
import com.igeek.hfrecyleviewlib.HFLineHerComDecoration;

import adapter.TestCoverImgSingleTypeRecyAdapter;
import entitys.RmTypeData6;

public class RmListType6 extends BaseHolderType<RmTypeData6, RmListType6.Viewholder> {

    @Override
    public int getType(RmTypeData6 rmTypeData6) {
        return rmTypeData6.getType();
    }

    @Override
    public BasicRecyViewHolder buildHolder(ViewGroup parent) {
        return new Viewholder(View.inflate(parent.getContext(), R.layout.layout_multi_typeview6,null));
    }

    @Override
    public void bindDataToHolder(Viewholder holder, RmTypeData6 rmTypeData6, int postion) {
        TestCoverImgSingleTypeRecyAdapter adapter= new TestCoverImgSingleTypeRecyAdapter(R.layout.layout_multi_typeview6_coverimg);
        holder.recyclerView.setAdapter(adapter);
        LinearLayoutManager manager=new LinearLayoutManager(holder.itemView.getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.recyclerView.setLayoutManager(manager);
        HFLineHerComDecoration decoration= (HFLineHerComDecoration) holder.recyclerView.getTag();
        if(decoration==null) {
            decoration=new HFLineHerComDecoration(8, Color.parseColor("#f4f4f4"));
            holder.recyclerView.addItemDecoration(decoration);
            holder.recyclerView.setTag(decoration);
        }
        adapter.refreshDatas(rmTypeData6.getCoverImgIds());
    }

    public static class Viewholder extends BasicRecyViewHolder {

        RecyclerView recyclerView;

        public Viewholder(View itemView) {
            this(itemView,null,null);
        }

        public Viewholder(View itemView, OnItemClickListener clickListener, OnItemLongClickListener longClickListener) {
            super(itemView, clickListener, longClickListener);
            recyclerView= (RecyclerView) itemView.findViewById(R.id.rmTypeView6_recyclerview);
        }
    }
}
