package com.bbbond.kexuetuokouxiu.app.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import com.bbbond.kexuetuokouxiu.R;
import com.bbbond.kexuetuokouxiu.bean.ProgrammeCache;
import com.bbbond.kexuetuokouxiu.db.ProgrammeCacheDao;
import com.bbbond.kexuetuokouxiu.utils.LogUtil;
import com.bbbond.kexuetuokouxiu.utils.StrUtil;

import java.util.Locale;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadEvent;
import zlc.season.rxdownload2.entity.DownloadFlag;
import zlc.season.rxdownload2.entity.DownloadStatus;

/**
 * 下载服务
 * Created by bbbond on 2017/5/27.
 */

public class DownloadService extends Service {

    public static final String PROGRAMME = "PROGRAMME";
    public static final int DOWNLOADING_ID = 19941;
    private static final int REQUEST_CODE = 102;

    private NotificationManagerCompat manager;
    private RxDownload mRxDownload;
    private Disposable subscribe;

    public static final String ACTION_PAUSE = "com.bbbond.kexuetuokouxiu.pause";
    public static final String ACTION_RESUME = "com.bbbond.kexuetuokouxiu.resume";

    private PendingIntent mPauseIntent;
    private PendingIntent mResumeIntent;

    private DownloadReceiver mDownloadReceiver = new DownloadReceiver();


    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d(DownloadService.class, "onCreate", "");
        mRxDownload = RxDownload
                .getInstance(getApplicationContext())
                .maxDownloadNumber(1)
                .maxThread(3);
        manager = NotificationManagerCompat.from(this);

        String pkg = getApplication().getPackageName();
        mPauseIntent = PendingIntent.getBroadcast(getApplicationContext(), REQUEST_CODE,
                new Intent(ACTION_PAUSE).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT);
        mResumeIntent = PendingIntent.getBroadcast(getApplicationContext(), REQUEST_CODE,
                new Intent(ACTION_RESUME).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_PAUSE);
        filter.addAction(ACTION_RESUME);
        registerReceiver(mDownloadReceiver, filter);

        manager.cancel(DOWNLOADING_ID);
        manager.cancel(DOWNLOADING_ID + 1);
        manager.cancel(DOWNLOADING_ID + 2);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final ProgrammeCache cache = intent.getParcelableExtra(PROGRAMME);
        LogUtil.d(DownloadService.class, "onStartCommand", "");
        if (cache != null) {
            download(cache);
        }
        return START_REDELIVER_INTENT;
    }

    private void download(final ProgrammeCache cache) {
        if (cache != null) {
            LogUtil.d(DownloadService.class, "download", cache.getTitle());
            mRxDownload
                    .serviceDownload(cache.getUrl(), cache.getId(), StrUtil.getDiskCachePath(getApplicationContext()))
                    .subscribe();

            subscribe = mRxDownload
                    .receiveDownloadStatus(cache.getUrl())
                    .subscribe(new Consumer<DownloadEvent>() {
                        @Override
                        public void accept(DownloadEvent downloadEvent) throws Exception {
                            if (downloadEvent.getFlag() == DownloadFlag.FAILED) {
                                ProgrammeCacheDao.getInstance().deleteById(cache.getId());
                                LogUtil.d(DownloadService.class, "download", "下载失败");
                                Toast.makeText(getApplicationContext(), cache.getTitle() + "\n下载失败", Toast.LENGTH_SHORT).show();
                                if (subscribe != null && !subscribe.isDisposed()) {
                                    subscribe.dispose();
                                }
                            } else if (downloadEvent.getFlag() == DownloadFlag.COMPLETED) {
                                ProgrammeCacheDao.getInstance().downloadProgrammeFinish(cache.getId(), downloadEvent.getDownloadStatus().getTotalSize());
                                LogUtil.d(DownloadService.class, "download", "下载完成");
                                Toast.makeText(getApplicationContext(), "下载完成", Toast.LENGTH_SHORT).show();
                                if (subscribe != null && !subscribe.isDisposed()) {
                                    subscribe.dispose();
                                }
                            }
                            showNotification(cache.getTitle(), downloadEvent);
                        }
                    });
        }
    }

    public void showNotification(String title, DownloadEvent downloadEvent) {
        if (manager == null)
            manager = NotificationManagerCompat.from(this);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        int id;
        Notification notification;
        if (downloadEvent.getFlag() == DownloadFlag.FAILED) {
            manager.cancel(DOWNLOADING_ID);
            notification = builder
                    .setContentTitle(title)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText("下载失败")
                    .setOngoing(false)
                    .build();
            id = DOWNLOADING_ID + 1;
            LogUtil.d(DownloadService.class, "showNotification", "下载失败");
        } else if (downloadEvent.getFlag() == DownloadFlag.COMPLETED) {
            manager.cancel(DOWNLOADING_ID);
            notification = builder
                    .setContentTitle(title)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText("下载完成")
                    .setOngoing(false)
                    .build();
            id = DOWNLOADING_ID + 2;
            LogUtil.d(DownloadService.class, "showNotification", "下载完成");
        } else if (downloadEvent.getFlag() == DownloadFlag.STARTED) {
            DownloadStatus status = downloadEvent.getDownloadStatus();
            String s;
            try {
                s = String.format(Locale.CHINA, "%1$s/%2$s 已下载 %3$2d%%", status.getFormatDownloadSize(), status.getFormatTotalSize(), (status.getDownloadSize() * 100 / status.getTotalSize()));
            } catch (Exception e) {
                s = "0M/0M 0%";
                e.printStackTrace();
            }
            notification = builder
                    .setContentTitle(title)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText(s)
                    .setOngoing(true)
                    .setContentIntent(mPauseIntent)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .build();
            id = DOWNLOADING_ID;
        } else if (downloadEvent.getFlag() == DownloadFlag.PAUSED) {
            notification = builder
                    .setContentTitle(title)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText("已暂停")
                    .setContentIntent(mResumeIntent)
                    .setOngoing(false)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .build();
            id = DOWNLOADING_ID;
        } else {
            manager.cancel(DOWNLOADING_ID);
            return;
        }
        manager.notify(id, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mDownloadReceiver);
        LogUtil.d(DownloadService.class, "onDestroy", "");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class DownloadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            LogUtil.d(DownloadService.class, "onReceive", action);
            switch (action) {
                case ACTION_PAUSE:
                    mRxDownload
                            .pauseAll()
                            .subscribe();
                    break;
                case ACTION_RESUME:
                    mRxDownload
                            .startAll()
                            .subscribe();
                    break;
                default:
                    LogUtil.d(DownloadService.class, "onReceive", "为止的广播意图（已忽略）: " + action);
            }
        }
    }
}
