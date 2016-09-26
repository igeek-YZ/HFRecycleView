package bean;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.igeek.hfrecycleviewtest.R;
import com.igeek.hfrecyleviewlib.BaseHolderType;
import com.igeek.hfrecyleviewlib.BasicRecyViewHolder;

import entitys.RmTypeData5;

public class RmListType5 extends BaseHolderType<RmTypeData5, RmListType5.Viewholder> {

    @Override
    public int getType(RmTypeData5 rmTypeData5) {
        return rmTypeData5.getType();
    }

    @Override
    public BasicRecyViewHolder buildHolder(ViewGroup parent) {
        return new Viewholder(View.inflate(parent.getContext(), R.layout.layout_multi_typeview5,null));
    }

    @Override
    public void bindDataToHolder(Viewholder viewholder, RmTypeData5 rmTypeData5, int postion) {

    }

    public static class Viewholder extends BasicRecyViewHolder {

        ImageView bigCoverImg;
        ImageView smailCoverImg1;
        TextView bookName1;
        ImageView smailCoverImg2;
        TextView bookName2;
        ImageView smailCoverImg3;
        TextView bookName3;


        public Viewholder(View itemView) {
            this(itemView,null,null);
        }

        public Viewholder(View itemView, OnItemClickListener clickListener, OnItemLongClickListener longClickListener) {
            super(itemView, clickListener, longClickListener);
            bigCoverImg= (ImageView) itemView.findViewById(R.id.rmTypeView5_bigCoverImg);
            smailCoverImg1= (ImageView) itemView.findViewById(R.id.rmTypeView5_smailCoverImg1);
            bookName1= (TextView) itemView.findViewById(R.id.rmTypeView5_bookName1);
            smailCoverImg2= (ImageView) itemView.findViewById(R.id.rmTypeView5_smailCoverImg2);
            bookName2= (TextView) itemView.findViewById(R.id.rmTypeView5_bookName2);
            smailCoverImg3= (ImageView) itemView.findViewById(R.id.rmTypeView5_smailCoverImg3);
            bookName3= (TextView) itemView.findViewById(R.id.rmTypeView5_bookName3);
        }
    }
}
