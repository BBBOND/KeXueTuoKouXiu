package com.bbbond.kexuetuokouxiu.app.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bbbond.kexuetuokouxiu.R;
import com.bbbond.kexuetuokouxiu.app.contract.PlayingContract;
import com.bbbond.kexuetuokouxiu.app.presenter.PlayingPresenter;
import com.bbbond.kexuetuokouxiu.app.service.DownloadService;
import com.bbbond.kexuetuokouxiu.bean.Programme;
import com.bbbond.kexuetuokouxiu.bean.ProgrammeCache;
import com.bbbond.kexuetuokouxiu.db.ProgrammeCacheDao;
import com.bbbond.kexuetuokouxiu.helper.NetHelper;
import com.bbbond.kexuetuokouxiu.utils.LogUtil;
import com.bbbond.kexuetuokouxiu.utils.ParseUtil;
import com.bbbond.kexuetuokouxiu.utils.StrUtil;
import com.bbbond.simpleplayer.SimplePlayer;
import com.bbbond.simpleplayer.model.MediaData;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

import static com.bbbond.simpleplayer.SimplePlayer.EXTRA_CURRENT_MEDIA_DESCRIPTION;

public class PlayingActivity extends BaseActivity implements PlayingContract.View, View.OnClickListener {

    public static final String PROGRAMME = "PROGRAMME";

    private TextView title, currentTime, totalTime;
    private ImageButton about, download, comment, rewind, play, forward;
    private SeekBar sbProgress;

    private Programme programme;
    ProgrammeCache cache;
    private PlayingContract.Presenter presenter;
    private int playState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        presenter = new PlayingPresenter(this);

