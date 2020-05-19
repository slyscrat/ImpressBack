package com.slyscrat.impress.service.business;

import java.util.HashMap;
import java.util.Map;

public enum SortEnum {
    DateDSC(1),
    DateASC(2),
    NameASC(3),
    NameDSC(4);

    private final int number;
    SortEnum(int number) {
        this.number = number;
    }

    private static final Map<Integer,SortEnum> map;
    static {
        map = new HashMap<Integer,SortEnum>();
        for (SortEnum v : SortEnum.values()) {
            map.put(v.number, v);
        }
    }

    public static SortEnum findByKey(int i) {
        return map.get(i);
    }
}
