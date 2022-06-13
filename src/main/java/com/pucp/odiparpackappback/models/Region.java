package com.pucp.odiparpackappback.models;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Region {
    COSTA(0),
    SIERRA(1),
    SELVA(2);

    private static final Map<Integer, Region> lookup = new HashMap<Integer, Region>();

    static {
        for (Region w : EnumSet.allOf(Region.class))
            lookup.put(w.getCode(), w);
    }

    private final int code;

    Region(int code) {
        this.code = code;
    }

    public static Region get(int code) {
        return lookup.get(code);
    }

    public int getCode() {
        return code;
    }
}
