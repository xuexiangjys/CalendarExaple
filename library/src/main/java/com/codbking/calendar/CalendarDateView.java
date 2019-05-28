package com.codbking.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;
import java.util.LinkedList;

import static com.codbking.calendar.CalendarFactory.getMonthOfDayList;

/**
 * @author xuexiang
 * @since 2019/5/28 14:18
 */
public class CalendarDateView extends ViewPager implements CalendarTopView {
    private final static int DEFAULT_MAX_ROW_COUNT = 5;

    private SparseArray<CalendarView> mViews = new SparseArray<>();
    private CaledarTopViewChangeListener mCaledarLayoutChangeListener;
    private CalendarView.OnItemClickListener mOnItemClickListener;
    private CalendarView.OnTodaySelectStatusChangedListener mOnTodaySelectStatusChangedListener;

    private LinkedList<CalendarView> mCache = new LinkedList<>();
    private PagerAdapter mPagerAdapter;
    private int mRow;

    private CaledarAdapter mAdapter;
    private int mCalendarItemHeight = 0;

    public void setAdapter(CaledarAdapter adapter) {
        mAdapter = adapter;
        initData();
    }

    public CalendarDateView setOnItemClickListener(CalendarView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        return this;
    }

    public CalendarDateView setOnTodaySelectStatusChangedListener(CalendarView.OnTodaySelectStatusChangedListener onTodaySelectStatusChangedListener) {
        mOnTodaySelectStatusChangedListener = onTodaySelectStatusChangedListener;
        return this;
    }

    public CalendarDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CalendarDateView);
        mRow = a.getInteger(R.styleable.CalendarDateView_cbd_calendar_row, DEFAULT_MAX_ROW_COUNT);
        a.recycle();
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int calendarHeight = 0;
        if (getAdapter() != null) {
            CalendarView view = (CalendarView) getChildAt(0);
            if (view != null) {
                calendarHeight = view.getMeasuredHeight();
                mCalendarItemHeight = view.getItemHeight();
            }
        }
        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(calendarHeight, MeasureSpec.EXACTLY));
    }

    private void init() {
        final int[] dateArr = CalendarUtils.getYMD(new Date());

        setAdapter(mPagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return Integer.MAX_VALUE;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {

                CalendarView view;

                if (!mCache.isEmpty()) {
                    view = mCache.removeFirst();
                } else {
                    view = new CalendarView(container.getContext(), mRow);
                }

                view.setOnItemClickListener(mOnItemClickListener);
                view.setOnTodaySelectStatusChangedListener(mOnTodaySelectStatusChangedListener);
                view.setAdapter(mAdapter);

                view.setData(getMonthOfDayList(dateArr[0], dateArr[1] + position - Integer.MAX_VALUE / 2), position == Integer.MAX_VALUE / 2);
                container.addView(view);
                mViews.put(position, view);

                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
                mCache.addLast((CalendarView) object);
                mViews.remove(position);
            }
        });

        addOnPageChangeListener(new SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (mOnItemClickListener != null) {
                    CalendarView view = mViews.get(position);
                    Object[] obs = view.getSelect();
                    mOnItemClickListener.onItemClick((View) obs[0], (int) obs[1], (CalendarDate) obs[2]);
                }

                mCaledarLayoutChangeListener.onLayoutChange(CalendarDateView.this);
            }
        });
    }


    private void initData() {
        setCurrentItem(Integer.MAX_VALUE / 2, false);
        getAdapter().notifyDataSetChanged();

    }

    @Override
    public int[] getCurrentSelectRect() {
        CalendarView view = mViews.get(getCurrentItem());
        if (view == null) {
            view = (CalendarView) getChildAt(0);
        }
        if (view != null) {
            return view.getSelectRect();
        }
        return new int[4];
    }

    @Override
    public int getCurrentSelectPosition() {
        CalendarView view = mViews.get(getCurrentItem());
        if (view == null) {
            view = (CalendarView) getChildAt(0);
        }
        if (view != null) {
            return view.getSelectPostion();
        }
        return -1;
    }

    @Override
    public CalendarDate getSelectCalendarDate() {
        CalendarView view = mViews.get(getCurrentItem());
        if (view == null) {
            view = (CalendarView) getChildAt(0);
        }
        if (view != null) {
            return view.getSelectCalendarDate();
        }
        return null;
    }

    public CalendarView getCurrentCalendarView() {
        return mViews.get(getCurrentItem());
    }

    @Override
    public int getItemHeight() {
        return mCalendarItemHeight;
    }

    @Override
    public void setCaledarTopViewChangeListener(CaledarTopViewChangeListener listener) {
        mCaledarLayoutChangeListener = listener;
    }

    public PagerAdapter getPagerAdapter() {
        return mPagerAdapter;
    }

    public void previous() {
        setCurrentItem(getCurrentItem() - 1);
    }

    public void next() {
        setCurrentItem(getCurrentItem() + 1);
    }
}
