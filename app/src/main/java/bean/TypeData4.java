package bean;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.igeek.hfrecycleviewtest.R;
import com.igeek.hfrecyleviewlib.BasicRecyViewHolder;
import com.igeek.hfrecyleviewlib.HFGridVerDecoration;
import com.igeek.hfrecyleviewlib.HolderTypeData;

import java.util.List;

import adapter.TestGridImgRecyAdapter;


/**
 * Created by A10-02 on 2016/6/30.
 */
public class TypeData4 extends HolderTypeData<TypeData4.ViewHolder> {

    private List<Integer> imgResIds;
    private String id;

    public TypeData4(String id) {
        this.id = id;
    }

    public TypeData4( String id,List<Integer> imgResIds) {
        this.imgResIds = imgResIds;
        this.id = id;
    }

    public void setImgResIds(List<Integer> imgResIds) {
        this.imgResIds = imgResIds;
    }

    @Override
    public int getType() {
        return id.hashCode();
    }

    @Override
    public BasicRecyViewHolder buildHolder(ViewGroup parent) {
        return new ViewHolder(View.inflate(parent.getContext(), R.layout.layout_grid_item4, null));
    }

    @Override
    public void bindDatatoHolder(ViewHolder holder, int position,int type) {
        TestGridImgRecyAdapter adapter=new TestGridImgRecyAdapter(R.layout.layout_grid_item4_item);
        holder.recyclerView.addItemDecoration(new HFGridVerDecoration());
        holder.recyclerView.setAdapter(adapter);
        GridLayoutManager manager=new GridLayoutManager(holder.itemView.getContext(),2);
        manager.setAutoMeasureEnabled(true);
        holder.recyclerView.setLayoutManager(manager);
        adapter.refreshDatas(imgResIds);
    }

    public static class ViewHolder extends BasicRecyViewHolder {

        RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.typeData4_recycleview);
        }
    }
}
