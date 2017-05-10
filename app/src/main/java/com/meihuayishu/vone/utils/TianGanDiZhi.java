package com.meihuayishu.vone.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

/**
 * Created by Dell on 2017-2-21.
 * 根据传入的时间计算八字
 */
public class TianGanDiZhi {
    public static final String DIZHI_STR = "亥子丑寅卯辰巳午未申酉戌";
    public static final String TIANGAN_STR ="癸甲乙丙丁戊己庚辛壬";
    public static final String[]
            TIANGAN = new String[]{"癸", "甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬"},
            DIZHI = new String[]{"亥", "子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌"};
    private static long time2000_01_01, currenttime;
    private static String yearGan, yearZhi, monthGan, monthZhi, dayGan, dayZhi, hourGan, hourZhi;

    /**
     * @param time yyyy-MM-dd HH:mm
     * @return
     */
    public static String exchangeGanZhi(String time) {

        String mDate = time.split(" ")[0], mTime = time.split(" ")[1];
        String[] mDates = mDate.split("-");
        String[] mTimes = mTime.split(":");
        int year = Integer.valueOf(mDates[0]);
        int month = Integer.valueOf(mDates[1]);
        int day = Integer.valueOf(mDates[2]);
        int hour = Integer.valueOf(mTimes[0]);

        //年份干支
        yearGan = TIANGAN[(year - 3) % 10];
        yearZhi = DIZHI[(year - 3) % 12];

        //月份干支
        monthGan = TIANGAN[(((year - 3) % 10) * 2 + month - 1) % 10];

        if (month >= 1 && month <= 6) {
            if (day <= 4) {
                monthZhi = DIZHI[month % 12];
            } else {
                monthZhi = DIZHI[month % 12 + 1];
            }
        } else {
            if (day <= 7) {
                monthZhi = DIZHI[month % 12];
            } else {
                if (month % 12 == 11) monthZhi = DIZHI[0];
                monthZhi = DIZHI[month % 12 + 1];
            }
        }

        //日期干支
        long days = getIntercalDay(time);
        if (year < 2000) {
            dayGan = TIANGAN[(10+((int) (days - 6) % 10))%10];
            dayZhi = DIZHI[(12+((int)(days - 6) % 12))%12];
        } else {
            dayGan = TIANGAN[(int) (Math.abs((days + 5) % 10))];
            dayZhi = DIZHI[(int) (Math.abs((days + 5) % 12))];
        }

        //时辰干支
        int index = Arrays.asList(TIANGAN).indexOf(dayGan);
        if (index == 0) {
            index = 10;
        }
        hourZhi = DIZHI[((hour + 3) / 2) % 12];
        hourGan = TIANGAN[(index * 2 + (((hour + 3) / 2) % 12) - 2) % 10];

        //每天23：00 ~ 0：00的判断 是子时且hour == 23 日期干支后推一天，
            /*天数判断 日期是这个月的最后一天 如 day ==31 || day==30 && month ==小月 ||
             day == 28 && month ==2 || day ==29 && month ==2 && year==闰年 月份干支后推一个月*/
        //月份判断 month == 12 ,year后推一年
        if (hour == 23) {
            hour = 0;
            day ++;
            if (day == 31 ||
                    (day == 30 && (month ==4 || month == 6 || month ==9|| month ==11)) ||
                    (day == 29 && month ==2) ||
                    (day == 28 && month ==2 && ((year % 4 != 0 && year % 100 == 0 ) ||(year % 400 != 0)))){
                month++;
                day = 1;
                if (month ==12) {
                    month =1 ;
                    year ++;
                }

            }

            time = new StringBuffer().append(year +"-").append(month +"-").append(day +" ").append(hour+":").append(mTimes[1]).toString();
            return exchangeGanZhi(time);

        }

        return yearGan + yearZhi + monthGan + monthZhi + dayGan + dayZhi + hourGan + hourZhi;
    }

    /**
     * @param time 传入具体的时间，与2000年元旦计算天数差
     * @return 与2000年元旦相差天数
     */
    static long getIntercalDay(String time) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            time2000_01_01 = format.parse("1999-12-31 23:00").getTime();
            currenttime = format.parse(time).getTime();//微秒级别
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long days = (time2000_01_01 - currenttime) / (1000 * 60 * 60 * 24);
        return days;
    }

}
