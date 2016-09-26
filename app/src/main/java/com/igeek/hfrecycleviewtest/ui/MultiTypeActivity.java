package com.igeek.hfrecycleviewtest.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.igeek.hfrecycleviewtest.R;
import com.igeek.hfrecyleviewlib.BasicRecyViewHolder;
import com.igeek.hfrecyleviewlib.HFGridMultiTypeGapDecoration;
import com.igeek.hfrecyleviewlib.HFMultiTypeRecyAdapter;
import com.igeek.hfrecyleviewlib.HolderTypeData;
import com.igeek.hfrecyleviewlib.NestedRefreshLayout;

import java.util.Timer;
import java.util.TimerTask;

import Utils.DemoUtils;
import adapter.TestHFGridMuiltTypeSpanSizeLookup;
import bean.RmListType1;

public class MultiTypeActivity extends Activity implements
        BasicRecyViewHolder.OnItemClickListener{

    RecyclerView recyclerView;
    NestedRefreshLayout refreshLayout;
    HFMultiTypeRecyAdapter adapter;

    View loadingView;
    View nodataView;
    View buttomView;
    View topView;

    public static final int type1=100;
    public static final int type2=101;
    public static final int type3=102;
    public static final int type4=103;
    public static final int type5=104;
    public static final int type6=105;
    public static final int type7=106;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        refreshLayout= (NestedRefreshLayout) findViewById(R.id.refreshLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        loadingView = getLayoutInflater().inflate(R.layout.layout_listbottom_loadingview, null);
        nodataView = getLayoutInflater().inflate(R.layout.layout_list_nodata, null);
        topView = getLayoutInflater().inflate(R.layout.layout_recommend_topview, null);
        buttomView = getLayoutInflater().inflate(R.layout.layout_recommend_buttomview, null);

        if (adapter == null) {
            adapter = new HFMultiTypeRecyAdapter();
            //添加头部
            adapter.setHeadView(topView);
            adapter.setFootView(buttomView);
            //添加item的点击事件
            adapter.setItemClickListener(this);
            adapter.addSubViewListener(R.id.rmTypeView1_more,moreListener);
            adapter.addSubViewListener(R.id.rmTypeView5_bigCoverImg,view5_bigCoverImgListener);
            adapter.addSubViewListener(R.id.rmTypeView5_smailCoverImg1,view5_coverImg1Listener);
            adapter.addSubViewListener(R.id.rmTypeView5_smailCoverImg2,view5_coverImg2Listener);
            adapter.addSubViewListener(R.id.rmTypeView5_smailCoverImg3,view5_coverImg3Listener);
            adapter.addFootSubViewListener(R.id.rmButtomView_img,buttomImgListener);
        }
        recyclerView.setAdapter(adapter);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        TestHFGridMuiltTypeSpanSizeLookup spanSizeLookup=new TestHFGridMuiltTypeSpanSizeLookup();
        spanSizeLookup.setAdapter(adapter);
        spanSizeLookup.setLayoutManager(gridLayoutManager);
        gridLayoutManager.setSpanSizeLookup(spanSizeLookup);
        recyclerView.setLayoutManager(gridLayoutManager);
        HFGridMultiTypeGapDecoration gapDecoration=new HFGridMultiTypeGapDecoration(8);
        gapDecoration.setOffsetTopEnabled(false);
        recyclerView.addItemDecoration(gapDecoration);
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
        },1000);
    }

    android.os.Handler handler=new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter.refreshDatas(DemoUtils.buildDemoList());
        }
    };

    View.OnClickListener moreListener=new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            int postion=(int) view.getTag();
            int type=  adapter.getData(postion).getType();
            if( type==type1){
                RmListType1 type1=(RmListType1) adapter.getData(postion);
                Toast.makeText(MultiTypeActivity.this,"【"+type1.getData().getTitle()+"】更多",Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void OnItemClick(View v, int adapterPosition) {
        HolderTypeData typeData=adapter.getData(adapter.getPositon(adapterPosition));
        switch (typeData.getType()){
            case type1:
                break;
            case type2:
                break;
            case type3:
                break;
            case type4:
                break;
            case type5:
                break;
            case type6:
                break;
            case type7:
                break;
        }
        Toast.makeText(this,"position="+adapter.getPositon(adapterPosition),Toast.LENGTH_SHORT).show();
    }

    View.OnClickListener view5_bigCoverImgListener=new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Toast.makeText(MultiTypeActivity.this,"原创独家的大图片广告",Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener view5_coverImg1Listener=new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Toast.makeText(MultiTypeActivity.this,"原创独家的第一张小图片广告",Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener view5_coverImg2Listener=new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Toast.makeText(MultiTypeActivity.this,"原创独家的第二张小图片广告",Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener view5_coverImg3Listener=new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Toast.makeText(MultiTypeActivity.this,"原创独家的第三张小图片广告",Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener buttomImgListener=new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Toast.makeText(MultiTypeActivity.this,"推荐列表底部的广告图片",Toast.LENGTH_SHORT).show();
        }
    };
}
