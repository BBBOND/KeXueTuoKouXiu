package com.bbbond.kexuetuokouxiu.utils;

import android.content.Context;
import android.os.Environment;

import java.util.Locale;

/**
 * 字符串类工具
 * Created by bbbond on 2017/5/27.
 */

public class StrUtil {

    public static String byteFormat(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format(Locale.CHINA, "%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(Locale.CHINA, f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size > kb) {
            float f = (float) size / kb;
            return String.format(Locale.CHINA, f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format(Locale.CHINA, "%d B", size);
        }
    }

    public static String getDiskCachePath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            return context.getExternalFilesDir(Environment.DIRECTORY_PODCASTS).getPath();
        } else {
            return context.getFilesDir().getPath();
        }
    }
}
