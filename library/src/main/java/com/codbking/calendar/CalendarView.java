package com.codbking.calendar;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;


import java.util.Date;
import java.util.List;

/**
 * @author xuexiang
 * @since 2019/5/28 14:31
 */
public class CalendarView extends ViewGroup {
    private int mSelectPosition = -1;

    private CaledarAdapter mAdapter;
    private List<CalendarDate> mData;
    private OnItemClickListener mOnItemClickListener;

    private int mRow = 6;
    private int mColumn = 7;
    private int mItemWidth;
    private int mItemHeight;

    private boolean mIsToday;
    private View mTodayView;
    private int mTodayPosition;
    private boolean mIsTodaySelected;
    private OnTodaySelectStatusChangedListener mOnTodaySelectStatusChangedListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int postion, CalendarDate date);
    }

    public interface OnTodaySelectStatusChangedListener {
        void onStatusChanged(View todayView, boolean isSelected);
    }


    public CalendarView(Context context, int row) {
        super(context);
        mRow = row;
    }

    public CalendarView setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        return this;
    }

    public CalendarView setOnTodaySelectStatusChangedListener(OnTodaySelectStatusChangedListener onTodaySelectStatusChangedListener) {
        mOnTodaySelectStatusChangedListener = onTodaySelectStatusChangedListener;
        return this;
    }

    public int getItemHeight() {
        return mItemHeight;
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public void setAdapter(CaledarAdapter adapter) {
        this.mAdapter = adapter;
    }

    public void setData(List<CalendarDate> data, boolean isToday) {
        mData = data;
        mIsToday = isToday;
        setItem();
        requestLayout();
    }

    private void setItem() {
        mSelectPosition = -1;
        if (mAdapter == null) {
            throw new RuntimeException("mAdapter is null,please setadapter");
        }

        for (int i = 0; i < mData.size(); i++) {
            CalendarDate calendarDate = mData.get(i);
            View view = getChildAt(i);
            View chidView = mAdapter.getView(view, this, calendarDate);

            if (view == null || view != chidView) {
                addViewInLayout(chidView, i, chidView.getLayoutParams(), true);
            }

            if (mIsToday && mSelectPosition == -1) {
                int[] date = CalendarUtils.getYMD(new Date());
                if (calendarDate.year == date[0] && calendarDate.month == date[1] && calendarDate.day == date[2]) {
                    mSelectPosition = i;
                    mTodayView = chidView;
                    mTodayPosition = i;
                    mIsTodaySelected = true;
                }
            } else {
                if (mSelectPosition == -1 && calendarDate.day == 1) {
                    mSelectPosition = i;
                }
            }

            chidView.setSelected(mSelectPosition == i);

            setItemClick(chidView, i, calendarDate);

        }
    }

    public Object[] getSelect() {
        return new Object[]{getChildAt(mSelectPosition), mSelectPosition, mData.get(mSelectPosition)};
    }

    public void setItemClick(final View view, final int position, final CalendarDate bean) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectPosition != -1) {
                    getChildAt(mSelectPosition).setSelected(false);
                    getChildAt(position).setSelected(true);
                }
                mSelectPosition = position;

                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, position, bean);
                }

                if (mIsToday) {
                    if (mOnTodaySelectStatusChangedListener != null) {
                        if (mIsTodaySelected) {
                            if (mSelectPosition != mTodayPosition) {
                                mIsTodaySelected = false;
                                mOnTodaySelectStatusChangedListener.onStatusChanged(mTodayView, false);
                            }
                        } else {
                            if (mSelectPosition == mTodayPosition) {
                                mIsTodaySelected = true;
                                mOnTodaySelectStatusChangedListener.onStatusChanged(mTodayView, true);
                            }
                        }
                    }
                }

            }
        });
    }

    public int[] getSelectRect() {
        Rect rect = new Rect();
        try {
            getChildAt(mSelectPosition).getHitRect(rect);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new int[]{rect.left, rect.top, rect.right, rect.top};
    }

    public int getSelectPostion() {
        return mSelectPosition;
    }

    public CalendarDate getSelectCalendarDate() {
        return mData.get(mSelectPosition);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int parentWidth = MeasureSpec.getSize(MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.EXACTLY));

        mItemWidth = parentWidth / mColumn;
        mItemHeight = mItemWidth;

        View view = getChildAt(0);
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null && params.height > 0) {
            mItemHeight = params.height;
        }
        setMeasuredDimension(parentWidth, mItemHeight * mRow);


        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            childView.measure(MeasureSpec.makeMeasureSpec(mItemWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mItemHeight, MeasureSpec.EXACTLY));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            layoutChild(getChildAt(i), i, l, t, r, b);
        }
    }

    private void layoutChild(View view, int postion, int l, int t, int r, int b) {

        int cc = postion % mColumn;
        int cr = postion / mColumn;

        int itemWidth = view.getMeasuredWidth();
        int itemHeight = view.getMeasuredHeight();

        l = cc * itemWidth;
        t = cr * itemHeight;
        r = l + itemWidth;
        b = t + itemHeight;
        view.layout(l, t, r, b);
    }


}
