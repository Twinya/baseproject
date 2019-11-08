package com.appengine.frame.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StatisticsTimeUtils {
    public static final int MS_OF_DAY = 24 * 60 * 60 * 1000;
    public static final int MS_OF_HALF_DAY = 12 * 60 * 60 * 1000;
    public static final int MS_OF_HOUR = 60 * 60 * 1000;

    /**
     * yyyyMMddHH get next hour
     *
     * @param yyyyMMddHH
     * @return
     */
    public static Integer getNextHour(Integer yyyyMMddHH) {
        return getNextTime(yyyyMMddHH, "yyyyMMddHH", MS_OF_HOUR);
    }

    public static Integer getPreviousHour(Integer yyyyMMddHH) {
        return getPreviousTime(yyyyMMddHH, "yyyyMMddHH", MS_OF_HOUR);
    }

    public static Integer getPreviousHour() {
        return getPreviousHour(getHourFromLong(System.currentTimeMillis()));
    }

    /**
     * yyyyMMdd get next day
     *
     * @param yyyyMMdd
     * @return
     */
    public static int getNextDay(Integer yyyyMMdd) {
        return getNextTime(yyyyMMdd, "yyyyMMdd", MS_OF_DAY);
    }

    public static int getPreviousDay(Integer yyyyMMdd) {
        return getPreviousTime(yyyyMMdd, "yyyyMMdd", MS_OF_DAY);
    }

    public static int getPreviousDay() {
        return getPreviousDay(getDateFromLong(System.currentTimeMillis()));
    }

    private static Integer getNextTime(Integer dateHour, String format, int unit) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(dateHour + "");
            date.setTime(date.getTime() + unit);
            return Integer.decode(sdf.format(date));
        } catch (Exception e) {
            return 0;
        }
    }

    private static Integer getPreviousTime(Integer dateHour, String format, int unit) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(dateHour + "");
            date.setTime(date.getTime() - unit);
            return Integer.decode(sdf.format(date));
        } catch (Exception e) {
            return 0;
        }
    }

    public static long getTime(Integer dateHour, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(dateHour + "");
            return date.getTime();
        } catch (Exception e) {
            return 0;
        }
    }


    /**
     * ms time convert to yyyyMMddHH
     *
     * @param time
     * @return
     */
    public static Integer getHourFromLong(Long time) {
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHH");
        return Integer.decode(format.format(date));
    }

    public static Integer getHourNow() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHH");
        return Integer.decode(format.format(date));
    }

    public static Long getMinuteNow() {
        return getMinute(System.currentTimeMillis());
    }

    public static Long getMinute(Long time) {
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
        return Long.decode(format.format(date));
    }

    public static Integer getToday() {
        return getDateFromLong(System.currentTimeMillis());
    }

    /**
     * ms time convert to yyyyMMdd
     *
     * @param time ms
     * @return
     */
    public static int getDateFromLong(Long time) {
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return Integer.decode(format.format(date));
    }

    /**
     * @return ms at 00:00 today
     */
    public static long getZeroTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static long getZeroTime(int yyyyMMdd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(getTime(yyyyMMdd, "yyyyMMdd")));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static Integer getZeroHour() {
        return getHourFromLong(getZeroTime());
    }

    public static String getMinuteOfHour() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("mmss");
        return format.format(date);
    }
}
