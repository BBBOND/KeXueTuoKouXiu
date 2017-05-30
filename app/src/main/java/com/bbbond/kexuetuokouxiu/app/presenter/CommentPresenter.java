package com.bbbond.kexuetuokouxiu.app.presenter;

import com.bbbond.kexuetuokouxiu.app.contract.CommentContract;
import com.bbbond.kexuetuokouxiu.app.model.CommentModel;
import com.bbbond.kexuetuokouxiu.bean.Comment;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by bbbond on 2017/5/30.
 */

public class CommentPresenter implements CommentContract.Presenter {

    private CommentContract.View view;
    private CommentContract.Model model;

    public CommentPresenter(CommentContract.View view) {
        this.view = view;
        this.model = new CommentModel();
    }

    @Override
    public void fetchComments(String url) {
        model
                .getComments(url)
                .subscribe(new Observer<List<Comment>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        view.showDialog();
                    }

                    @Override
                    public void onNext(List<Comment> comments) {
                        view.receiveCommentList(comments);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showNoData();
                        view.hideDialog();
                    }

                    @Override
                    public void onComplete() {
                        view.hideDialog();
                    }
                });
    }

}
