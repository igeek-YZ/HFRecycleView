package bean;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.igeek.hfrecycleviewtest.R;
import com.igeek.hfrecyleviewlib.BasicRecyViewHolder;
import com.igeek.hfrecyleviewlib.HolderTypeData;


/**
 * Created by A10-02 on 2016/6/30.
 */
public class TypeData1 extends HolderTypeData<TypeData1.ViewHolder> {

    private String id;
    private String name;

    public TypeData1(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int getType() {
        return id.hashCode();
    }

    @Override
    public BasicRecyViewHolder buildHolder(ViewGroup parent) {
        return new ViewHolder(View.inflate(parent.getContext(), R.layout.layout_recy_item, null));
    }

    @Override
    public void bindDatatoHolder(ViewHolder bookViewHolder,int postion, int type) {
        bookViewHolder.title.setText(name);
        bookViewHolder.btn.setText(id);
        bookViewHolder.btn.setTag(postion);
    }

    public static class ViewHolder extends BasicRecyViewHolder {

        Button btn;
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            btn = (Button) itemView.findViewById(R.id.item_btn);
            title = (TextView) itemView.findViewById(R.id.item_title);
        }
    }
}
