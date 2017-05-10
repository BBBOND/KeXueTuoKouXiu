package com.bbbond.kexuetuokouxiu.utils;

import android.util.Log;

import com.bbbond.kexuetuokouxiu.BuildConfig;

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
                Log.d(clas.getSimpleName(), methodName + "------> size: " + ((List) logContent).size());
                for (Object obj : (List) logContent)
                    Log.d(clas.getSimpleName(), methodName + "------>" + obj.toString());
            } else if (logContent instanceof String[]) {
                int length = ((String[]) logContent).length;
                Log.d(clas.getSimpleName(), methodName + "------> length: " + length);
                for (int i = 0; i < length; i++) {
                    Log.d(clas.getSimpleName(), methodName + "------>" + ((String[]) logContent)[i]);
                }
            } else
                Log.d(clas.getSimpleName(), methodName + "------>" + String.valueOf(logContent));
            Log.d(clas.getSimpleName(), methodName + "===========================");
        }
    }

    public static void e(Class clas, String methodName, String errMsg) {
        if (BuildConfig.DEBUG) {
            Log.e(clas.getSimpleName(), methodName + "===========================");
            Log.e(clas.getSimpleName(), methodName + "------>" + errMsg);
            Log.e(clas.getSimpleName(), methodName + "===========================");
        }
    }

    public static void w(Class clas, String methodName, String warningMsg) {
        if (BuildConfig.DEBUG) {
            Log.w(clas.getSimpleName(), methodName + "===========================");
            Log.w(clas.getSimpleName(), methodName + "------>" + warningMsg);
            Log.w(clas.getSimpleName(), methodName + "===========================");
        }
    }
}
