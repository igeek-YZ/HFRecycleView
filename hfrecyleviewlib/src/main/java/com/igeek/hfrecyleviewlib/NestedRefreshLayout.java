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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;

import com.orhanobut.logger.Logger;

public class NestedRefreshLayout extends ViewGroup
        implements NestedScrollingParent, NestedScrollingChild {

    private static final int INVALID_INDEX = -1;
    private static final int INVALID_POINTER = -1;

    //滑动时候的弹性因子
    private static final float DRAG_RATE = .5f;
    //减速插值因子
    private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;

    private int animDuration;

    //每次嵌套滑动的 dx，dy
    private final int[] mScrollConsumed = new int[2];
    private final int[] mScrollOffset = new int[2];
    private final NestedScrollingChildHelper childHelper;
    private final NestedScrollingParentHelper parentHelper;

    //whether to remind the callback listener(OnRefreshListener)
    //手指是否处于屏幕滑动状态
    private boolean mIsBeingDragged;
    private boolean isRefreshing;

    private int pullViewIndex = INVALID_INDEX;
    private int mActivePointerId = INVALID_POINTER;

    private int mTouchSlop;

    private int mLastMotionY;
    private int mNestedYOffset;

    private View nestedTarget;
    private View pullView;
    private PullViewHelper pullHelper;
    private IPullRefreshView mIRefreshStatus;
    private OnRefreshListener mOnRefreshListener;

    private Interpolator mInterpolator;
    private PullAnimation animation;

    private final Animation.AnimationListener isRefreshingListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            mIRefreshStatus.onPullRefresh();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (mOnRefreshListener != null) {
                mOnRefreshListener.onRefresh();
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
            isRefreshing = false;
            mIRefreshStatus.onPullHided();
        }
    };

    public NestedRefreshLayout(Context context) {
        this(context, null);
    }

    public NestedRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NestedRefreshLayout);

        int pullViewHeight = ta.getDimensionPixelSize(R.styleable.NestedRefreshLayout_pullViewHeight, 0);
        int pullMinViewHeight = ta.getDimensionPixelSize(R.styleable.NestedRefreshLayout_pullMinViewHeight, 0);
        int pullMaxHeight = ta.getDimensionPixelSize(R.styleable.NestedRefreshLayout_pullMaxHeight, pullViewHeight);
        int refreshHeight = ta.getDimensionPixelSize(R.styleable.NestedRefreshLayout_refreshHeight, pullViewHeight);
        animDuration = ta.getInteger(R.styleable.NestedRefreshLayout_animPlayDuration, 300);

        final int pullviewId = ta.getResourceId(R.styleable.NestedRefreshLayout_pullView, -1);

        ta.recycle();

        if (pullviewId != -1)
            pullView = LayoutInflater.from(context).inflate(pullviewId, this, false);


        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);

        parentHelper = new NestedScrollingParentHelper(this);
        childHelper = new NestedScrollingChildHelper(this);
        pullHelper = new PullViewHelper(pullViewHeight, pullMaxHeight,pullMinViewHeight,refreshHeight);

        setWillNotDraw(false);
        setNestedScrollingEnabled(true);
        ViewCompat.setChildrenDrawingOrderEnabled(this, true);

        animation=new PullAnimation() {
            @Override
            public void applyTransformationTop(int targetTop) {
                scrollTargetOffset(pullHelper.getScroll() - targetTop);
            }
        };

        addOrUpdatePullView(pullViewHeight, pullView != null ? pullView : new DeafultRefreshView(context));
    }

    private void addOrUpdatePullView(int pullViewHeight, View pullView) {

        if (pullView == null || this.pullView == pullView) return;

        if (this.pullView != null && this.pullView.getParent() != null) {
            ((ViewGroup) this.pullView.getParent()).removeView(this.pullView);
        }

        this.pullView = pullView;

        if (pullView instanceof IPullRefreshView) {
            mIRefreshStatus = (IPullRefreshView) pullView;
        } else {
            throw new ClassCastException("the refreshView must implement the interface IPullRefreshView");
        }

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, pullViewHeight);
        addView(this.pullView, 0, layoutParams);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (nestedTarget != null) {
            nestedTarget.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY));
        }

        if (pullView != null) {
            pullView.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(pullView.getLayoutParams().height, MeasureSpec.EXACTLY));
        }

        checkUpdatePullViewIndex();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        checkNestedTarget();

        if (nestedTarget == null) return;

        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        final int childTop = getPaddingTop();
        final int childLeft = getPaddingLeft();
        final int childWidth = width - getPaddingLeft() - getPaddingRight();
        final int childHeight = height - getPaddingTop() - getPaddingBottom();

        nestedTarget.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);

        int offsetTop = -pullView.getMeasuredHeight();

        pullView.layout(childLeft, offsetTop, childLeft + childWidth, offsetTop + pullView.getMeasuredHeight());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        checkNestedTarget();
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (pullViewIndex < 0) {
            return i;
        } else if (i == childCount - 1) {
            return pullViewIndex;
        } else if (i >= pullViewIndex) {
            return i + 1;
        } else {
            return i;
        }
    }

    // NestedScrollingParent

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        parentHelper.onNestedScrollAccepted(child, target, axes);
        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {

        final boolean canScrollUp= pullHelper.canScrollUp();

//        Logger.i("dy=" + dy + "\ncanScrollUp=" + canScrollUp);

        if(dy>0&&canScrollUp){
            consumed[1] = scrollMoveOffset(dy);
        }

//        final int[] parentConsumed = mScrollConsumed;
//        if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {
//            consumed[0] += parentConsumed[0];
//            consumed[1] += parentConsumed[1];
//        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed,
                               int dyUnconsumed) {

//        Logger.i("dyConsumed=" + dyConsumed + "\ndyUnconsumed=" + dyUnconsumed);

        final int myConsumed = scrollMoveOffset(dyUnconsumed);
        final int myUnconsumed = dyUnconsumed - myConsumed;
        dispatchNestedScroll(dxConsumed, myConsumed, dxUnconsumed, myUnconsumed, null);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return parentHelper.getNestedScrollAxes();
    }

    @Override
    public void onStopNestedScroll(View target) {
        parentHelper.onStopNestedScroll(target);
        stopNestedScroll();
    }

    // NestedScrollingChild

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
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
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
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (!isEnabled()) return false;

        final int action = MotionEventCompat.getActionMasked(ev);

        Logger.i("mIsBeingDragged=" + mIsBeingDragged + "\naction=" + getActionString(action));

        if ((action == MotionEvent.ACTION_MOVE) && (mIsBeingDragged)) {
            return true;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = (int) ev.getY();
                mIsBeingDragged = false;
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                break;

            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    break;
                }
                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                if (pointerIndex == -1) {
                    break;
                }

                final int y = (int) MotionEventCompat.getY(ev, pointerIndex);
                final int yDiff = Math.abs(y - mLastMotionY);

                if (yDiff > mTouchSlop && (!mIsBeingDragged)) {
                    mIsBeingDragged = true;
                    mLastMotionY = y;
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                /* Release the drag */
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                stopNestedScroll();
                break;
            default:
                break;
        }

        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        MotionEvent vtev = MotionEvent.obtain(ev);

        final int actionMasked = MotionEventCompat.getActionMasked(ev);

        Logger.i("mIsBeingDragged=" + mIsBeingDragged + "\naction=" + getActionString(actionMasked));

        if (actionMasked == MotionEvent.ACTION_DOWN) {
            mNestedYOffset = 0;
        }
        vtev.offsetLocation(0, mNestedYOffset);

        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                mIsBeingDragged = false;
                mLastMotionY = (int) ev.getY();
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                break;

            case MotionEvent.ACTION_MOVE: {
                final int activePointerIndex = MotionEventCompat.findPointerIndex(ev,
                        mActivePointerId);
                if (activePointerIndex == -1) {
                    break;
                }

                final int y = (int) MotionEventCompat.getY(ev, activePointerIndex);
                int deltaY = mLastMotionY - y;

                if (dispatchNestedPreScroll(0, deltaY, mScrollConsumed, mScrollOffset)) {
                    deltaY -= mScrollConsumed[1];
                    vtev.offsetLocation(0, mScrollOffset[1]);
                    mNestedYOffset += mScrollOffset[1];
                }

                if (!mIsBeingDragged && Math.abs(deltaY) > mTouchSlop) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    mIsBeingDragged = true;
                    if (deltaY > 0) {
                        deltaY -= mTouchSlop;
                    } else {
                        deltaY += mTouchSlop;
                    }
                }

                if (mIsBeingDragged) {
                    // Scroll to follow the motion event
                    mLastMotionY = y - mScrollOffset[1];
                    final int oldY = getScrollY();
                    scrollMoveOffset(deltaY);
                    final int scrolledDeltaY = getScrollY() - oldY;
                    final int unconsumedY = deltaY - scrolledDeltaY;
                    if (dispatchNestedScroll(0, scrolledDeltaY, 0, unconsumedY, mScrollOffset)) {
                        mLastMotionY -= mScrollOffset[1];
                        vtev.offsetLocation(0, mScrollOffset[1]);
                        mNestedYOffset += mScrollOffset[1];
                    }
                }
                break;
            }

            case MotionEventCompat.ACTION_POINTER_DOWN: {
                final int index = MotionEventCompat.getActionIndex(ev);
                mLastMotionY = (int) MotionEventCompat.getY(ev, index);
                mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                break;
            }

            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                mLastMotionY = (int) MotionEventCompat.getY(ev,
                        MotionEventCompat.findPointerIndex(ev, mActivePointerId));
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (mActivePointerId == INVALID_POINTER) {
                    break;
                }
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                checkSpringBack();
                stopNestedScroll();
            }
            default:
                break;
        }

        return true;
    }

    //视图内容滚动至指定位置并更新下拉视图状态
    private int scrollMoveOffset(int deltaY) {

        if(deltaY==0) return 0;

        final int oldScrollY = pullHelper.getScroll();
        final int consumed =pullHelper.checkUpdateScroll(deltaY);
        final int scrollY = pullHelper.getScroll();
        final int delta = scrollY - oldScrollY;

//        Logger.i("deltaY=" + deltaY+"\ngetScroll()="+getScrollY() + "\noldScrollY=" + oldScrollY +"\nScrollY=" + scrollY+
//                "\nconsumed="+consumed+"\ndelta="+delta +
//                "\nrefreshHeight="+pullHelper.getPullRefreshHeight()+"\npullMaxHegiht="+pullHelper.getMaxHeight());

        if (!isRefreshing) {
            if (pullHelper.canSendRefresh()) {
                mIRefreshStatus.onPullDowning();
            } else {
                mIRefreshStatus.onPullFreeHand();
            }
        }

        if(delta!=0)
            scrollTargetOffset(delta);

        return consumed;

    }

    //视图内容滚动到指定的位置
    private void scrollTargetOffset(int offsetY) {
        pullView.bringToFront();
        scrollBy(0, offsetY);
        mIRefreshStatus.onPullProgress(pullHelper.getScroll(),pullHelper.getMovePercentage());
    }

    //手离开屏幕后检查和更新状态
    private void checkSpringBack() {
        if (isRefreshing) {
            animToRefreshPosition(pullHelper.getScroll(), isRefreshingListener);
        } else {
            froceRefreshToState(pullHelper.canSendRefresh(), 0);
        }
    }

    //更新视图至指定当前相反状态
    public void froceRefreshToggle() {
        froceRefreshToState(!isRefreshing, 0);
    }

    //更新视图至指定刷新状态
    public void froceRefreshToState(boolean refresh, long delay) {
        final int scrollY=pullHelper.getScroll();
        if (isRefreshing != refresh) {
            isRefreshing = refresh;
            if (refresh) {
                animToRefreshPosition(scrollY, isRefreshingListener);
            } else {
                animToStartPosition(scrollY, mResetListener, delay);
            }
        } else if (isRefreshing == false) {
            animToStartPosition(scrollY, mResetListener, delay);
        }
    }

    //【刷新完成】后延迟指定时间执行【回滚隐藏动画】
    public void refreshDelayFinish(int delayTime) {
        if (mIRefreshStatus != null && isRefreshing != false) {
            mIRefreshStatus.onPullFinished();
            froceRefreshToState(false, delayTime);
        }
    }

    //【刷新完成】后执行【回滚隐藏动画】
    public void refreshFinish() {
        refreshDelayFinish(0);
    }

    private void animToStartPosition(int from,Animation.AnimationListener listener, long delay) {
        animation.reset();
        animation.setFrom(from);
        animation.setTo(0);
        animation.setStartOffset(delay);
        animation.setDuration(animDuration);
        animation.setInterpolator(mInterpolator);
        if (listener != null) {
            animation.setAnimationListener(listener);
        }
        startAnimation(animation);
    }

    private void animToRefreshPosition(int from, Animation.AnimationListener listener) {
        animation.reset();
        animation.setFrom(from);
        animation.setTo(pullHelper.getPullRefreshHeight());
        animation.setDuration(animDuration);
        animation.setInterpolator(mInterpolator);
        if (listener != null) {
            animation.setAnimationListener(listener);
        }
        clearAnimation();
        startAnimation(animation);
        animToPostion(from,pullHelper.getPullRefreshHeight(),listener);
    }

    public void animToPostion(int from,int to,Animation.AnimationListener listener){
        animation.reset();
        animation.setFrom(from);
        animation.setTo(to);
        animation.setDuration(animDuration);
        animation.setInterpolator(mInterpolator);
        if (listener != null) {
            animation.setAnimationListener(listener);
        }
        clearAnimation();
        startAnimation(animation);
    }

    //多个手指触摸屏幕状态的变更
    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = (ev.getAction() & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >>
                MotionEventCompat.ACTION_POINTER_INDEX_SHIFT;
        final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            // Make this decision more intelligent.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mLastMotionY = (int) MotionEventCompat.getY(ev, newPointerIndex);
            mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
        }
    }

    private void checkNestedTarget() {
        if (!isTargetValid()) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (!child.equals(pullView)) {
                    nestedTarget = child;
                    break;
                }
            }
        }
    }

    public boolean isTargetValid() {
        for (int i = 0; i < getChildCount(); i++) {
            if (nestedTarget == getChildAt(i)) {
                return true;
            }
        }

        return false;
    }

    public void checkUpdatePullViewIndex() {
        pullViewIndex = -1;
        for (int index = 0; index < getChildCount(); index++) {
            if (getChildAt(index) == pullView) {
                pullViewIndex = index;
                break;
            }
        }
    }

    public void setPullView(View pullView) {
        addOrUpdatePullView(pullHelper.getHeight(), pullView);
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefreshListener = listener;
    }

    public interface OnRefreshListener {
        void onRefresh();
    }


    public String getActionString(int action) {

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                return "ACTION_DOWN";

            case MotionEvent.ACTION_MOVE:
                return "ACTION_MOVE";

            case MotionEvent.ACTION_UP:
                return "ACTION_UP";

            case MotionEvent.ACTION_POINTER_UP:
                return "ACTION_POINTER_UP";
            default:
                return "";

        }
    }

    public static abstract class PullAnimation extends Animation{
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
