package bean;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.igeek.hfrecycleviewtest.R;
import com.igeek.hfrecyleviewlib.BasicRecyViewHolder;
import com.igeek.hfrecyleviewlib.HolderTypeData;


/**
 * Created by A10-02 on 2016/6/30.
 */
public class TypeData3 extends HolderTypeData<TypeData3.ViewHolder> {

    private String id;
    private String name;

    public TypeData3(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int getType() {
        return id.hashCode();
    }

    @Override
    public BasicRecyViewHolder buildHolder(ViewGroup parent) {
        return new ViewHolder(View.inflate(parent.getContext(), R.layout.layout_type_item1, null));
    }

    @Override
    public void bindDatatoHolder(ViewHolder viewHolder,int position, int type) {

        viewHolder.title.setText(name);
    }


    public static class ViewHolder extends BasicRecyViewHolder {

        ImageView img1;
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            img1 = (ImageView) itemView.findViewById(R.id.typedata1_img);
            title = (TextView) itemView.findViewById(R.id.typedata1_title);
        }
    }
}
