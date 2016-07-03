# HFRecycleView

##带头部(headView)和底部(footView)的RecycleView。分装了recycleview.adater里面的初始化和构建。更加便捷的使用的recycleview

#### 效果图 

<img src="https://github.com/igeek-YZ/HFRecycleView/blob/master/hfrecycleview.gif" width = "338" height = "693" alt="554" align=center />

###xml文件

	<?xml version="1.0" encoding="utf-8"?>
	<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    	android:layout_width="match_parent"
    	android:layout_height="match_parent"
    	android:background="@android:color/white">

    	<android.support.v7.widget.RecyclerView
        	android:id="@+id/recycle"
        	android:layout_width="match_parent"
        	android:layout_height="match_parent" />

	</RelativeLayout>

###部分代码
	
	class HeadFootActivity extends Activity implements
        BasicRecyViewHolder.OnItemClickListener,
        BasicRecyViewHolder.OnItemLongClickListener,
        BasicRecyViewHolder.OnHeadViewClickListener,
        BasicRecyViewHolder.OnFootViewClickListener {

    	RecyclerView recyclerView;
    	TestSingleFHFSingleTypeRecyAdapter adapter;

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
            	adapter = new TestSingleFHFSingleTypeRecyAdapter(R.layout.layout_recy_item);
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
                    	Toast.makeText(HeadFootActivity.this," 你点击了第 "+view.getTag().toString()+" 个button",Toast.LENGTH_SHORT).show();
                	}
            	});
            	//处理头部当中子视图的点击事件
            	adapter.addHeadSubViewListener(R.id.topview_text, headlistener);
            	//处理底部当中子视图的点击事件
            	adapter.addFootSubViewListener(R.id.nodataview_text, footlistener);
        	}
        	recyclerView.setAdapter(adapter);
        	//添加滚动事件的监听的处理刷新和加载更多
        	recyclerView.addOnScrollListener(srcollListener);
        	recyclerView.setLayoutManager(new LinearLayoutManager(this));
        	adapter.refreshDatas(buildListByPosition(0));
    	}
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
