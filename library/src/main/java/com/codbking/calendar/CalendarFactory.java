package com.codbking.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.codbking.calendar.CalendarUtils.getDayOfWeek;


/**
 *
 *
 * @author xuexiang
 * @since 2019/5/28 14:28
 */
public final class CalendarFactory {

    private static HashMap<String, List<CalendarDate>> mCache = new HashMap<>();

    //获取一月中的集合
    static List<CalendarDate> getMonthOfDayList(int y, int m) {
        String key = y + "" + m;
        if (mCache.containsKey(key)) {
            List<CalendarDate> list = mCache.get(key);
            if (list == null) {
                mCache.remove(key);
            } else {
                return list;
            }
        }

        List<CalendarDate> list = new ArrayList<CalendarDate>();
        mCache.put(key, list);

        //计算出一月第一天是星期几
        int fweek = getDayOfWeek(y, m, 1);
        int total = CalendarUtils.getDayOfMaonth(y, m);

        //根据星期推出前面还有几个显示
        for (int i = fweek - 1; i > 0; i--) {
            CalendarDate bean = getCalendarDate(y, m, 1 - i);
            bean.monthFlag = -1;
            list.add(bean);
        }

        //获取当月的天数
        for (int i = 0; i < total; i++) {
            CalendarDate bean = getCalendarDate(y, m, i + 1);
            list.add(bean);
        }

        //为了塞满42个格子，显示多出当月的天数
        for (int i = 0; i < 42 - (fweek - 1) - total; i++) {
            CalendarDate bean = getCalendarDate(y, m, total + i + 1);
            bean.monthFlag = 1;
            list.add(bean);
        }
        return list;
    }


    public static CalendarDate getCalendarDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DATE);

        CalendarDate bean = new CalendarDate(year, month, day);
        bean.week = CalendarUtils.getDayOfWeek(year, month, day);
        String[] chinaDate = ChinaDateUtils.getChinaDate(year, month, day);
        bean.chinaMonth = chinaDate[0];
        bean.chinaDay = chinaDate[1];
        return bean;
    }


    public static CalendarDate getCalendarDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);

        CalendarDate bean = new CalendarDate(year, month, day);
        bean.week = CalendarUtils.getDayOfWeek(year, month, day);
        String[] chinaDate = ChinaDateUtils.getChinaDate(year, month, day);
        bean.chinaMonth = chinaDate[0];
        bean.chinaDay = chinaDate[1];
        return bean;
    }

}
