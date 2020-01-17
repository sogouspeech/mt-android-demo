package com.sogou.speech.mtdemo;

import android.text.TextUtils;
import android.util.Log;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Date:2018/6/28
 * Author:zhangxiaobei
 * Describe:
 */
public class TimeUtil {
    private static final String TAG = "TimeUtil";

    /**
     * 将时间戳转成RFC3399格式的时间
     *
     * @param timeStamp
     * @return
     */
    public static String timeStampToRFC3399Format(String timeStamp) {
        String rfc3399Time;
        if (!TextUtils.isEmpty(timeStamp)) {
            Date date = new Date(Long.valueOf(timeStamp));
            rfc3399Time = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(date);
        } else {
            rfc3399Time = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date());
        }
        Log.i(TAG, "rfc3399Time:" + rfc3399Time);
        return rfc3399Time;
    }

    /**
     * 将RFC3399格式的时间转成时间戳
     *
     * @param rFC3399FormatTime
     * @return
     */
    public static long rFC3399FormatToTimeStamp(String rFC3399FormatTime) {
        //LogUtil.d(TAG, "rFC3399FormatTime:" + rFC3399FormatTime);
        if (!TextUtils.isEmpty(rFC3399FormatTime)) {
            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                return dateFormat.parse(rFC3399FormatTime).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
                //LogUtil.e(TAG, "rFC3399FormatToTimeStamp,ParseException");
            }

        }
        return -1;
    }


}
