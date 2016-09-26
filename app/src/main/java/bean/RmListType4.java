package bean;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.igeek.hfrecycleviewtest.R;
import com.igeek.hfrecyleviewlib.BaseHolderType;
import com.igeek.hfrecyleviewlib.BasicRecyViewHolder;

import entitys.RmTypeData4;

public class RmListType4 extends BaseHolderType<RmTypeData4, RmListType4.Viewholder> {

    @Override
    public int getType(RmTypeData4 rmTypeData4) {
        return rmTypeData4.getType();
    }

    @Override
    public BasicRecyViewHolder buildHolder(ViewGroup parent) {
        return new Viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_multi_typeview4,parent,false));
    }

    @Override
    public void bindDataToHolder(Viewholder holder, RmTypeData4 rmTypeData4, int postion) {
        holder.bookName.setText(rmTypeData4.getBookName());
        holder.coverImg.setImageResource(rmTypeData4.getCoverImgId());
        holder.hotNumber.setText(rmTypeData4.getHotNumber()+"");
        holder.newstChapterTip.setText("更新到了"+rmTypeData4.getNewstChapter()+"话");
    }

    public static class Viewholder extends BasicRecyViewHolder {

        ImageView coverImg;
        ImageView hotIcon;
        TextView bookName;
        TextView newstChapterTip;
        TextView hotNumber;

        public Viewholder(View itemView) {
            this(itemView,null,null);
        }

        public Viewholder(View itemView, OnItemClickListener clickListener, OnItemLongClickListener longClickListener) {
            super(itemView, clickListener, longClickListener);
            coverImg= (ImageView) itemView.findViewById(R.id.rmTypeView4_coverImg);
            hotIcon= (ImageView) itemView.findViewById(R.id.rmTypeView4_hotIcon);
            bookName= (TextView) itemView.findViewById(R.id.rmTypeView4_bookName);
            newstChapterTip= (TextView) itemView.findViewById(R.id.rmTypeView4_newstChapterTip);
            hotNumber= (TextView) itemView.findViewById(R.id.rmTypeView4_hotNumber);
        }
    }
}
