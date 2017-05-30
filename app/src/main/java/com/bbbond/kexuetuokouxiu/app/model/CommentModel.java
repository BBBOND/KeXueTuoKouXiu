package com.bbbond.kexuetuokouxiu.app.model;

import com.bbbond.kexuetuokouxiu.app.contract.CommentContract;
import com.bbbond.kexuetuokouxiu.bean.Comment;
import com.bbbond.kexuetuokouxiu.network.RemoteClient;
import com.bbbond.kexuetuokouxiu.utils.ParseUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by bbbond on 2017/5/30.
 */

public class CommentModel implements CommentContract.Model {

    @Override
    public Observable<List<Comment>> getComments(String url) {
        return RemoteClient
                .getComments(url)
                .map(new Function<String, List<Comment>>() {
                    @Override
                    public List<Comment> apply(String s) throws Exception {
                        return ParseUtil.parseXml2Comments(s);
                    }
                });
    }
}
