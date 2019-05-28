package com.codbking.calendar;


import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日历
 *
 * @author xuexiang
 * @since 2019/5/28 15:23
 */
public class CalendarDate {

    public int year;
    public int month;
    public int day;
    public int week;

    //-1,0,1
    public int monthFlag;

    //显示
    public String chinaMonth;
    public String chinaDay;

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