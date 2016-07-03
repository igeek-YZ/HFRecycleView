package com.igeek.hfrecycleviewtest.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.igeek.hfrecycleviewtest.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
    }

    public void headfoot(View view){
        startActivity(new Intent(this,HeadFootActivity.class));
    }

    public void foot(View view){
        startActivity(new Intent(this,FootActivity.class));
    }

    public void grid(View view){
        startActivity(new Intent(this,GridActivity.class));
    }

    public void gridFlow(View view){
        startActivity(new Intent(this,GridFlowAcitvity.class));
    }

}
