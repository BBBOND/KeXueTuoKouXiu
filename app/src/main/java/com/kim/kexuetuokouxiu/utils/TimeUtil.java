package com.kim.kexuetuokouxiu.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Weya on 2017/1/8.
 */

public class TimeUtil {

    public static long time2Long(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss", Locale.CHINA);
        try {
            Date date = sdf.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            LogUtil.e(TimeUtil.class, "time2Long", e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
}
