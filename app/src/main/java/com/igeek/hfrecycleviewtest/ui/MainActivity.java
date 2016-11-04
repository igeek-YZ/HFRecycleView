package com.igeek.hfrecycleviewtest.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

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

    public void mulitype(View view){
        startActivity(new Intent(this,MultiTypeActivity.class));
    }

    public void animStart(View view){


        Animation animation=new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
            }
        };

        animation.setDuration(400);
        view.startAnimation(animation);
    }

    public static abstract class PullAnimation extends Animation {
        private int from;
        private int to;
        private int animDuration;

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int targetEnd = to;
            int targetTop = (int) (from + (targetEnd - from) * interpolatedTime);
            applyTransformationTop(targetTop);
        }

        public abstract void applyTransformationTop(int targetTop);

        public int getFrom() {
            return from;
        }

        public void setFrom(int from) {
            this.from = from;
        }

        public int getTo() {
            return to;
        }

        public void setTo(int to) {
            this.to = to;
        }

        public int getAnimDuration() {
            return animDuration;
        }

        public void setAnimDuration(int animDuration) {
            this.animDuration = animDuration;
        }
    }

}
