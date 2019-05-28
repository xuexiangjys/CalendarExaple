package com.codbking.calendar;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;

/**
 *
 *
 * @author xuexiang
 * @since 2019/5/28 14:08
 */
public class CalendarLayout extends FrameLayout {

    private View mView1;
    private ViewGroup mView2;
    private CalendarTopView mTopView;
    //展开
    public static final int TYPE_OPEN = 0;
    //折叠
    public static final int TYPE_FOLD = 1;
    public int mType = TYPE_OPEN;

    //是否处于滑动中
    private boolean mIsSilde = false;

    private int mTopHeigth;
    private int mItemHeight;
    private int mBottomViewTopHeight;
    private int mMaxDistance;

    private ScrollerCompat mScroller;
    private float mMaxVelocity;
    private float mMinVelocity;
    private int mPotionerId;

    private static final Interpolator sInterpolator = new Interpolator() {
        @Override
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };

    public CalendarLayout(Context context) {
        super(context);
        init();
    }

    public CalendarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        final CalendarTopView viewPager = (CalendarTopView) getChildAt(0);

        mTopView = viewPager;
        mView1 = (View) viewPager;
        mView2 = (ViewGroup) getChildAt(1);

        mTopView.setCaledarTopViewChangeListener(new CaledarTopViewChangeListener() {
            @Override
            public void onLayoutChange(CalendarTopView topView) {
                CalendarLayout.this.requestLayout();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mItemHeight = mTopView.getItemHeight();
        mTopHeigth = mView1.getMeasuredHeight();
        mMaxDistance = mTopHeigth - mItemHeight;

        switch (mType) {
            case TYPE_FOLD:
                mBottomViewTopHeight = mItemHeight;
                break;
            case TYPE_OPEN:
                mBottomViewTopHeight = mTopHeigth;
                break;
        }
        if (mView2 != null) {
            mView2.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec) - mTopView.getItemHeight(), MeasureSpec.EXACTLY));
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mView2 != null) {
            mView2.offsetTopAndBottom(mBottomViewTopHeight);
        }
        int[] selectRct = getCurrentSelectRect();
        if (mType == TYPE_FOLD) {
            mView1.offsetTopAndBottom(-selectRct[1]);
        }
    }

    private void init() {
        final ViewConfiguration vc = ViewConfiguration.get(getContext());
        mMaxVelocity = vc.getScaledMaximumFlingVelocity();
        mMinVelocity = vc.getScaledMinimumFlingVelocity();
        mScroller = ScrollerCompat.create(getContext(), sInterpolator);
    }

    private float mTempY, mTempX;
    private boolean mIsClickBottomView = false;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mView2 == null) {
            return super.onInterceptTouchEvent(ev);
        }
        boolean isflag = false;

        //上下运动进行拦截
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mView2 != null) {
                    mTempY = ev.getY();
                    mTempX = ev.getX();
                    mIsClickBottomView = isClickView(mView2, ev);
                    cancel();
                    mPotionerId = ev.getPointerId(0);

                    int top = mView2.getTop();

                    if (top < mTopHeigth) {
                        mType = TYPE_FOLD;
                    } else {
                        mType = TYPE_OPEN;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mView2 != null) {
                    float y = ev.getY();
                    float x = ev.getX();

                    float xdiff = x - mTempX;
                    float ydiff = y - mTempY;

                    if (Math.abs(ydiff) > 5 && Math.abs(ydiff) > Math.abs(xdiff)) {
                        isflag = true;

                        if (mIsClickBottomView) {
                            boolean isScroll = isScroll(mView2);
                            if (ydiff > 0) {
                                //向下
                                if (mType == TYPE_OPEN) {
                                    return super.onInterceptTouchEvent(ev);
                                } else {
                                    if (isScroll) {
                                        return super.onInterceptTouchEvent(ev);
                                    }

                                }
                            } else {
                                //向上
                                if (mType == TYPE_FOLD) {
                                    return super.onInterceptTouchEvent(ev);
                                } else {
                                    if (isScroll) {
                                        return super.onInterceptTouchEvent(ev);
                                    }
                                }
                            }

                        }
                    }
                    mTempX = x;
                    mTempY = y;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return mIsSilde || isflag || super.onInterceptTouchEvent(ev);
    }

    private boolean isScroll(ViewGroup view2) {
        View fistChildView = view2.getChildAt(0);
        if (fistChildView == null) {
            return false;
        }
        if (view2 instanceof ListView) {
            AbsListView list = (AbsListView) view2;
            if (fistChildView.getTop() != 0) {
                return true;
            } else {
                return list.getPositionForView(fistChildView) != 0;
            }
        }
        return false;
    }

    public boolean isClickView(View view, MotionEvent ev) {
        Rect rect = new Rect();
        view.getHitRect(rect);
        return rect.contains((int) ev.getX(), (int) ev.getY());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        processTouchEvent(event);
        return true;
    }

    private VelocityTracker mVelocityTracker;

    public void processTouchEvent(MotionEvent event) {

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsSilde) {
                    return;
                }
                float cy = event.getY();
                int dy = (int) (cy - mTempY);

                if (dy == 0) {
                    return;
                }
                mTempY = cy;
                move(dy);

                break;
            case MotionEvent.ACTION_UP:
                if (mView2 == null) {
                    return;
                }
                if (mIsSilde) {
                    cancel();
                    return;
                }

                //判断速度
                final int pointerId = mPotionerId;
                mVelocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
                float crrentV = VelocityTrackerCompat.getYVelocity(mVelocityTracker, pointerId);

                if (Math.abs(crrentV) > 2000) {
                    if (crrentV > 0) {
                        open();
                    } else {
                        flod();
                    }
                    cancel();
                    return;
                }

                int top = mView2.getTop() - mTopHeigth;
                int maxd = mMaxDistance;

                if (Math.abs(top) < maxd / 2) {
                    open();
                } else {
                    flod();
                }
                cancel();

                break;
            case MotionEvent.ACTION_CANCEL:
                cancel();
                break;
        }
    }

    public void open() {
        if (mView2 != null) {
            startScroll(mView2.getTop(), mTopHeigth);
        }
    }

    public void flod() {
        if (mView2 != null) {
            startScroll(mView2.getTop(), mTopHeigth - mMaxDistance);
        }
    }

    private int[] getCurrentSelectRect() {
        return mTopView.getCurrentSelectRect();
    }

    private void move(int dy) {
        if (mView2 == null) {
            return;
        }

        int[] selectRect = getCurrentSelectRect();
        int itemHeight = mTopView.getItemHeight();

        int dy1 = getAreaValue(mView1.getTop(), dy, -selectRect[1], 0);
        int dy2 = getAreaValue(mView2.getTop() - mTopHeigth, dy, -(mTopHeigth - itemHeight), 0);

        if (dy1 != 0) {
            ViewCompat.offsetTopAndBottom(mView1, dy1);
        }
        if (dy2 != 0) {
            ViewCompat.offsetTopAndBottom(mView2, dy2);
        }
    }

    private int getAreaValue(int top, int dy, int minValue, int maxValue) {
        if (top + dy < minValue) {
            return minValue - top;
        }
        if (top + dy > maxValue) {
            return maxValue - top;
        }
        return dy;
    }

    private void startScroll(int starty, int endY) {

        float distance = endY - starty;
        float t = distance / mMaxDistance * 600;

        mScroller.startScroll(0, 0, 0, endY - starty, (int) Math.abs(t));
        postInvalidate();
    }

    int oldY = 0;

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mView2 != null) {
            mBottomViewTopHeight = mView2.getTop();
        }
        if (mScroller.computeScrollOffset()) {
            mIsSilde = true;
            int cy = mScroller.getCurrY();
            int dy = cy - oldY;
            move(dy);
            oldY = cy;
            postInvalidate();
        } else {
            oldY = 0;
            mIsSilde = false;
        }
    }

    public void cancel() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

}
