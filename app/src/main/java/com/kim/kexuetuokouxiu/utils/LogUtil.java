package com.kim.kexuetuokouxiu.utils;

import android.util.Log;

import com.kim.kexuetuokouxiu.BuildConfig;

import java.util.List;

/**
 * Created by Weya on 2017/1/6.
 */

public class LogUtil {

    public static void d(Class clas, String methodName, Object logContent) {
        if (BuildConfig.DEBUG) {
            Log.d(clas.getSimpleName(), methodName + "===========================");
            if (logContent instanceof String)
                Log.d(clas.getSimpleName(), methodName + "---->" + logContent);
            else if (logContent instanceof List) {
                for (Object obj : (List) logContent)
                    Log.d(clas.getSimpleName(), methodName + "------>" + obj.toString());
            }
            Log.d(clas.getSimpleName(), methodName + "===========================");
        }
    }
}
