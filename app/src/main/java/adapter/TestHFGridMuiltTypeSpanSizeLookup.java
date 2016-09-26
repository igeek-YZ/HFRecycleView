package adapter;

import android.support.v7.widget.GridLayoutManager;

import com.igeek.hfrecycleviewtest.ui.MultiTypeActivity;
import com.igeek.hfrecyleviewlib.BasicHFRecyAdapter;

public class TestHFGridMuiltTypeSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    private GridLayoutManager layoutManager;
    private BasicHFRecyAdapter adapter;

    @Override
    public int getSpanSize(int position) {
        if (layoutManager != null && adapter != null) {
            if (adapter.needMatchParentWidth(position)) {
                return layoutManager.getSpanCount();
            } else {
                final int type = adapter.getItemViewType(position);
                if (type == MultiTypeActivity.type1) {
                    return layoutManager.getSpanCount();
                } else if (type == MultiTypeActivity.type2) {
                    return layoutManager.getSpanCount();
                } else if (type == MultiTypeActivity.type3) {
                    return layoutManager.getSpanCount();
                } else if (type == MultiTypeActivity.type4) {
                    return 1;
                }else if (type == MultiTypeActivity.type5) {
                    return layoutManager.getSpanCount();
                }else if (type == MultiTypeActivity.type6) {
                    return layoutManager.getSpanCount();
                }else if (type == MultiTypeActivity.type7) {
                    return 1;
                }
            }
        }
        return 1;
    }

    public void setLayoutManager(GridLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public BasicHFRecyAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(BasicHFRecyAdapter adapter) {
        this.adapter = adapter;
    }
}
