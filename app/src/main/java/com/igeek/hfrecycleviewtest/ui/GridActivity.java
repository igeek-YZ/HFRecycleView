package com.igeek.hfrecycleviewtest.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.igeek.hfrecycleviewtest.R;
import com.igeek.hfrecyleviewlib.BasicRecyViewHolder;
import com.igeek.hfrecyleviewlib.HFGridSpanSizeLookup;
import com.igeek.hfrecyleviewlib.HFGridVerDecoration;
import com.igeek.hfrecyleviewlib.NestedRefreshLayout;
import com.igeek.hfrecyleviewlib.RecycleScrollListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import adapter.TestSingleFHFSingleTypeRecyAdapter;


public class GridActivity extends Activity implements
        BasicRecyViewHolder.OnItemClickListener,
        BasicRecyViewHolder.OnItemLongClickListener,
        BasicRecyViewHolder.OnHeadViewClickListener,
        BasicRecyViewHolder.OnFootViewClickListener {

    RecyclerView recyclerView;
    NestedRefreshLayout refreshLayout;
    TestSingleFHFSingleTypeRecyAdapter adapter;

    View loadingView;
    View nodataView;
    View topView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        refreshLayout= (NestedRefreshLayout) findViewById(R.id.refreshLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        loadingView = getLayoutInflater().inflate(R.layout.layout_listbottom_loadingview, null);
        nodataView = getLayoutInflater().inflate(R.layout.layout_list_nodata, null);
        topView = getLayoutInflater().inflate(R.layout.layout_topview, null);
        if (adapter == null) {
            adapter = new TestSingleFHFSingleTypeRecyAdapter(R.layout.layout_grid_item);
            adapter.setHeadView(topView);
            adapter.setFootView(loadingView);
            adapter.setItemClickListener(this);
            adapter.setItemLongClickListener(this);
            adapter.setHeadClickListener(this);
            adapter.setFootClickListener(this);
            adapter.addSubViewListener(R.id.item_btn, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(GridActivity.this," 你点击了第 "+view.getTag().toString()+" 个button",Toast.LENGTH_LONG).show();
                }
            });
        }
        recyclerView.addItemDecoration(new HFGridVerDecoration());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        GridLayoutManager manager=new GridLayoutManager(this,2);
        HFGridSpanSizeLookup spanSizeLookup=new HFGridSpanSizeLookup();
        spanSizeLookup.setAdapter(adapter);
        spanSizeLookup.setLayoutManager(manager);
        manager.setSpanSizeLookup(spanSizeLookup);
        recyclerView.setLayoutManager(manager);
        recyclerView.addOnScrollListener(srcollListener);
        handler.sendEmptyMessageDelayed(0,1000);
        refreshLayout.setOnRefreshListener(new NestedRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFinish();
            }
        });
    }

    public void refreshFinish(){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.refreshFinish();
                    }
                });
            }
        },1500);
    }

    @Override
    public void OnItemClick(View v, int adapterPosition) {
        //adapterPosition 的位置不一定是数据集当中的位置 获取真实的位置通过  adapter.getPositon(adapterPosition) 获得
        Toast.makeText(this, "你点击了第 "+adapter.getPositon(adapterPosition)+" 个数据item", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnItemLongClick(View v, int adapterPosition) {
        //adapterPosition 的位置不一定是数据集当中的位置 获取真实的位置通过  adapter.getPositon(adapterPosition) 获得
        Toast.makeText(this, "你长按了第 "+adapter.getPositon(adapterPosition)+" 个数据item", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReCycleFootClick(View view, View clickView) {
        Toast.makeText(this, "你点击了底部 footView", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecycleHeadClick(View view, View clickView) {
        Toast.makeText(this, "你点击了顶部 headView", Toast.LENGTH_SHORT).show();
    }

    public RecycleScrollListener srcollListener = new RecycleScrollListener() {
        @Override
        public void loadMore() {
            if (adapter.getDatas().size() > 30) {
                adapter.updateFootView(nodataView);
            } else {
                handler.sendEmptyMessageDelayed(0,2000);
            }
        }


        @Override
        public void refresh() {

        }
    };

    android.os.Handler handler=new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter.appendDatas(buildListByPosition(adapter.getDatas().size()));
            srcollListener.finished();
        }
    };

    public List<String> buildListByPosition(int position) {

        List<String> titles = new ArrayList<String>();

        int target=position+10;

        for (;position < target; position++) {
            titles.add("position " + position);
        }

        return titles;
    }

}
