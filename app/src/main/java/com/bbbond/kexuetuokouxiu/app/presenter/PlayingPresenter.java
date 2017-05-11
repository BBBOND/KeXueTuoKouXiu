package com.bbbond.kexuetuokouxiu.app.presenter;

import com.bbbond.kexuetuokouxiu.app.contract.PlayingContract;
import com.bbbond.kexuetuokouxiu.app.model.PlayingModel;
import com.bbbond.kexuetuokouxiu.bean.Programme;

/**
 * Created by bbbond on 2017/5/11.
 */

public class PlayingPresenter implements PlayingContract.Presenter {

    private PlayingContract.View view;
    private PlayingContract.Model model;

    public PlayingPresenter(PlayingContract.View view) {
        this.view = view;
        this.model = new PlayingModel();
    }

    @Override
    public Programme getProgrammeById(String id) {
        return model.getProgrammeById(id);
    }
}
