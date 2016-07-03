package adapter.viewholder;

import android.view.View;

import com.igeek.hfrecyleviewlib.BasicRecyViewHolder;


public class BookViewHolder extends BasicRecyViewHolder {


    public BookViewHolder(View itemView) {
        super(itemView);
    }

    public BookViewHolder(View itemView, OnItemClickListener clickListener, OnItemLongClickListener longClickListener) {
        super(itemView, clickListener, longClickListener);
    }

}
