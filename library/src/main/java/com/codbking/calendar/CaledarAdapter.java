package com.codbking.calendar;

import android.view.View;
import android.view.ViewGroup;

/**
 * 日历的适配器
 *
 * @author xuexiang
 * @since 2019/5/28 15:05
 */
public interface CaledarAdapter {
     View getView(View convertView, ViewGroup parentView, CalendarDate calendarDate);
}
