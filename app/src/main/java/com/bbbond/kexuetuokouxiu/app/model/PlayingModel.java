package com.bbbond.kexuetuokouxiu.app.model;

import com.bbbond.kexuetuokouxiu.app.contract.PlayingContract;
import com.bbbond.kexuetuokouxiu.bean.Programme;
import com.bbbond.kexuetuokouxiu.db.ProgrammeDao;

/**
 * Created by bbbond on 2017/5/11.
 */

public class PlayingModel implements PlayingContract.Model {

    @Override
    public Programme getProgrammeById(String id) {
        return ProgrammeDao.getInstance().getProgrammeById(id);
    }
}
