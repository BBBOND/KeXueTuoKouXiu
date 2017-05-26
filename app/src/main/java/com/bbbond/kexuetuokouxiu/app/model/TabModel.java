package com.bbbond.kexuetuokouxiu.app.model;

import com.bbbond.kexuetuokouxiu.app.contract.TabContract;
import com.bbbond.kexuetuokouxiu.bean.Programme;
import com.bbbond.kexuetuokouxiu.db.ProgrammeDao;
import com.bbbond.kexuetuokouxiu.network.RemoteClient;
import com.bbbond.kexuetuokouxiu.utils.LogUtil;
import com.bbbond.kexuetuokouxiu.utils.ParseUtil;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by bbbond on 2017/4/30.
 */

public class TabModel implements TabContract.Model {

    /**
     * 通过类别获取节目列表
     *
     * @param category 节目类型
     * @return Observable\<List\<Programme\>\>
     */
    @Override
    public Observable<List<Programme>> getProgrammeListFromLocalByCategories(final String[] category) {
        return ProgrammeDao.getInstance().rxGetProgrammeListByCategories(category);
    }

    /**
     * 保存programmeList
     *
     * @param programmeList 需要保存的数据
     * @return Observable\<Void\>
     */
    @Override
    public Observable<Void> saveProgrammeList(List<Programme> programmeList) {
        return ProgrammeDao.getInstance().rxSaveOrUpdate(programmeList);
    }

    /**
     * 通过服务器远程获取
     *
     * @return Observable\<List\<Programme\>\>
     */
    @Override
    public Observable<List<Programme>> fetchRemoteFromJson() {
        LogUtil.d(TabModel.class, "fetchRemoteFromJson", "");
        return RemoteClient
                .getProgrammesFromJson()
                .map(new Function<JSONArray, List<Programme>>() {
                    @Override
                    public List<Programme> apply(JSONArray jsonArray) throws Exception {
                        try {
                            List<Programme> programmes = new ArrayList<>();
                            Gson gson = new Gson();
                            int length = jsonArray.length();
                            try {
                                for (int i = 0; i < length; i++) {
                                    programmes.add(gson.fromJson(jsonArray.get(i).toString(), Programme.class));
                                }
                            } catch (Exception e) {
                                programmes = null;
                            }
                            return programmes;
                        } catch (Exception e) {
                            return null;
                        }
                    }
                });
    }

    /**
     * 通过官网远程获取
     *
     * @return Observable\<List\<Programme\>\>
     */
    @Override
    public Observable<List<Programme>> fetchRemoteFromXml() {
        LogUtil.d(TabModel.class, "fetchRemoteFromXml", "");
        return RemoteClient
                .getProgrammesFromXml()
                .map(new Function<String, List<Programme>>() {
                    @Override
                    public List<Programme> apply(String s) throws Exception {
                        try {
                            return ParseUtil.parseXml2ProgrammeList(s);
                        } catch (Exception e) {
                            return null;
                        }
                    }
                });
    }
}
