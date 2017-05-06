package com.bbbond.kexuetuokouxiu.app.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bbbond.kexuetuokouxiu.R;
import com.bbbond.kexuetuokouxiu.bean.Programme;
import com.bbbond.kexuetuokouxiu.utils.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by bbbond on 2017/5/6.
 */

public class ArticleDetailActivity extends BaseActivity {

    public static final String ARTICLE = "ARTICLE";

    private TextView tvTitle, tvTime, tvCreator, tvContent;
    private Programme programme;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        initData();
        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent == null || intent.getParcelableExtra(ARTICLE) == null) {
            Toast.makeText(getApplicationContext(), "打开异常", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            programme = intent.getParcelableExtra(ARTICLE);
            LogUtil.d(ArticleDetailActivity.class, "initData", programme);
        }
    }

    private void initView() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setTitle(programme.getTitle());

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvCreator = (TextView) findViewById(R.id.tvCreator);
        tvContent = (TextView) findViewById(R.id.tvContent);

        tvTitle.setText(programme.getTitle());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        tvTime.setText(sdf.format(new Date(programme.getPubDate())));
        tvCreator.setText(programme.getCreator());
        tvContent.setText(Html.fromHtml(programme.getSummary()));
        tvContent.setTextIsSelectable(true);
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

    public void openLink(View view) {
        if (programme != null) {
            Uri uri = Uri.parse(programme.getLink());
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }
}
