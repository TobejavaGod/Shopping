package com.neuedu.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * 时间的格式化类
 * joda-time
 * @author jyw
 * @date 2019/10/27-13:31
 */
public class DateUtils {

    private static final String FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 将字符串日期转为date
     * @param dateTimeStr 要转换的字符串
     * @param formatStr 转换的格式 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date strToDate(String dateTimeStr,String formatStr){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatStr);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return  dateTime.toDate();
    }

    // 重载 使用默认的格式化策略
    public static Date strToDate(String dateTimeStr){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(FORMAT_DEFAULT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return  dateTime.toDate();
    }

    public static String dateToStr(Date date,String formatStr){
        if(date==null){
            return null;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(formatStr);
    }

    // 重载 使用默认的格式化策略
    public static String dateToStr(Date date){
        if(date==null){
            return null;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(FORMAT_DEFAULT);
    }
}
