package bean;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.igeek.hfrecycleviewtest.R;
import com.igeek.hfrecyleviewlib.BaseHolderType;
import com.igeek.hfrecyleviewlib.BasicRecyViewHolder;

import entitys.RmTypeData2;

public class RmListType2 extends BaseHolderType<RmTypeData2,RmListType2.Viewholder> {

    @Override
    public int getType(RmTypeData2 rmListType2) {
        return rmListType2.getType();
    }

    @Override
    public BasicRecyViewHolder buildHolder(ViewGroup parent) {
        return new Viewholder(View.inflate(parent.getContext(), R.layout.layout_multi_typeview2,null));
    }

    @Override
    public void bindDataToHolder(Viewholder viewholder, RmTypeData2 rmListType2, int postion) {

    }

    public static class Viewholder extends BasicRecyViewHolder {

        ImageView img;

        public Viewholder(View itemView) {
            this(itemView,null,null);
        }

        public Viewholder(View itemView, OnItemClickListener clickListener, OnItemLongClickListener longClickListener) {
            super(itemView, clickListener, longClickListener);
            img= (ImageView) itemView.findViewById(R.id.rmTypeView2_img);
        }
    }
}
