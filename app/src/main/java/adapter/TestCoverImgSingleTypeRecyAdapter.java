package adapter;

import android.view.View;
import android.widget.ImageView;

import com.igeek.hfrecycleviewtest.R;
import com.igeek.hfrecyleviewlib.BasicRecyViewHolder;
import com.igeek.hfrecyleviewlib.HFSingleTypeRecyAdapter;

public class TestCoverImgSingleTypeRecyAdapter extends HFSingleTypeRecyAdapter<Integer, TestCoverImgSingleTypeRecyAdapter.RecyViewHolder> {


    public TestCoverImgSingleTypeRecyAdapter(int resId) {
        super(resId);
    }

    @Override
    public RecyViewHolder buildViewHolder(View itemView) {
        return new RecyViewHolder(itemView);
    }

    @Override
    public void bindDataToHolder(RecyViewHolder holder, Integer resId, int position) {
        holder.coverImg.setImageResource(resId);
    }

    public static class RecyViewHolder extends BasicRecyViewHolder {

        ImageView coverImg;

        public RecyViewHolder(View itemView) {
            super(itemView);
            coverImg= (ImageView) itemView.findViewById(R.id.typeData6_item_coverImg);
        }

    }
}
