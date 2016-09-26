package bean;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.igeek.hfrecycleviewtest.R;
import com.igeek.hfrecyleviewlib.BaseHolderType;
import com.igeek.hfrecyleviewlib.BasicRecyViewHolder;

import entitys.RmTypeData7;

public class RmListType7 extends BaseHolderType<RmTypeData7, RmListType7.Viewholder> {

    @Override
    public int getType(RmTypeData7 rmTypeData7) {
        return rmTypeData7.getType();
    }

    @Override
    public void bindDataToHolder(Viewholder holder, RmTypeData7 rmTypeData7, int postion) {
        holder.title.setText(rmTypeData7.getTitle());
        holder.coverImg.setImageResource(rmTypeData7.getCoverImgId());
        holder.hotNumber.setText(rmTypeData7.getHotNumber()+"");
        holder.subTitle.setVisibility(TextUtils.isEmpty(rmTypeData7.getSubTitle())?View.GONE:View.VISIBLE);
        holder.subTitle.setText(rmTypeData7.getSubTitle());
    }

    @Override
    public BasicRecyViewHolder buildHolder(ViewGroup parent) {
        return new Viewholder(View.inflate(parent.getContext(), R.layout.layout_multi_typeview7,null));
    }

    public static class Viewholder extends BasicRecyViewHolder {

        ImageView coverImg;
        ImageView hotIcon;
        TextView title;
        TextView subTitle;
        TextView hotNumber;

        public Viewholder(View itemView) {
            this(itemView,null,null);
        }

        public Viewholder(View itemView, OnItemClickListener clickListener, OnItemLongClickListener longClickListener) {
            super(itemView, clickListener, longClickListener);
            coverImg= (ImageView) itemView.findViewById(R.id.rmTypeView7_coverImg);
            hotIcon= (ImageView) itemView.findViewById(R.id.rmTypeView7_hotIcon);
            title= (TextView) itemView.findViewById(R.id.rmTypeView7_title);
            subTitle= (TextView) itemView.findViewById(R.id.rmTypeView7_subTitle);
            hotNumber= (TextView) itemView.findViewById(R.id.rmTypeView7_hotNumber);
        }
    }
}
