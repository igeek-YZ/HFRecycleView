package adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.igeek.hfrecycleviewtest.R;
import com.igeek.hfrecyleviewlib.BasicRecyViewHolder;
import com.igeek.hfrecyleviewlib.HFSingleTypeRecyAdapter;

/**
 * 测试
 */
public class TestGridImgRecyAdapter extends HFSingleTypeRecyAdapter<Integer, TestGridImgRecyAdapter.RecyViewHolder> {


    public TestGridImgRecyAdapter(int resId) {
        super(resId);
    }

    @Override
    public RecyViewHolder buildViewHolder(View itemView) {
        return new RecyViewHolder(itemView);
    }

    @Override
    public void bindDataToHolder(RecyViewHolder holder, Integer s, int position) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.img.getLayoutParams();
        lp.height = lp.width;
        holder.img.setLayoutParams(lp);
        holder.img.setImageResource(s);
    }

    public static class RecyViewHolder extends BasicRecyViewHolder {

        ImageView img;

        public RecyViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.typeData4_grid_item);
        }

    }
}
