package com.codbking.calendar.exaple;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codbking.calendar.CaledarAdapter;
import com.codbking.calendar.CalendarDate;
import com.codbking.calendar.CalendarDateView;
import com.codbking.calendar.CalendarUtils;
import com.codbking.calendar.CalendarView;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.codbking.calendar.exaple.Utils.px;

/**
 * @author xuexiang
 * @since 2019/5/27 17:18
 */
public class CustomActivity extends AppCompatActivity {
    @BindView(R.id.calendarDateView)
    CalendarDateView mCalendarDateView;
    @BindView(R.id.title)
    TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        ButterKnife.bind(this);

        mCalendarDateView.setAdapter(new CaledarAdapter() {
            @Override
            public View getView(View convertView, ViewGroup parentView, CalendarDate calendarDate) {
                TextView view;
                if (convertView == null) {
                    convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.item_custom, null);
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(px(48), px(48));
                    convertView.setLayoutParams(params);
                }

                view = convertView.findViewById(R.id.text);
                view.setText("" + calendarDate.day);

                if (calendarDate.monthFlag != 0) {
                    view.setTextColor(0xff9299a1);
                } else {
                    if (calendarDate.isToday() && calendarDate.equals(mCalendarDateView.getSelectCalendarDate())) {
                        view.setTextColor(Color.parseColor("#FF9900"));
                    } else {
                        view.setTextColor(Color.parseColor("#3B4664"));
                    }
                }
                return convertView;
            }
        });

        mCalendarDateView.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, CalendarDate calendarDate) {
                mTitle.setText(calendarDate.year + "/" + getDisPlayNumber(calendarDate.month) + "/" + getDisPlayNumber(calendarDate.day));
            }
        });

        mCalendarDateView.setOnTodaySelectStatusChangedListener(new CalendarView.OnTodaySelectStatusChangedListener() {
            @Override
            public void onStatusChanged(View todayView, boolean isSelected) {
                TextView view = todayView.findViewById(R.id.text);
                if (isSelected) {
                    view.setTextColor(Color.parseColor("#3B4664"));
                } else {
                    view.setTextColor(Color.parseColor("#FF9900"));
                }
            }
        });

        int[] data = CalendarUtils.getYMD(new Date());
        mTitle.setText(data[0] + "/" + data[1] + "/" + data[2]);
    }

    private String getDisPlayNumber(int num) {
        return num < 10 ? "0" + num : "" + num;
    }

    @OnClick({R.id.previous, R.id.next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.previous:
                mCalendarDateView.previous();
                break;
            case R.id.next:
                mCalendarDateView.next();
                break;
        }
    }
}
