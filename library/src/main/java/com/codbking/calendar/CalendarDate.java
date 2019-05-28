package com.codbking.calendar;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日历
 *
 * @author xuexiang
 * @since 2019/5/28 15:23
 */
public class CalendarDate {

    /**
     * 年
     */
    public int year;
    /**
     * 月
     */
    public int month;
    /**
     * 日
     */
    public int day;
    /**
     * 周
     */
    public int week;

    //-1,0,1
    /**
     * 月份flag
     */
    public int monthFlag;

    /**
     * 农历的月
     */
    public String chinaMonth;
    /**
     * 农历的日
     */
    public String chinaDay;

    public static CalendarDate get(Date date) {
        return CalendarFactory.getCalendarDate(date);
    }

    public static CalendarDate get(int year, int month, int day) {
        return CalendarFactory.getCalendarDate(year, month, day);
    }

    public CalendarDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getWeek() {
        return week;
    }

    public int getMonthFlag() {
        return monthFlag;
    }

    public String getChinaMonth() {
        return chinaMonth;
    }

    public String getChinaDay() {
        return chinaDay;
    }

    public String getDisplayWeek() {
        String s = "";
        switch (week) {
            case 1:
                s = "星期日";
                break;
            case 2:
                s = "星期一";
                break;
            case 3:
                s = "星期二";
                break;
            case 4:
                s = "星期三";
                break;
            case 5:
                s = "星期四";
                break;
            case 6:
                s = "星期五";
                break;
            case 7:
                s = "星期六";
                break;
            default:
                break;

        }
        return s;
    }

    /**
     * 转化为Date
     *
     * @return
     */
    public Date toDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return calendar.getTime();
    }

    /**
     * 获取格式化的日期
     *
     * @return
     */
    public String formatDate(final DateFormat format) {
        return CalendarUtils.date2String(toDate(), format);
    }

    /**
     * 获取格式化的日期
     *
     * @return
     */
    public String formatDate() {
        return CalendarUtils.date2String(toDate(), new SimpleDateFormat("yyyy-MM-dd"));
    }

    /**
     * 是否是今天
     *
     * @return
     */
    public boolean isToday() {
        return CalendarUtils.isToday(this);
    }

    @Override
    public String toString() {
        return "CalendarDate{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", week=" + week +
                ", monthFlag=" + monthFlag +
                ", chinaMonth='" + chinaMonth + '\'' +
                ", chinaDay='" + chinaDay + '\'' +
                '}';
    }

    public boolean equals(CalendarDate date) {
        return date != null && date.year == year && date.month == month && date.day == day && date.week == week;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        CalendarDate calendarDate = new CalendarDate(2019, 5, 28);
        System.out.println("isToday:" + calendarDate.isToday());
    }
}