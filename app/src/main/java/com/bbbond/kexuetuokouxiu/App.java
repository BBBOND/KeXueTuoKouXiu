package com.bbbond.kexuetuokouxiu;

import android.app.Application;

import com.bbbond.simpleplayer.helper.LogHelper;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.URLConnectionNetworkExecutor;
import com.yolanda.nohttp.cache.DBCacheStore;
import com.yolanda.nohttp.cookie.DBCookieStore;

import io.realm.Realm;

/**
 * Created by Weya on 2016/11/10.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NoHttp.initialize(this, new NoHttp.Config()
                .setConnectTimeout(30 * 1000)
                .setReadTimeout(30 * 1000)
                .setCacheStore(new DBCacheStore(this).setEnable(false))
                .setCookieStore(new DBCookieStore(this).setEnable(false))
                .setNetworkExecutor(new URLConnectionNetworkExecutor())
        );
        Logger.setDebug(true);
        Logger.setTag("NoHttp");
        Realm.init(this);
        LogHelper.init(BuildConfig.DEBUG);
    }
}
