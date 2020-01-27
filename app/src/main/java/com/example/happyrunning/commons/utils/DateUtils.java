package com.example.happyrunning.commons.utils;

import com.example.happyrunning.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 描述: 时间格式工具类

 */
public class DateUtils {

    /**
     * 短日期补位
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String formatStringDateShort(int year, int month, int day) {
        return UIhelper.getString(R.string.date_year_month_day, String.valueOf(year),
                month < 10 ? "0" + month : String.valueOf(month),
                day < 10 ? "0" + day : String.valueOf(day));
    }

    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getStringDateShort(long time) {
        Date currentTime = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

}
