package widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.igeek.hfrecycleviewtest.R;

public class AspectRatioRelativeLayout extends RelativeLayout {

    private float aspectRatio;

    public AspectRatioRelativeLayout(Context context) {
        this(context,null);
    }

    public AspectRatioRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AspectRatioRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.AspectRatioRelativeLayout);
        aspectRatio=ta.getFloat(R.styleable.AspectRatioRelativeLayout_aspectRatio,-1);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(aspectRatio!=-1){
            int widthSize=MeasureSpec.getSize(widthMeasureSpec);
            heightMeasureSpec=MeasureSpec.makeMeasureSpec((int) (widthSize*aspectRatio),MeasureSpec.EXACTLY);
            setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
        }
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }
}
