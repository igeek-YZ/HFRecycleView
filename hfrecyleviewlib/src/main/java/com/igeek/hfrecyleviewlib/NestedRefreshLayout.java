package com.igeek.hfrecyleviewlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;

public class NestedRefreshLayout extends ViewGroup
        implements NestedScrollingParent, NestedScrollingChild {

    private static final int INVALID_INDEX = -1;
    private static final int INVALID_POINTER = -1;

    //滑动时候的弹性因子
    private static final float DRAG_RATE = .5f;
    //减速插值因子
    private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;

    //刷新视图的高度
    private int pullViewHeight;
    //刷新的临界高度
    private int refreshHeight;
    //默认的滚动到刷新或者隐藏时候的动画时间
    private int animDuration;

    // NestedScroll
    private float mTotalUnconsumed;
    //每次嵌套滑动的 dx，dy
    private final int[] consumeds = new int[2];
    private final NestedScrollingChildHelper childHelper;
    private final NestedScrollingParentHelper parentHelper;

    //whether to remind the callback listener(OnRefreshListener)
    private boolean mNotify;
    private boolean mRefreshing;
    //手指是否处于屏幕滑动状态
    private boolean mIsBeingDragged;
    private boolean mIsFitRefreshing;
    private boolean mReturningToStart;

    private int mRefreshViewIndex = INVALID_INDEX;
    private int mActivePointerId = INVALID_POINTER;
    private int mAnimateDuration = animDuration;

    private int mFrom;
    private int mTouchSlop;
    private int mCurrentScrollOffset;

    private float mInitialDownY;
    private float mInitialMotionY;
    private float mRefreshTargetOffset;

    private View mTarget;
    private View mPullView;

    private IPullRefreshView mIRefreshStatus;
    private OnRefreshListener mOnRefreshListener;

    private Interpolator mInterpolator;

    private final Animation mAnimateToRefreshingAnimation = new Animation() {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int targetEnd = (int) mRefreshTargetOffset;
            int targetTop = (int) (mFrom + (targetEnd - mFrom) * interpolatedTime);

            scrollTargetOffset(0, -mCurrentScrollOffset - targetTop);
        }
    };

    private final Animation mAnimateToStartAnimation = new Animation() {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int targetEnd = 0;
            int targetTop = (int) (mFrom + (targetEnd - mFrom) * interpolatedTime);
            scrollTargetOffset(0, -mCurrentScrollOffset - targetTop);
        }
    };

    private final Animation.AnimationListener mRefreshingListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            mIRefreshStatus.onPullRefreshing();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (mNotify) {
                if (mOnRefreshListener != null) {
                    mOnRefreshListener.onRefresh();
                }
            }
        }
    };

    private final Animation.AnimationListener mResetListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mIRefreshStatus.onPullHided();
            mRefreshing = false;
            mReturningToStart = false;
        }
    };

    public NestedRefreshLayout(Context context) {
        this(context, null);
    }

    public NestedRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta=context.obtainStyledAttributes(attrs,R.styleable.NestedRefreshLayout);

        pullViewHeight=ta.getDimensionPixelSize(R.styleable.NestedRefreshLayout_pullViewHeight,0);
        refreshHeight=ta.getDimensionPixelSize(R.styleable.NestedRefreshLayout_refreshHeight,0);
        animDuration=ta.getInt(R.styleable.NestedRefreshLayout_animPlayDuration,300);

        ta.recycle();

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);

        parentHelper = new NestedScrollingParentHelper(this);
        childHelper = new NestedScrollingChildHelper(this);

        setWillNotDraw(false);
        setNestedScrollingEnabled(true);
        ViewCompat.setChildrenDrawingOrderEnabled(this, true);

        onCreateRefreshView(pullViewHeight,new DeafultRefreshView(context));
    }

    private void onCreateRefreshView(int pullViewHeight,View pullView) {

        if (pullView==null||mPullView == pullView)  return;

        if (mPullView != null && mPullView.getParent() != null) {
            ((ViewGroup) mPullView.getParent()).removeView(mPullView);
        }

        mPullView = pullView;

        if (pullView instanceof IPullRefreshView) {
            mIRefreshStatus = (IPullRefreshView) pullView;
        } else {
            throw new ClassCastException("the refreshView must implement the interface IPullRefreshView");
        }

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, pullViewHeight);
        addView(mPullView, layoutParams);
    }

    public void setRefreshView(View refreshView){
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, pullViewHeight);
        setRefreshView(refreshView,layoutParams);
    }

    public void setRefreshView(View refreshView, LayoutParams layoutParams) {

        if (mPullView == refreshView) {
            return;
        }

        if (mPullView != null && mPullView.getParent() != null) {
            ((ViewGroup) mPullView.getParent()).removeView(mPullView);
        }

        mPullView = refreshView;

        if (mPullView instanceof IPullRefreshView) {
            mIRefreshStatus = (IPullRefreshView) mPullView;
        } else {
            throw new ClassCastException("the refreshView must implement the interface IPullRefreshView");
        }
        addView(mPullView, layoutParams);
    }

    public void setInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
    }

    public void setAnimateDuration(int duration) {
        mAnimateDuration = duration;
    }

    public void setRefreshTargetOffset(float refreshTargetOffset) {
        mRefreshTargetOffset = refreshTargetOffset;
        requestLayout();
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (mRefreshViewIndex < 0) {
            return i;
        } else if (i == childCount - 1) {
            return mRefreshViewIndex;
        } else if (i >= mRefreshViewIndex) {
            return i + 1;
        } else {
            return i;
        }
    }

    // NestedScrollingParent
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        if (isEnabled() && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0) {
            startNestedScroll(nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL);
            return true;
        }
        return false;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        parentHelper.onNestedScrollAccepted(child, target, axes);
        mTotalUnconsumed = 0;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (dy > 0 && mTotalUnconsumed > 0) {
            if (dy > mTotalUnconsumed) {
                consumed[1] = dy - (int) mTotalUnconsumed;
                mTotalUnconsumed = 0;
            } else {
                mTotalUnconsumed -= dy;
                consumed[1] = dy;
            }
            moveSpinner(mTotalUnconsumed);
        }

        final int[] parentConsumed = consumeds;
        if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {
            consumed[0] += parentConsumed[0];
            consumed[1] += parentConsumed[1];
        }
    }

    @Override
    public int getNestedScrollAxes() {
        return parentHelper.getNestedScrollAxes();
    }

    @Override
    public void onStopNestedScroll(View target) {
        parentHelper.onStopNestedScroll(target);
        if (mTotalUnconsumed > 0) {
            finishSpinner(mTotalUnconsumed);
            mTotalUnconsumed = 0;
        }
        stopNestedScroll();
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed,
                               int dyUnconsumed) {
        if (dyUnconsumed < 0) {
            dyUnconsumed = Math.abs(dyUnconsumed);
            mTotalUnconsumed += dyUnconsumed;
            moveSpinner(mTotalUnconsumed);
        }
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dxConsumed, null);
    }

    // NestedScrollingChild
    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        childHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return childHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return childHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        childHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return childHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow) {
        return childHelper.dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return childHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return childHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return childHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (getChildCount() == 0) {
            return;
        }

        ensureTarget();
        if (mTarget == null) {
            return;
        }

        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        final int childTop = getPaddingTop();
        final int childLeft = getPaddingLeft();
        final int childWidth = width - getPaddingLeft() - getPaddingRight();
        final int childHeight = height - getPaddingTop() - getPaddingBottom();

        mTarget.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);

        if (mRefreshTargetOffset < mPullView.getHeight()) {
            mRefreshTargetOffset = mPullView.getHeight();
        }

        int offsetTop = (int) -(mRefreshTargetOffset - (mRefreshTargetOffset - mPullView.getMeasuredHeight()) / 2);

        mPullView.layout((width / 2 - mPullView.getMeasuredWidth() / 2), offsetTop,
                (width / 2 + mPullView.getMeasuredWidth() / 2), offsetTop + mPullView.getMeasuredHeight());
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        ensureTarget();
        if (mTarget == null) {
            return;
        }

        mTarget.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY));
        mPullView.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(mPullView.getLayoutParams().height, MeasureSpec.EXACTLY));

        mRefreshViewIndex = -1;
        for (int index = 0; index < getChildCount(); index++) {
            if (getChildAt(index) == mPullView) {
                mRefreshViewIndex = index;
                break;
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        ensureTarget();
        if (mTarget == null) {
            return false;
        }

        if (mRefreshing || mReturningToStart) {
            return true;
        }

        if (!isEnabled() || canChildScrollUp(mTarget)) {
            return false;
        }

        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                scrollTargetOffset(0, 0);
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                mIsBeingDragged = false;

                float initialDownY = getMotionEventY(ev, mActivePointerId);
                if (initialDownY == -1) {
                    return false;
                }

                mInitialDownY = initialDownY;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }

                float activeMoveY = getMotionEventY(ev, mActivePointerId);
                if (activeMoveY == -1) {
                    return false;
                }

                initDragStatus(activeMoveY);
                break;

            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
            default:
                break;
        }

        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        ensureTarget();
        if (mTarget == null) {
            return false;
        }

        if (mRefreshing || mReturningToStart) {
            return true;
        }

        if (!isEnabled() || canChildScrollUp(mTarget)) {
            return false;
        }

        final int action = ev.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                mIsBeingDragged = false;
                break;

            case MotionEvent.ACTION_MOVE: {
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }

                final float activeMoveY = getMotionEventY(ev, mActivePointerId);
                if (activeMoveY == -1) {
                    return false;
                }

                final float overScrollY = (activeMoveY - mInitialMotionY) * DRAG_RATE;

                if (mIsBeingDragged) {
                    if (overScrollY > 0) {
                        moveSpinner(overScrollY);
                    } else {
                        return false;
                    }
                } else {
                    initDragStatus(activeMoveY);
                }
                break;
            }

            case MotionEventCompat.ACTION_POINTER_DOWN: {
                final int index = MotionEventCompat.getActionIndex(ev);
                mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                break;
            }

            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }

                final float activeMoveY = getMotionEventY(ev, mActivePointerId);
                if (activeMoveY == -1) {
                    mIsBeingDragged = false;
                    mActivePointerId = INVALID_POINTER;
                    return false;
                }

                if (!mIsBeingDragged) {
                    return false;
                }

                final float overScrollTop = (activeMoveY - mInitialMotionY) * DRAG_RATE;

                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;

                finishSpinner(overScrollTop);
                return false;
            }
            default:
                break;
        }

        return true;
    }

    public void setRefreshing(boolean refreshing) {
        if (refreshing && mRefreshing != refreshing) {
            mRefreshing = refreshing;
            scrollTargetOffset(0, 0);
            mNotify = false;

            animateToRefreshingPosition(-mCurrentScrollOffset, mRefreshingListener);
        } else {
            setRefreshing(refreshing, false,0);
        }
    }

    private void setRefreshing(boolean refreshing, final boolean notify,int delay) {
        if (mRefreshing != refreshing) {
            mNotify = notify;
            mRefreshing = refreshing;
            if (refreshing) {
                animateToRefreshingPosition(-mCurrentScrollOffset, mRefreshingListener);
            } else {
                animateOffsetToStartPosition(-mCurrentScrollOffset, mResetListener,delay);
            }
        }
    }

    public void refreshFinish(){
        if(mIRefreshStatus!=null&&mRefreshing != false){
            mIRefreshStatus.onPullRefreshFinished();
            setRefreshing(false,false,1000);
        }
    }

    private void initDragStatus(float activeMoveY) {
        float diff = activeMoveY - mInitialDownY;
        if (!mIsBeingDragged && diff > mTouchSlop) {
            mInitialMotionY = mInitialDownY + diff;
            mIsBeingDragged = true;
        }
    }

    private void animateOffsetToStartPosition(int from, Animation.AnimationListener listener, int delay) {
        mFrom = from;
        mAnimateToStartAnimation.reset();
        mAnimateToStartAnimation.setStartOffset(delay);
        mAnimateToStartAnimation.setDuration(mAnimateDuration);
        mAnimateToStartAnimation.setInterpolator(mInterpolator);
        if (listener != null) {
            mAnimateToStartAnimation.setAnimationListener(listener);
        }
        startAnimation(mAnimateToStartAnimation);
    }

    private void animateToRefreshingPosition(int from, Animation.AnimationListener listener) {
        mFrom = from;

        mAnimateToRefreshingAnimation.reset();
        mAnimateToRefreshingAnimation.setDuration(mAnimateDuration);
        mAnimateToRefreshingAnimation.setInterpolator(mInterpolator);

        if (listener != null) {
            mAnimateToRefreshingAnimation.setAnimationListener(listener);
        }

        clearAnimation();
        startAnimation(mAnimateToRefreshingAnimation);
    }

    private void moveSpinner(float overScrollTop) {
        float originalDragPercent = overScrollTop / mRefreshTargetOffset;
        float dragPercent = Math.min(1f, Math.abs(originalDragPercent));
        float slingshotDist = mRefreshTargetOffset;
        float extraOS = Math.abs(overScrollTop) - mRefreshTargetOffset;
        float tensionSlingshotPercent = Math.max(0, Math.min(extraOS, slingshotDist * 2.5f) / slingshotDist);
        float tensionPercent = (float) ((tensionSlingshotPercent / 4) -
                Math.pow((tensionSlingshotPercent / 4), 2)) * 2f;
        float extraMove = (slingshotDist) * tensionPercent * 2;

        int targetY = (int) ((slingshotDist * dragPercent) + extraMove);

        if (mPullView.getVisibility() != View.VISIBLE) {
            mPullView.setVisibility(View.VISIBLE);
        }

        if (overScrollTop > mRefreshTargetOffset && !mIsFitRefreshing) {
            mIsFitRefreshing = true;
            mIRefreshStatus.onPullToRefresh();
        } else if (overScrollTop <= mRefreshTargetOffset && mIsFitRefreshing) {
            mIsFitRefreshing = false;
            mIRefreshStatus.releaseToRefresh();
        }

        scrollTargetOffset(0, -mCurrentScrollOffset - targetY);
    }

    private void finishSpinner(float overScrollTop) {
        mReturningToStart = true;

        if (overScrollTop > mRefreshTargetOffset) {
            setRefreshing(true, true,0);
        } else {
            mRefreshing = false;
            animateOffsetToStartPosition(-mCurrentScrollOffset, mResetListener,0);
        }
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = MotionEventCompat.getActionIndex(ev);
        int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);

        if (pointerId == mActivePointerId) {
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
        }
    }

    private void scrollTargetOffset(int offsetX, int offsetY) {
        mPullView.bringToFront();
        scrollBy(offsetX, offsetY);
        mCurrentScrollOffset = getScrollY();

        mIRefreshStatus.pullProgress(-mCurrentScrollOffset, -mCurrentScrollOffset / mRefreshTargetOffset);
    }

    private float getMotionEventY(MotionEvent ev, int activePointerId) {
        final int index = MotionEventCompat.findPointerIndex(ev, activePointerId);
        if (index < 0) {
            return -1;
        }
        return MotionEventCompat.getY(ev, index);
    }

    public boolean canChildScrollUp(View mTarget) {
        if (mTarget == null) {
            return false;
        }

        if (android.os.Build.VERSION.SDK_INT < 14 && mTarget instanceof AbsListView) {
            final AbsListView absListView = (AbsListView) mTarget;
            return absListView.getChildCount() > 0
                    && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                    .getTop() < absListView.getPaddingTop());
        }

        if (mTarget instanceof ViewGroup) {
            int childCount = ((ViewGroup) mTarget).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = ((ViewGroup) mTarget).getChildAt(i);
                if (canChildScrollUp(child)) {
                    return true;
                }
            }
        }

        return ViewCompat.canScrollVertically(mTarget, -1) || mTarget.getScrollY() > 0;
    }

    private void ensureTarget() {
        if (!isTargetValid()) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (!child.equals(mPullView)) {
                    mTarget = child;
                    break;
                }
            }
        }
    }

    public boolean isTargetValid() {
        for (int i = 0; i < getChildCount(); i++) {
            if (mTarget == getChildAt(i)) {
                return true;
            }
        }

        return false;
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefreshListener = listener;
    }

    public interface OnRefreshListener {
        void onRefresh();
    }
}