        initData();
        initView();
        initEvent();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent == null) {
            Toast.makeText(this, "打开异常", Toast.LENGTH_SHORT).show();
            finishActivity();
            return;
        } else {
            programme = intent.getParcelableExtra(PROGRAMME);
            MediaDescriptionCompat mediaDescriptionCompat = intent.getParcelableExtra(EXTRA_CURRENT_MEDIA_DESCRIPTION);
            if (programme == null && mediaDescriptionCompat != null) {
                programme = presenter.getProgrammeById(mediaDescriptionCompat.getMediaId());
            }
            LogUtil.d(PlayingActivity.class, "initData", programme);
            if (programme == null && mediaDescriptionCompat == null) {
                Toast.makeText(this, "打开异常", Toast.LENGTH_SHORT).show();
                finishActivity();
                return;
            }
            if (programme != null) {
                cache = ProgrammeCacheDao.getInstance().getCacheById(programme.getId());
                LogUtil.d(PlayingActivity.class, "initData", cache);
            }
        }
    }

    private void initView() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title = (TextView) findViewById(R.id.tv_title);
        currentTime = (TextView) findViewById(R.id.tv_current_time);
        totalTime = (TextView) findViewById(R.id.tv_total_time);
        about = (ImageButton) findViewById(R.id.ibtn_about);
        download = (ImageButton) findViewById(R.id.ibtn_download);
        comment = (ImageButton) findViewById(R.id.ibtn_comment);
        rewind = (ImageButton) findViewById(R.id.ibtn_rewind);
        play = (ImageButton) findViewById(R.id.ibtn_play);
        forward = (ImageButton) findViewById(R.id.ibtn_forward);
        sbProgress = (SeekBar) findViewById(R.id.sb_progress);

        title.setText(programme.getTitle());
        totalTime.setText(programme.getDuration());

        play.setEnabled(false);
        forward.setEnabled(false);
        rewind.setEnabled(false);
        sbProgress.setEnabled(false);
        download.setVisibility(null != cache && cache.isFinished() ? View.GONE : View.VISIBLE);
    }

    private void initEvent() {
        about.setOnClickListener(this);
        download.setOnClickListener(this);
        comment.setOnClickListener(this);
        rewind.setOnClickListener(this);
        forward.setOnClickListener(this);
        play.setOnClickListener(this);
        sbProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());
    }

    private void startPlay() {
        if (programme.getMediaUrl().equals(SimplePlayer.getInstance().getMediaUri())
                || (cache != null && cache.getPath().equals(SimplePlayer.getInstance().getMediaUri()))) {
            if (SimplePlayer.getInstance().getState() == PlaybackStateCompat.STATE_PAUSED
                    || SimplePlayer.getInstance().getState() == PlaybackStateCompat.STATE_STOPPED) {
                SimplePlayer.getInstance().getTransportControls(this, new SimplePlayer.GetTransportControlsCallback() {
                    @Override
                    public void success(MediaControllerCompat.TransportControls transportControls) {
                        if (transportControls != null) {
                            transportControls.play();
                            Toast.makeText(PlayingActivity.this, "开始加载节目...", Toast.LENGTH_SHORT).show();
                        } else {
                            LogUtil.e(PlayingActivity.class, "startPlay", "获取控制器失败");
                        }
                    }

                    @Override
                    public void error(String errorMsg) {
                        LogUtil.e(PlayingActivity.class, "startPlay", errorMsg);
                    }
                });
            }
        } else {
            List<MediaData> mediaDataList = new ArrayList<>();
            MediaData mediaData = ParseUtil.parseProgramme2MediaData(programme);
            if (cache != null && cache.isFinished() && new File(cache.getPath()).exists()) {
                mediaData.setMediaUri(cache.getPath());
                LogUtil.d(PlayingActivity.class, "startPlay", cache.getPath());
            }
            mediaDataList.add(mediaData);
            SimplePlayer.getInstance().setMediaDataList("music", mediaDataList, null);
            SimplePlayer.getInstance().getTransportControls(this, new SimplePlayer.GetTransportControlsCallback() {
                @Override
                public void success(MediaControllerCompat.TransportControls transportControls) {
                    if (transportControls != null) {
                        transportControls.playFromMediaId(programme.getId(), null);
                        Toast.makeText(PlayingActivity.this, "开始加载节目...", Toast.LENGTH_SHORT).show();
                    } else {
                        LogUtil.e(PlayingActivity.class, "startPlay", "获取控制器失败");
                    }
                }

                @Override
                public void error(String errorMsg) {
                    LogUtil.e(PlayingActivity.class, "startPlay", errorMsg);
                }
            });
        }
    }

    private void play() {
        SimplePlayer.getInstance().getTransportControls(this, new SimplePlayer.GetTransportControlsCallback() {
            @Override
            public void success(MediaControllerCompat.TransportControls transportControls) {
                if (transportControls != null) {
                    transportControls.play();
                } else {
                    LogUtil.e(PlayingActivity.class, "play", "获取控制器失败");
                }
            }

            @Override
            public void error(String errorMsg) {
                LogUtil.e(PlayingActivity.class, "play", errorMsg);
            }
        });
    }

    private void pause() {
        SimplePlayer.getInstance().getTransportControls(this, new SimplePlayer.GetTransportControlsCallback() {
            @Override
            public void success(MediaControllerCompat.TransportControls transportControls) {
                if (transportControls != null) {
                    transportControls.pause();
                } else {
                    LogUtil.e(PlayingActivity.class, "pause", "获取控制器失败");
                }
            }

            @Override
            public void error(String errorMsg) {
                LogUtil.e(PlayingActivity.class, "pause", errorMsg);
            }
        });
    }

    private void stop() {
        SimplePlayer.getInstance().getTransportControls(this, new SimplePlayer.GetTransportControlsCallback() {
            @Override
            public void success(MediaControllerCompat.TransportControls transportControls) {
                if (transportControls != null) {
                    transportControls.stop();
                } else {
                    LogUtil.e(PlayingActivity.class, "stop", "获取控制器失败");
                }
            }

            @Override
            public void error(String errorMsg) {
                LogUtil.e(PlayingActivity.class, "stop", errorMsg);
            }
        });
    }

    private void rewind() {
        SimplePlayer.getInstance().getTransportControls(this, new SimplePlayer.GetTransportControlsCallback() {
            @Override
            public void success(MediaControllerCompat.TransportControls transportControls) {
                if (transportControls != null)
                    transportControls.seekTo((sbProgress.getProgress() - 1) * SimplePlayer.getInstance().getDuration() / sbProgress.getMax());
            }

            @Override
            public void error(String errorMsg) {

            }
        });
    }

    private void forward() {
        SimplePlayer.getInstance().getTransportControls(this, new SimplePlayer.GetTransportControlsCallback() {
            @Override
            public void success(MediaControllerCompat.TransportControls transportControls) {
                if (transportControls != null)
                    transportControls.seekTo((sbProgress.getProgress() + 1) * SimplePlayer.getInstance().getDuration() / sbProgress.getMax());
            }

            @Override
            public void error(String errorMsg) {

            }
        });
    }

    private void initState(int state) {
        switch (state) {
            case PlaybackStateCompat.STATE_PAUSED:
                playState = 1;
                play.setEnabled(true);
                play.setImageResource(R.drawable.ic_play);
                forward.setEnabled(true);
                rewind.setEnabled(true);
                sbProgress.setEnabled(true);
                break;
            case PlaybackStateCompat.STATE_PLAYING:
                playState = 2;
                play.setEnabled(true);
                play.setImageResource(R.drawable.ic_pause);
                forward.setEnabled(true);
                rewind.setEnabled(true);
                sbProgress.setEnabled(true);
                break;
            case PlaybackStateCompat.STATE_STOPPED:
                playState = 0;
                play.setEnabled(true);
                play.setImageResource(R.drawable.ic_play);
                forward.setEnabled(true);
                rewind.setEnabled(true);
                sbProgress.setEnabled(false);
                break;
            case PlaybackStateCompat.STATE_BUFFERING:
                playState = 3;
                play.setEnabled(false);
                forward.setEnabled(false);
                rewind.setEnabled(false);
                sbProgress.setEnabled(false);
                break;
        }
    }

    private void registerProgressListener() {
        SimplePlayer.getInstance().setOnProgressChangeListener(new SimplePlayer.OnProgressChangeListener() {
            @Override
            public void progressChanged(int progress) {
                if (sbProgress != null) {
                    sbProgress.setProgress(progress);
                }
            }

            @Override
            public void secondaryProgressChanged(int progress) {
                if (sbProgress != null) {
                    progress = (int) ((progress / 100.0) * sbProgress.getMax());
                    sbProgress.setSecondaryProgress(progress);
                }
            }

            @Override
            public void completion() {
                stop();
            }
        });
    }

    private void unregisterProgressListener() {
        SimplePlayer.getInstance().setOnProgressChangeListener(null);
    }

    private void startDownload(final boolean downloadNow) {
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .doOnNext(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {

                        if (!granted) {  //权限被拒绝
                            Toast.makeText(getApplicationContext(), "下载已取消", Toast.LENGTH_SHORT).show();
                        } else {  // 开始下载
                            ProgrammeCache programmeCache = new ProgrammeCache();
                            programmeCache.setId(programme.getId());
                            programmeCache.setCategory(programme.getCategory());
                            programmeCache.setCreator(programme.getCreator());
                            programmeCache.setTitle(programme.getTitle());
                            programmeCache.setPath(StrUtil.getDiskCachePath(PlayingActivity.this) + "/" + programme.getId());
                            programmeCache.setUrl(programme.getMediaUrl());
                            programmeCache.setSize(-1L);
                            programmeCache.setFinished(false);
                            ProgrammeCacheDao.getInstance().saveOrUpdate(programmeCache);

                            if (downloadNow) {
                                Intent intent = new Intent(getApplicationContext(), DownloadService.class);
                                intent.putExtra(DownloadService.PROGRAMME, programmeCache);
                                startService(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "等待Wi-Fi链接后再试", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }).subscribe();

    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishActivity();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_about:
                createDialog(programme.getTitle(), programme.getSummary())
                        .setPositiveButton("知道了", null)
                        .create().show();
                break;
            case R.id.ibtn_play:
                switch (playState) {
                    case 0:
                        startPlayAfterCheck();
                        break;
                    case 1:
                        play();
                        break;
                    case 2:
                        pause();
                        break;
                }
                break;
            case R.id.ibtn_forward:
                forward();
                break;
            case R.id.ibtn_rewind:
                rewind();
                break;
            case R.id.ibtn_download:
                startDownloadAfterCheck();
                break;
        }
    }

    private void startDownloadAfterCheck() {
        if (NetHelper.getAPNType(getApplicationContext()) == NetHelper.NO_NETWORK) {
            Toast.makeText(getApplicationContext(), "无法访问网络，请检查网络状态", Toast.LENGTH_SHORT).show();
            initState(PlaybackStateCompat.STATE_STOPPED);
        } else if (NetHelper.getAPNType(getApplicationContext()) != NetHelper.WIFI) {
            createDialog("高能预警", "当前正使用4G/3G/2G网络，是否继续下载？")
                    .setPositiveButton("立即下载", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startDownload(true);
                        }
                    })
                    .setNegativeButton("等待WLAN", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startDownload(false);
                        }
                    })
                    .create().show();
        } else {
            startDownload(true);
        }
    }

    private void startPlayAfterCheck() {
        if (cache != null && cache.isFinished() && new File(cache.getPath()).exists()) {
            startPlay();
        } else if (NetHelper.getAPNType(getApplicationContext()) == NetHelper.NO_NETWORK) {
            Toast.makeText(getApplicationContext(), "无法访问网络，请检查网络状态", Toast.LENGTH_SHORT).show();
            initState(PlaybackStateCompat.STATE_STOPPED);
        } else if (NetHelper.getAPNType(getApplicationContext()) != NetHelper.WIFI) {
            createDialog("高能预警", "当前正使用4G/3G/2G网络，是否继续播放？")
                    .setPositiveButton("土豪，继续播", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startPlay();
                        }
                    })
                    .setNegativeButton("算了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            initState(PlaybackStateCompat.STATE_STOPPED);
                        }
                    })
                    .create().show();
        } else {
            startPlay();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerProgressListener();
        SimplePlayer.getInstance().registerMediaControllerCallback(new MediaControllerCompat.Callback() {
            @Override
            public void onMetadataChanged(MediaMetadataCompat metadata) {
                super.onMetadataChanged(metadata);
                if (metadata != null && metadata.getDescription() != null)
                    LogUtil.d(PlayingActivity.class, "onMetadataChanged: ", metadata.getDescription().getMediaUri());
            }

            @Override
            public void onPlaybackStateChanged(PlaybackStateCompat state) {
                initState(state.getState());
            }
        });
        initState(SimplePlayer.getInstance().getState());
        if (!programme.getMediaUrl().equals(SimplePlayer.getInstance().getMediaUri()))
            startPlayAfterCheck();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterProgressListener();
        SimplePlayer.getInstance().unregisterMediaControllerCallback();
    }

    public AlertDialog.Builder createDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(Html.fromHtml(message));
        return builder;
    }

    private class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
        int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            this.progress = progress * SimplePlayer.getInstance().getDuration() / seekBar.getMax();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            unregisterProgressListener();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            SimplePlayer.getInstance().getTransportControls(PlayingActivity.this, new SimplePlayer.GetTransportControlsCallback() {
                @Override
                public void success(MediaControllerCompat.TransportControls transportControls) {
                    if (transportControls != null)
                        transportControls.seekTo(progress);
                    registerProgressListener();
                }

                @Override
                public void error(String errorMsg) {
                    Toast.makeText(PlayingActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    registerProgressListener();
                }
            });

        }
    }
}
