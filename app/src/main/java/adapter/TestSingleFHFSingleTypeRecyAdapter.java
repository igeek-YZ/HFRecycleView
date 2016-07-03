package adapter;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.igeek.hfrecycleviewtest.R;
import com.igeek.hfrecyleviewlib.BasicRecyViewHolder;
import com.igeek.hfrecyleviewlib.HFSingleTypeRecyAdapter;

/**
 *  测试
 */
public class TestSingleFHFSingleTypeRecyAdapter extends HFSingleTypeRecyAdapter<String, TestSingleFHFSingleTypeRecyAdapter.RecyViewHolder> {


    public TestSingleFHFSingleTypeRecyAdapter(int resId) {
        super(resId);
    }

    @Override
    public RecyViewHolder buildViewHolder(View itemView) {
        return new RecyViewHolder(itemView);
    }

    @Override
    public void bindDataToHolder(RecyViewHolder holder, String s, int position) {
        holder.text.setText(s);
        holder.btn.setText("btn -> "+position);
        holder.btn.setTag(position);
    }

    public static class RecyViewHolder extends BasicRecyViewHolder {

        TextView text;
        Button btn;

        public RecyViewHolder(View itemView) {
            super(itemView);
            text= (TextView) itemView.findViewById(R.id.item_title);
            btn= (Button) itemView.findViewById(R.id.item_btn);
        }

    }
}
