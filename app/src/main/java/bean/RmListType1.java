package bean;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.igeek.hfrecycleviewtest.R;
import com.igeek.hfrecyleviewlib.BaseHolderType;
import com.igeek.hfrecyleviewlib.BasicRecyViewHolder;

import entitys.RmTypeData1;

public class RmListType1 extends BaseHolderType<RmTypeData1, RmListType1.Viewholder> {

    @Override
    public int getType(RmTypeData1 rmTypeData1) {
        return rmTypeData1.getType();
    }

    @Override
    public BasicRecyViewHolder buildHolder(ViewGroup parent) {
        return new Viewholder(View.inflate(parent.getContext(), R.layout.layout_multi_typeview1,null));
    }

    @Override
    public void bindDataToHolder(Viewholder holder, RmTypeData1 rmTypeData1, int postion) {
        holder.more.setTag(postion);
        holder.title.setText(rmTypeData1.getTitle());
    }

    public static class Viewholder extends BasicRecyViewHolder {

        View more;
        ImageView icon;
        TextView title;


        public Viewholder(View itemView) {
            this(itemView,null,null);
        }

        public Viewholder(View itemView, OnItemClickListener clickListener, OnItemLongClickListener longClickListener) {
            super(itemView, clickListener, longClickListener);
            more= itemView.findViewById(R.id.rmTypeView1_more);
            icon= (ImageView) itemView.findViewById(R.id.rmTypeView1_icon);
            title= (TextView) itemView.findViewById(R.id.rmTypeView1_title);
        }
    }
}
