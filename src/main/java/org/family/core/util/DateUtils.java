package org.family.core.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.*;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 *
 * @author ThinkGem
 * @version 2014-4-15
 */
@Slf4j
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static final String YYYY_MM = "yyyy-MM";

    public static final String YYYY = "yyyy";

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(YYYY_MM_DD);

    private static String[] parsePatterns = { YYYY, YYYY_MM, "yyyy.MM", "yyyy/MM", YYYY_MM_DD,
            "yyyy.MM.dd", "yyyy/MM/dd", "yyyy-MM-dd HH:mm", "yyyy.MM.dd HH:mm", "yyyy/MM/dd HH:mm",
            YYYY_MM_DD_HH_MM_SS, "yyyy.MM.dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss",
            "yyyy-MM-dd HH:mm:ss.SSS" };

    private static final int[] seasonFirstMonth = new int[] { 0, 0, 0, 3, 3, 3, 6, 6, 6, 9, 9, 9 };

    private static final int[] seasonLastMonth = new int[] { 2, 2, 2, 5, 5, 5, 8, 8, 8, 11, 11,
            11 };

    /**************************** 格式化时间 begin ********************************/

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String formatDate(Date date, Object... pattern) {
        String formatDate;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, YYYY_MM_DD);
        }
        return formatDate;
    }

    public static Date getDateFromStr(String dateStr, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public static String getStrFromNewStr(String dateStr, String pattern, String newPattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        SimpleDateFormat newSdf = new SimpleDateFormat(newPattern);
        try {
            return newSdf.format(sdf.parse(dateStr));
        } catch (ParseException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 日期型字符串转化为日期 格式 { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
     * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy.MM.dd",
     * "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseDateWithPattern(Object str, String pattern) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), pattern);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 转换为时间（天,时:分:秒.毫秒）
     */
    public static String formatDateTime(long timeMillis) {
        long day = timeMillis / (24 * 60 * 60 * 1000);
        long hour = ((timeMillis / (60 * 60 * 1000)) - (day * 24));
        long min = ((timeMillis / (60 * 1000)) - (day * 24 * 60) - (hour * 60));
        long s = ((timeMillis / 1000) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (min * 60));
        long sss = (timeMillis - (day * 24 * 60 * 60 * 1000) - (hour * 60 * 60 * 1000)
                - (min * 60 * 1000) - (s * 1000));
        return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
    }

    /**************************** 格式化时间 end ********************************/

    /**************************** 获取格式化的当前时间 begin ********************************/

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd）
     */
    public static String getDate() {
        return getDate(YYYY_MM_DD);
    }

    /**
     * 得到当前日期字符串 格式（pattern） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    /**
     * 得到当前年份字符串 格式（yyyy）
     */
    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    /**
     * 得到当前月份字符串 格式（MM）
     */
    public static String getMonth() {
        return formatDate(new Date(), "MM");
    }

    /**
     * 得到当天字符串 格式（dd）
     */
    public static String getDay() {
        return formatDate(new Date(), "dd");
    }

    /**
     * 得到当前星期字符串 格式（E）星期几
     */
    public static String getWeek() {
        return formatDate(new Date(), "E");
    }

    /**
     * 得到当前时间字符串 格式（HH:mm:ss）
     */
    public static String getTime() {
        return formatDate(new Date(), "HH:mm:ss");
    }

    /**
     * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String getDateTime() {
        return formatDate(new Date(), YYYY_MM_DD_HH_MM_SS);
    }

    /**************************** 获取格式化的当前时间 end ********************************/

    /**************************** 指定日期时间相对时间 begin ********************************/

    /**
     * 获取过去的天数
     */
    public static long pastDays(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (24 * 60 * 60 * 1000);
    }

    /**
     * 获取过去的小时
     */
    public static long pastHour(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (60 * 60 * 1000);
    }

    /**
     * 获取过去的分钟
     */
    public static long pastMinutes(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (60 * 1000);
    }

    /**
     * 获取两个日期之间的天数
     */
    public static double getDistanceOfTwoDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (double) (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
    }

    /**
     * 年加减
     */
    public static String getYears(Date date, int i, Object... pattern) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, i);
        return formatDate(calendar.getTime(), pattern);
    }

    /**
     * 月加减
     */
    public static String getMonth(Date date, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, i);
        return formatDate(calendar.getTime());
    }

    /**
     * 获取定日期同比时间
     */
    public static String getYoYDate(String date) {
        Date targetDate = getDateFromStr(date, YYYY_MM_DD);
        return getYears(targetDate, -1);
    }

    /**
     * 获取定日期环比时间
     */
    public static String getMoMDate(String date) {
        Date targetDate = getDateFromStr(date, YYYY_MM_DD);
        return getMonth(targetDate, -1);
    }

    /**
     * 得到指定日期所在季度的第一天的日期, 时分为00:00:00
     */
    public static Date getFirstDayInSeason(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        int currentMonth = calendar.get(Calendar.MONTH);

        calendar.set(Calendar.MONTH, seasonFirstMonth[currentMonth]);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, 0);

        return getFirstDayInMonth(calendar.getTime());
    }

    /**
     * 得到指定日期所在季度的最后一天的日期, 时分为23:59:59
     */
    public static Date getLastDayInSeason(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        int currentMonth = calendar.get(Calendar.MONTH);

        calendar.set(Calendar.MONTH, seasonLastMonth[currentMonth]);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, 0);

        return getLastDayInMonth(calendar.getTime());
    }

    /**
     * 得到指定日期所在月的第一天的日期, 时分为00:00:00
     */
    public static Date getFirstDayInMonth(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /**
     * 得到指定日期所在月的最后一天的日期, 时分为23:59:59
     */
    public static Date getLastDayInMonth(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /**
     * 得到定日期年份的最后一天
     */
    public static Date getLastDayInYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
        return calendar.getTime();
    }

    /**
     * 得到定日期年份的第一天
     */
    public static Date getFirstDayInYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMinimum(Calendar.DAY_OF_YEAR));
        return calendar.getTime();
    }

    /**
     * 得到定日期月份第一天
     */
    public static String getMonthFirstDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return formatDate(calendar.getTime());
    }

    /**
     * 得到定日期月份最后一天
     */
    public static String getMonthLastDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return formatDate(calendar.getTime());
    }

    /**
     * 得到定日期年份的最后一天
     */
    public static String getYearLastDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
        return formatDate(calendar.getTime());
    }

    /**
     * 得到定日期年份的第一天
     */
    public static String getYearFirstDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMinimum(Calendar.DAY_OF_YEAR));
        return formatDate(calendar.getTime());
    }

    /**************************** 指定日期时间相对时间 end ********************************/

    /**************************** 获取相对时间 begin ********************************/

    /**
     * 获取n年前的那年的第一天
     */
    public static String getFirstDayOfXXYearAgoStr(int n) {
        LocalDate localDate = LocalDateTime
                .ofInstant(new Date().toInstant(), ZoneId.systemDefault()).toLocalDate();
        return dateTimeFormatter.format(localDate.minusYears(n).with(firstDayOfYear()));
    }

    /**
     * 获取n年前的那年的最后一天
     */
    public static String getLastDayOfXXYearAgoStr(int n) {
        LocalDate localDate = LocalDateTime
                .ofInstant((new Date()).toInstant(), ZoneId.systemDefault()).toLocalDate();
        return dateTimeFormatter.format(localDate.minusYears(n).with(lastDayOfYear()));
    }

    /**
     * 获取n月前的那年的第一天
     */
    public static String getFirstDayOfXXMonthAgoStr(int n) {
        LocalDate localDate = LocalDateTime
                .ofInstant(new Date().toInstant(), ZoneId.systemDefault()).toLocalDate();
        return dateTimeFormatter.format(localDate.minusMonths(n).with(firstDayOfMonth()));
    }

    /**
     * 获取N个月前的最后一天
     */
    public static Date getNMonthLastDay(int n) {
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.MONTH, n);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        return ca.getTime();
    }

    /**
     * 获取N个月前的第一天
     */
    public static Date getNMonthFirstDay(int n) {
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.MONTH, n);
        ca.set(Calendar.DAY_OF_MONTH, 1);
        return ca.getTime();
    }

    /**
     * 获取N个季度前的第一天
     */
    public static String getNQuarterFirstDay(int n) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.MONTH, (startCalendar.get(Calendar.MONTH) / 3 + n) * 3);
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        return formatDate(startCalendar.getTime());
    }

    /**
     * 获取N个季度前的最后一天
     */
    public static String getNQuarterLastDay(int n) {
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.MONTH, (endCalendar.get(Calendar.MONTH) / 3 + n) * 3 + 2);
        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return formatDate(endCalendar.getTime());
    }

    /**
     * 获取N个半年度前的第一天
     */
    public static String getNHalfYearFirstDay(int n) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.MONTH, (startCalendar.get(Calendar.MONTH) / 6 + n) * 6);
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        return formatDate(startCalendar.getTime());
    }

    /**
     * 获取N个半年度前的最后一天
     */
    public static String getNHalfYearLastDay(int n) {
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.MONTH, (endCalendar.get(Calendar.MONTH) / 6 + n) * 6 + 5);
        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return formatDate(endCalendar.getTime());
    }

    /**
     * 获取去年年份
     */
    public static String getLastYear() {
        return getLastXYear(1);
    }

    /**
     * 获取几年前的年份
     */
    public static String getLastXYear(int num) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR) - num;
        return String.valueOf(year);
    }

    /**
     * 几个月前的第一天
     */
    public static String getLastMonth(int num) {
        return getLastMonth(num, YYYY_MM) + "-01";
    }

    /**
     * 几个月前的pattern日期
     */
    public static String getLastMonth(int num, String pattern) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1 - num);
        SimpleDateFormat dft = new SimpleDateFormat(pattern);
        return dft.format(cal.getTime());
    }

    /**
     * 获取本年第一天
     */
    public static Date getFirstMonthFirstDayOfThisYear() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getFirstMonthFirstDayOfYear(currentYear);
    }

    /**
     * 获取本年最后一天
     */
    public static Date getLastMonthLastDayOfThisYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.getActualMaximum(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    /**
     * 获取指定年份第一天
     */
    public static Date getFirstMonthFirstDayOfYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, year);
        return cal.getTime();
    }

    /**
     * 获取指定年份最后一天
     */
    public static Date getLastMonthLastDayOfYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, calendar.getActualMaximum(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    /**
     * 指定季度的开始时间
     */
    public static String getQuarterStartTime(int year, int quarter) {
        Calendar c = Calendar.getInstance();
        String dateStr = StringUtils.EMPTY;
        try {
            if (quarter == 1) {
                c.set(Calendar.MONTH, 0);
            } else if (quarter == 2) {
                c.set(Calendar.MONTH, 3);
            } else if (quarter == 3) {
                c.set(Calendar.MONTH, 6);
            } else if (quarter == 4) {
                c.set(Calendar.MONTH, 9);
            }
            c.set(Calendar.DATE, 1);
            dateStr = formatDate(c.getTime());
        } catch (Exception e) {
            // loger
        }
        return year + dateStr.substring(dateStr.indexOf('-'), dateStr.length());
    }

    /**
     * 指定季度的结束时间
     */
    public static String getQuarterEndTime(int year, int quarter) {
        Calendar c = Calendar.getInstance();
        String dateStr = StringUtils.EMPTY;
        try {
            if (quarter == 1) {
                c.set(Calendar.MONTH, 2);
                c.set(Calendar.DATE, 31);
            } else if (quarter == 2) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (quarter == 3) {
                c.set(Calendar.MONTH, 8);
                c.set(Calendar.DATE, 30);
            } else if (quarter == 4) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            dateStr = formatDate(c.getTime());
        } catch (Exception e) {
            // loger
        }
        return year + dateStr.substring(dateStr.indexOf('-'), dateStr.length());
    }

    /**
     * 指定半年度的开始时间
     */
    public static String getHalfYearStartTime(int year, int halfYear) {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(Calendar.YEAR, year);

        try {
            if (halfYear == 1) {
                c.set(Calendar.MONTH, 0);
            } else if (halfYear == 2) {
                c.set(Calendar.MONTH, 6);
            }
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            return formatDate(c.getTime());
        } catch (Exception e) {
            // loger
        }
        return null;
    }

    /**
     * 指定半年度的结束时间
     */
    public static String getHalfYearEndTime(int year, int halfYear) {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(Calendar.YEAR, year);

        try {
            if (halfYear == 1) {
                c.set(Calendar.MONTH, 5);
            } else if (halfYear == 2) {
                c.set(Calendar.MONTH, 11);
            }
            c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
            return formatDate(c.getTime());
        } catch (Exception e) {
            // loger
        }
        return null;
    }

    /**************************** 获取相对时间 end ********************************/

    /**************************** 获取时间段List begin ********************************/

    /**
     * 根据开始时间和结束时间返回时间段内的月度时间集合
     */
    public static List<Date> getMonthsBetweenTwoDate(Date beginDate, Date endDate) {

        List<Date> lDate = new ArrayList<>();
        // 把开始时间加入集合
        lDate.add(beginDate);
        Calendar cal = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        while (true) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.MONTH, 1);
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {
                lDate.add(cal.getTime());
            } else {
                break;
            }
        }
        // 把结束时间加入集合
        lDate.add(endDate);
        return lDate;
    }

    /**
     * 根据开始时间和结束时间返回时间段内的年度时间集合
     */
    public static List<Date> getYearsBetweenTwoDate(Date beginDate, Date endDate) {
        List<Date> lDate = new ArrayList<>();
        // 把开始时间加入集合
        lDate.add(beginDate);
        Calendar cal = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        while (true) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.YEAR, 1);
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {
                lDate.add(cal.getTime());
            } else {
                break;
            }
        }
        // 把结束时间加入集合
        lDate.add(endDate);
        return lDate;
    }

    /**
     * 时间段内的月度序列(去重)
     */
    public static List<String> getDatesBetweenTwoDateOfMonth(String beginDate, String endDate) {
        return getMonthsBetweenTwoDate(parseDate(beginDate), parseDate(endDate)).stream()
                .sorted((a, b) -> {
                    if (a.getTime() > b.getTime()) {
                        return 1;
                    } else if (a.getTime() < b.getTime()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }).map(d -> formatDate(d, YYYY_MM)).distinct().collect(Collectors.toList());
    }

    /**
     * 时间段内的月度序列(去重)
     */
    public static List<String> getSortedMonthBetweenTwoDate(Date beginDate, Date endDate) {
        List<String> sortedMonthList = new ArrayList<>();

        List<Date> datesBetweenTwoDate = getMonthsBetweenTwoDate(beginDate, endDate);
        for (Date date : datesBetweenTwoDate) {
            sortedMonthList.add(DateUtils.formatDate(date, YYYY_MM));
        }

        // 去掉重复月份(首尾判断)
        if (sortedMonthList != null && sortedMonthList.size() == 2) {
            if (StringUtils.equals(sortedMonthList.get(0), sortedMonthList.get(1))) {
                sortedMonthList.remove(0);
            }
        } else if (sortedMonthList != null && sortedMonthList.size() > 2) {
            if (StringUtils.equals(sortedMonthList.get(0), sortedMonthList.get(1))) {
                sortedMonthList.remove(0);
            }
            if (StringUtils.equals(sortedMonthList.get(sortedMonthList.size() - 1),
                    sortedMonthList.get(sortedMonthList.size() - 2))) {
                sortedMonthList.remove(sortedMonthList.size() - 1);
            }
        }
        return sortedMonthList;
    }

    /**
     * 时间段内的年度序列(去重)
     */
    public static List<String> getDatesBetweenTwoDateOfYear(String beginDate, String endDate) {
        return getYearsBetweenTwoDate(parseDate(beginDate), parseDate(endDate)).stream()
                .map(d -> formatDate(d, YYYY)).distinct().sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
    }

    /**
     * 时间段内的年度序列(去重)
     */
    public static List<String> getSortedYearsBetweenTwoDate(Date beginDate, Date endDate) {
        List<String> sortedYearList = new ArrayList<>();
        List<Date> datesBetweenTwoDate = getYearsBetweenTwoDate(beginDate, endDate);
        for (Date date : datesBetweenTwoDate) {
            sortedYearList.add(DateUtils.formatDate(date, "yyyy"));
        }

        // 去掉重复年份(首尾判断)
        if (sortedYearList != null && sortedYearList.size() == 2) {
            if (StringUtils.equals(sortedYearList.get(0), sortedYearList.get(1))) {
                sortedYearList.remove(0);
            }
        } else if (sortedYearList != null && sortedYearList.size() > 2) {
            if (StringUtils.equals(sortedYearList.get(0), sortedYearList.get(1))) {
                sortedYearList.remove(0);
            }
            if (StringUtils.equals(sortedYearList.get(sortedYearList.size() - 1),
                    sortedYearList.get(sortedYearList.size() - 2))) {
                sortedYearList.remove(sortedYearList.size() - 1);
            }
        }
        return sortedYearList;
    }

    /**
     * 根据开始时间与结束时间获取该时间段内的季度序列
     */
    public static List<String> getSeasonListByBeginAndEndDate(String beginDate, String endDate) {
        List<String> seasonList = new ArrayList<>();
        LocalDate beginLocalDate = LocalDate.parse(beginDate);
        int beginYear = beginLocalDate.getYear();
        int beginMonth = beginLocalDate.getMonth().getValue();

        LocalDate endLocalDate = LocalDate.parse(endDate);
        int endYear = endLocalDate.getYear();
        int endMonth = endLocalDate.getMonth().getValue();

        int beginSeason = 0;
        int endSeason = 0;
        if (beginMonth >= 1 && beginMonth <= 3) {
            beginSeason = 1;
        } else if (beginMonth >= 4 && beginMonth <= 6) {
            beginSeason = 2;
        } else if (beginMonth >= 7 && beginMonth <= 9) {
            beginSeason = 3;
        } else {
            beginSeason = 4;
        }
        if (endMonth >= 1 && endMonth <= 3) {
            endSeason = 1;
        } else if (endMonth >= 4 && endMonth <= 6) {
            endSeason = 2;
        } else if (endMonth >= 7 && endMonth <= 9) {
            endSeason = 3;
        } else {
            endSeason = 4;
        }
        if (beginYear == endYear) {
            for (int i = beginSeason; i <= endSeason; i++) {
                seasonList.add(beginYear + "-" + i);
            }
        } else {
            for (int i = beginSeason; i <= 4; i++) {
                seasonList.add(beginYear + "-" + i);
            }
            for (int i = beginYear + 1; i <= endYear - 1; i++) {
                for (int j = 1; j <= 4; j++) {
                    seasonList.add(i + "-" + j);
                }
            }
            for (int i = 1; i <= endSeason; i++) {
                seasonList.add(endYear + "-" + i);
            }
        }
        return seasonList;
    }

    /**************************** 获取时间段List end ********************************/

    /**
     * 本年在坐标轴的显示
     */
    public static String getThisYearAxisShow() {
        String yearStr;
        String thisyear = getYear();
        String thismonth = getMonth();
        if ("01".equals(thismonth)) {
            yearStr = thisyear.substring(2, 4) + ":1";
        } else {
            yearStr = thisyear.substring(2, 4) + ":1-" + thismonth;
            yearStr = yearStr.replace("-0", "-");
        }
        return yearStr;
    }

    /**
     * 获取日期时间在当年季度
     * 1 第一季度 2 第二季度 3 第三季度 4 第四季度
     */
    public static int getQuarter(Date date) {
        int season = 0;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.FEBRUARY:
            case Calendar.MARCH:
                season = 1;
                break;
            case Calendar.APRIL:
            case Calendar.MAY:
            case Calendar.JUNE:
                season = 2;
                break;
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.SEPTEMBER:
                season = 3;
                break;
            case Calendar.OCTOBER:
            case Calendar.NOVEMBER:
            case Calendar.DECEMBER:
                season = 4;
                break;
            default:
                break;
        }
        return season;
    }

    /**
     * 获取日期时间半年度
     * 1 上半年 2 下半年
     */
    public static int getHalfYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);
        if (month < 6) {
            return 1;
        } else {
            return 2;
        }
    }

    /**
     * 判断时间格式 格式必须为pattern
     */
    public static boolean isLegalDate(String sDate, String pattern) {
        int legalLen = pattern.length();
        if ((sDate == null) || (sDate.length() != legalLen)) {
            return false;
        }

        DateFormat formatter = new SimpleDateFormat(pattern);
        try {
            Date date = formatter.parse(sDate);
            return sDate.equals(formatter.format(date));
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidDate(String date, String pattern) {
        if (StringUtils.isNoneBlank(date)) {
            Pattern pat = Pattern.compile(pattern);
            Matcher match = pat.matcher(date);
            return match.matches();
        } else {
            return false;
        }
    }


    public static Date getmonthLastDate(String str){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth yearMonth = YearMonth.parse(str, dateTimeFormatter);
        Instant instant = yearMonth.atEndOfMonth().atStartOfDay(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    public static Date getmonthFirstDate(String str){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth yearMonth = YearMonth.parse(str, dateTimeFormatter);
        Instant instant = yearMonth.atDay(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    public static String getYearMonth(String date){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        YearMonth yearMonth = YearMonth.parse(date, dateTimeFormatter);
        DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM");
        return yearMonth.format(dateTimeFormatter2);
    }

    public static LocalDate getLocalDate(String date){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, dateTimeFormatter);
    }
}
