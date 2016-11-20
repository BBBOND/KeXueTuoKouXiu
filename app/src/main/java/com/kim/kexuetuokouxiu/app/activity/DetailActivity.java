package com.kim.kexuetuokouxiu.app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kim.kexuetuokouxiu.R;
import com.kim.kexuetuokouxiu.app.presenter.DetailPresenter;
import com.kim.kexuetuokouxiu.app.view.DetailView;
import com.kim.kexuetuokouxiu.bean.Programme;
import com.kim.kexuetuokouxiu.service.PlayService;

public class DetailActivity extends AppCompatActivity implements DetailView {

    private TextView tvSubTitle;
    private Programme programme;
    private FloatingActionButton fabPlay;
    private SeekBar sbState;
    private DetailPresenter presenter;
    private ProgressDialog progressDialog;

    private boolean isShowAll = false;

    private PlayService playService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initData();
        initView();
        initEvent();
        presenter = new DetailPresenter(this);
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent == null)
            finish();
        programme = intent.getParcelableExtra("programme");
        Log.d("---->", programme.toString());


//        // TODO: 2016/11/17
//        Intent i = new Intent(this, PlayService.class);
//        i.putExtra("programme", programme);
//        bindService(i, new ServiceConnection() {
//            @Override
//            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//                PlayService.LocalBinder binder = (PlayService.LocalBinder) iBinder;
//                playService = binder.getService();
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName componentName) {
//
//            }
//        }, Context.BIND_AUTO_CREATE);


    }

    private void initView() {
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvSubTitle = (TextView) findViewById(R.id.tvSubTitle);
        fabPlay = (FloatingActionButton) findViewById(R.id.fabPlay);
        sbState = (SeekBar) findViewById(R.id.sbState);

        setTitle(programme.getTitle());
        tvTitle.setText(programme.getTitle());
        tvSubTitle.setText(Html.fromHtml(programme.getContentEncoded()));
//        tvSubTitle.setText(programme.toString());
        sbState.setOnSeekBarChangeListener(new SeekBarChangeEvent());
        sbState.setEnabled(false);
    }

    private void initEvent() {
        tvSubTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowAll) {
                    tvSubTitle.setMaxLines(4);
                    isShowAll = false;
                } else {
                    tvSubTitle.setMaxLines(999);
                    isShowAll = true;
                }
            }
        });
        fabPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (presenter.isPlaying()) {
                    presenter.pause();
                } else {
                    if (presenter.isPlayed())
                        presenter.play();
                    else {
                        presenter.playFirst(programme.getEnclosureUrl());
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishActivity();
                break;
            case R.id.menu_link:
                if (programme != null) {
                    Uri uri = Uri.parse(programme.getLink());
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                }
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }

    private void finishActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            finishAfterTransition();
        else
            super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.stop();
    }

    @Override
    public void changeProgress(int progress) {
        if (sbState != null) {
            sbState.setProgress(progress);
        }
    }

    @Override
    public void secondaryProgressChanged(int progress) {
        sbState.setEnabled(true);
        if (sbState != null) {
            sbState.setSecondaryProgress(progress);
        }
    }

    @Override
    public void onCompletion() {
        // TODO: 2016/11/15 完成
    }

    @Override
    public void onPlaying() {
        fabPlay.setImageResource(R.drawable.ic_pause);
    }

    @Override
    public void onPaused() {
        fabPlay.setImageResource(R.drawable.ic_play);
    }

    @Override
    public void showProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("加载中...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
        }
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (progressDialog.isShowing())
            progressDialog.cancel();
    }

    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
        int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            this.progress = progress * presenter.getDuration() / seekBar.getMax();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            presenter.startChangeProgress();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            presenter.changeProgress(progress);
            presenter.endChangeProgress();
        }
    }
}
