package com.igeek.hfrecycleviewtest.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.igeek.hfrecycleviewtest.R;
import com.igeek.hfrecyleviewlib.BasicRecyViewHolder;
import com.igeek.hfrecyleviewlib.HFLineVerComDecoration;
import com.igeek.hfrecyleviewlib.HolderTypeData;
import com.igeek.hfrecyleviewlib.RecycleScrollListener;

import java.util.ArrayList;
import java.util.List;

import adapter.TestMulitTypeRecyAdapter;
import bean.TypeData1;
import bean.TypeData2;
import bean.TypeData3;
import bean.TypeData4;

public class mulitTypeActivity extends Activity implements
        BasicRecyViewHolder.OnItemClickListener,
        BasicRecyViewHolder.OnItemLongClickListener,
        BasicRecyViewHolder.OnHeadViewClickListener,
        BasicRecyViewHolder.OnFootViewClickListener {

    RecyclerView recyclerView;
    TestMulitTypeRecyAdapter adapter;

    View loadingView;
    View nodataView;
    View topView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        loadingView = getLayoutInflater().inflate(R.layout.layout_listbottom_loadingview, null);
        nodataView = getLayoutInflater().inflate(R.layout.layout_list_nodata, null);
        topView = getLayoutInflater().inflate(R.layout.layout_topview, null);
        if (adapter == null) {
            adapter = new TestMulitTypeRecyAdapter();
            //添加头部
            adapter.setHeadView(topView);
            //添加底部
            adapter.setFootView(loadingView);
            //添加item的点击事件
            adapter.setItemClickListener(this);
            //添加item的长按事件
            adapter.setItemLongClickListener(this);
            //添加头部的点击事件
            adapter.setHeadClickListener(this);
            //添加底部的点击事件
            adapter.setFootClickListener(this);
            //处理item当中子视图的点击事件
            adapter.addSubViewListener(R.id.item_btn, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mulitTypeActivity.this, " 你点击了第 " + view.getTag().toString() + " 个button", Toast.LENGTH_SHORT).show();
                }
            });
            //处理头部当中子视图的点击事件
            adapter.addHeadSubViewListener(R.id.topview_text, headlistener);
            //处理底部当中子视图的点击事件
            adapter.addFootSubViewListener(R.id.nodataview_text, footlistener);
        }
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(srcollListener);
        recyclerView.addItemDecoration(new HFLineVerComDecoration(1, getResources().getColor(R.color.colorAccent)));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.refreshDatas(buildListByPosition(0));

    }

    View.OnClickListener headlistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(mulitTypeActivity.this, " 你点击了顶部headView当中的文本", Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener footlistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(mulitTypeActivity.this, " 你点击了底部footView当中的文本", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void OnItemClick(View v, int adapterPosition) {
        //adapterPosition 的位置不一定是数据集当中的位置 获取真实的位置通过  adapter.getPositon(adapterPosition) 获得
        Toast.makeText(this, "你点击了第 " + adapter.getPositon(adapterPosition) + " 个数据item", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnItemLongClick(View v, int adapterPosition) {
        //adapterPosition 的位置不一定是数据集当中的位置 获取真实的位置通过  adapter.getPositon(adapterPosition) 获得
        Toast.makeText(this, "你长按了第 " + adapter.getPositon(adapterPosition) + " 个数据item", Toast.LENGTH_SHORT).show();
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
            if (adapter.getDatas().size() > 20) {
                adapter.updateFootView(nodataView);
            } else {
                handler.sendEmptyMessageDelayed(0, 2000);
            }
        }


        @Override
        public void refresh() {

        }
    };

    android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter.appendDatas(buildListByPosition(adapter.getDatas().size()));
            srcollListener.finished();
        }
    };

    public List<HolderTypeData> buildListByPosition(int position) {

        List<HolderTypeData> datas = new ArrayList<HolderTypeData>();

        int target = position + 10;

        for (; position < target; position++) {

            HolderTypeData data;

            if (position % 2 == 0) {
                data = new TypeData2("typedata2", "第三种视图的标题_" + position);
            } else if (position % 3 == 0) {
                data = new TypeData3("typedata3", "第三种视图的标题_" + position);
            } else if (position % 5 == 0) {
                data = new TypeData4("typedata4", buildResIds());
            } else {
                data = new TypeData1("book", "第一种视图的标题_" + position);
            }

            datas.add(data);
        }

        return datas;
    }

    public List<Integer> buildResIds() {
        ArrayList<Integer> resIds = new ArrayList<Integer>();
        for (int index = 0; index < 4; index++) {
            resIds.add(R.mipmap.ic_test2);
        }

        return resIds;
    }


}
