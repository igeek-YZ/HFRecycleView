package adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.igeek.hfrecycleviewtest.R;
import com.igeek.hfrecyleviewlib.BasicRecyViewHolder;
import com.igeek.hfrecyleviewlib.HFSingleTypeRecyAdapter;

import entitys.RandomEntity;

/**
 * 测试
 */
public class TestStaggeredGridHFSingleTypeRecyAdapter extends HFSingleTypeRecyAdapter<RandomEntity, TestStaggeredGridHFSingleTypeRecyAdapter.RecyViewHolder> {


    public TestStaggeredGridHFSingleTypeRecyAdapter(int resId) {
        super(resId);
    }

    @Override
    public RecyViewHolder buildViewHolder(View itemView) {
        return new RecyViewHolder(itemView);
    }

    @Override
    public void bindDataToHolder(RecyViewHolder holder, RandomEntity entity, int position) {
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp == null)
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.height = entity.getHeight();
        holder.itemView.setLayoutParams(lp);
        holder.text.setText(entity.getTitle());
    }

    public static class RecyViewHolder extends BasicRecyViewHolder {

        TextView text;

        public RecyViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.gridFlow_title);
        }

    }

}
