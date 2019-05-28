package com.codbking.calendar;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 *
 * @author xuexiang
 * @since 2019/5/28 14:40
 */
public final class CalendarUtils {

    private CalendarUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 获取一月中某一天是星期几
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static int getDayOfWeek(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取一月最大天数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getDayOfMaonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        return cal.getActualMaximum(Calendar.DATE);
    }

    public static int getMothOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        int dateOfMonth = cal.get(Calendar.MONTH);
        return dateOfMonth + 1;
    }

    public static int[] getYMD(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return new int[]{cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE)};
    }

    /**
     * 判断是否今天
     *
     * @param date Date 类型时间
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isToday(final CalendarDate date) {
        Calendar now = Calendar.getInstance();
        now.setTime(getNowDate());
        return date.year == now.get(Calendar.YEAR) && date.month == now.get(Calendar.MONTH) + 1 && date.day == now.get(Calendar.DATE);
    }

    /**
     * 获取当前 Date
     *
     * @return Date 类型时间
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 将 Date 类型转为时间字符串
     * <p>格式为 format</p>
     *
     * @param date   Date 类型时间
     * @param format 时间格式
     * @return 时间字符串
     */
    public static String date2String(final Date date, final DateFormat format) {
        if (format != null) {
            return format.format(date);
        } else {
            return "";
        }
    }

}
