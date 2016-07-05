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
public class TypeData2 extends HolderTypeData<TypeData2.ViewHolder> {

    private String title;
    private String id;

    public TypeData2(String id,String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public int getType() {
        return id.hashCode();
    }

    @Override
    public BasicRecyViewHolder buildHolder(ViewGroup parent) {
        return new ViewHolder(View.inflate(parent.getContext(), R.layout.layout_type_item2, null));
    }

    @Override
    public void bindDatatoHolder(ViewHolder holder,int position, int type) {
        holder.title.setText(title);
    }


    public static class ViewHolder extends BasicRecyViewHolder {

        ImageView img1;
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            img1 = (ImageView) itemView.findViewById(R.id.typedata2_img);
            title = (TextView) itemView.findViewById(R.id.typedata2_title);
        }
    }
}
