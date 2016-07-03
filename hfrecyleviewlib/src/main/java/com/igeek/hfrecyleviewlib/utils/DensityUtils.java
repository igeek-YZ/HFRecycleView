package com.igeek.hfrecyleviewlib.utils;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * 常用单位转换的辅助类
 * Created by igeek2014@hotmail.com on 2015/4/16.
 */
public class DensityUtils {

    private DensityUtils() {
        throw new UnsupportedOperationException("单位工具类不能初始化对象");
    }

    /**
     * dp转px
     *
     * @param dpVal
     * @return
     */
    public static int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, Resources.getSystem().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param spVal
     * @return
     */
    public static int sp2px(float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, Resources.getSystem().getDisplayMetrics());
    }

    /**
     * px转dp
     *
     * @param pxVal
     * @return
     */
    public static float px2dp(float pxVal) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     *
     * @param pxVal
     * @return
     */
    public static float px2sp(float pxVal) {
        return (pxVal / Resources.getSystem().getDisplayMetrics().scaledDensity);
    }


}
