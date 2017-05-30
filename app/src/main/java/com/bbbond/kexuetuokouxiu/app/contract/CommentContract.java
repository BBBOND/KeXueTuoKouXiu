package com.bbbond.kexuetuokouxiu.app.contract;

import com.bbbond.kexuetuokouxiu.bean.Comment;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by bbbond on 2017/5/30.
 */

public interface CommentContract {
    interface Model {
        Observable<List<Comment>> getComments(String url);
    }

    interface View {
        void receiveCommentList(List<Comment> comments);

        void showNoData();

        void showDialog();

        void hideDialog();
    }

    interface Presenter {
        void fetchComments(String url);
    }
}
