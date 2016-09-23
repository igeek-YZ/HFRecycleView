package com.igeek.hfrecyleviewlib;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.igeek.hfrecyleviewlib.utils.DensityUtils;

public class DeafultRefreshView extends LinearLayout implements IPullRefreshView {

    private static final int ANIMATION_DURATION = 150;
    private static final Interpolator ANIMATION_INTERPOLATOR = new DecelerateInterpolator();

    private Animation mRotateAnimation;
    private Animation mResetRotateAnimation;
    private ImageView icon;
    private TextView text;

    public DeafultRefreshView(Context context) {
        this(context, null);
    }

    public DeafultRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);

        if(icon==null){
            LayoutParams lp=new LayoutParams(DensityUtils.dp2px(20), DensityUtils.dp2px(20));
            icon=new ImageView(getContext());
            icon.setImageResource(R.drawable.default_ptr_flip);
            addView(icon,lp);
        }

        if(text==null){
            LayoutParams lp=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.leftMargin=DensityUtils.dp2px(8);
            text=new TextView(getContext());
            text.setTextSize(14);
            text.setTextColor(Color.BLACK);
            text.setText(R.string.pulling);
            addView(text,lp);
        }

        initAnimation();
    }

    private void initAnimation() {
        mRotateAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
        mRotateAnimation.setDuration(ANIMATION_DURATION);
        mRotateAnimation.setFillAfter(true);

        mResetRotateAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mResetRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
        mResetRotateAnimation.setDuration(ANIMATION_DURATION);
        mResetRotateAnimation.setFillAfter(true);
    }

    @Override
    public void onPullHided() {
        icon.setVisibility(VISIBLE);
        icon.clearAnimation();
        icon.setImageDrawable(getResources().getDrawable(R.drawable.default_ptr_flip));
        text.setText(R.string.pulling);
    }

    @Override
    public void onPullRefresh() {
        icon.setVisibility(VISIBLE);
        icon.clearAnimation();
        AnimationDrawable drawable = (AnimationDrawable) getResources().getDrawable(R.drawable.spinner);
        drawable.start();
        icon.setImageDrawable(drawable);
        text.setText(R.string.pulling_refreshing);
    }

    @Override
    public void onPullFreeHand() {
        icon.setVisibility(VISIBLE);
        icon.clearAnimation();
        if (icon.getAnimation() == null || icon.getAnimation() == mResetRotateAnimation) {
            icon.startAnimation(mRotateAnimation);
        }
        text.setText(R.string.pulling_refresh);
    }

    @Override
    public void onPullDowning() {
        icon.setVisibility(VISIBLE);
        icon.clearAnimation();
        icon.setImageDrawable(getResources().getDrawable(R.drawable.default_ptr_flip));
        text.setText(R.string.pulling);
    }

    @Override
    public void onPullFinished() {
        icon.setVisibility(GONE);
        text.setText(R.string.pulling_refreshfinish);
    }

    @Override
    public void onPullProgress(float pullDistance, float pullProgress) {

    }
}
