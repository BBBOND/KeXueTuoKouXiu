package com.bbbond.kexuetuokouxiu.utils;

import com.bbbond.kexuetuokouxiu.bean.Programme;

import java.util.List;

/**
 * Created by bbbond on 2017/4/21.
 */

public class PageUtil {

    private static final int PAGE_SIZE = 20;

    public static List<Programme> paging(List<Programme> allProgramme, int page) {
        if (page * PAGE_SIZE >= allProgramme.size())
            return allProgramme;
        return allProgramme.subList(0, page * PAGE_SIZE);
    }

    public static boolean hasNext(List<Programme> allProgramme, int page) {
        return page * PAGE_SIZE < allProgramme.size();
    }
}
