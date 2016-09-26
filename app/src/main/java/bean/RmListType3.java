package bean;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.igeek.hfrecycleviewtest.R;
import com.igeek.hfrecyleviewlib.BaseHolderType;
import com.igeek.hfrecyleviewlib.BasicRecyViewHolder;

import entitys.RmTypeData3;

public class RmListType3 extends BaseHolderType<RmTypeData3, RmListType3.Viewholder> {

    @Override
    public int getType(RmTypeData3 rmTypeData3) {
        return rmTypeData3.getType();
    }

    @Override
    public BasicRecyViewHolder buildHolder(ViewGroup parent) {
        return new Viewholder(View.inflate(parent.getContext(), R.layout.layout_multi_typeview3,null));
    }

    @Override
    public void bindDataToHolder(Viewholder viewholder, RmTypeData3 rmTypeData3, int postion) {
        viewholder.bookName.setText("bookName"+postion);
    }

    public static class Viewholder extends BasicRecyViewHolder {

        ImageView coverImg;
        TextView bookName;
        TextView tags;
        TextView hotNumber;

        public Viewholder(View itemView) {
            this(itemView,null,null);
        }

        public Viewholder(View itemView, OnItemClickListener clickListener, OnItemLongClickListener longClickListener) {
            super(itemView, clickListener, longClickListener);
            coverImg= (ImageView) itemView.findViewById(R.id.rmTypeView3_coverImg);
            bookName= (TextView) itemView.findViewById(R.id.rmTypeView3_bookName);
            tags= (TextView) itemView.findViewById(R.id.rmTypeView3_tags);
            hotNumber= (TextView) itemView.findViewById(R.id.rmTypeView3_hotNumber);
        }
    }
}
