package bean;

import android.view.ViewGroup;

import com.igeek.hfrecyleviewlib.BasicRecyViewHolder;
import com.igeek.hfrecyleviewlib.HolderTypeData;

import adapter.viewholder.BookViewHolder;


/**
 * Created by A10-02 on 2016/6/30.
 */
public class Book extends HolderTypeData<BookViewHolder> {

    private String id;
    private String name;
    private String price;

    @Override
    public int getType() {
        return id.hashCode();
    }

    @Override
    public BasicRecyViewHolder buildHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void bindDatatoHolder(BookViewHolder bookViewHolder, int type) {

    }
}
