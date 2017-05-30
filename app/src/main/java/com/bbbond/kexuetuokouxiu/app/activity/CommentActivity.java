package com.bbbond.kexuetuokouxiu.app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bbbond.kexuetuokouxiu.R;
import com.bbbond.kexuetuokouxiu.app.adapter.CommentAdapter;
import com.bbbond.kexuetuokouxiu.app.contract.CommentContract;
import com.bbbond.kexuetuokouxiu.app.presenter.CommentPresenter;
import com.bbbond.kexuetuokouxiu.bean.Comment;
import com.bbbond.kexuetuokouxiu.bean.Programme;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bbbond on 2017/5/30.
 */

public class CommentActivity extends BaseActivity implements CommentContract.View {

    private RecyclerView rvContent;
    private TextView tvNoComment;
    private ProgressDialog dialog;

    private Programme programme;
    private List<Comment> commentList;
    private CommentAdapter adapter;

    private CommentContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_comment);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter = new CommentPresenter(this);

        initData();
        initView();

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.fetchComments(programme.getCommentRss());
        showNoData();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            programme = intent.getParcelableExtra(PlayingActivity.PROGRAMME);
        }
        if (commentList == null)
            commentList = new ArrayList<>();
    }

    private void initView() {
        rvContent = (RecyclerView) findViewById(R.id.rv_content);
        tvNoComment = (TextView) findViewById(R.id.tv_no_data);
        getSupportActionBar().setTitle(programme.getTitle());

        adapter = new CommentAdapter(this, commentList);
        adapter.setOnLongClickListener(new CommentAdapter.OnLongClickListener() {
            @Override
            public boolean longClick(int pos, Comment comment) {

                return true;
            }
        });
        rvContent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvContent.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvContent.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void receiveCommentList(List<Comment> comments) {
        if (comments != null) {
            commentList.clear();
            commentList.addAll(comments);
            adapter.notifyDataSetChanged();
        }
        showNoData();
    }

    @Override
    public void showNoData() {
        if (commentList != null && commentList.size() > 0) {
            tvNoComment.setVisibility(View.GONE);
        } else {
            tvNoComment.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(this);
        }
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("加载中");
        dialog.show();
    }

    @Override
    public void hideDialog() {
        if (dialog.isShowing()) {
            dialog.hide();
        }
    }

}
