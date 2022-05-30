package com.pucp.odiparpackappback.models;

import java.util.HashMap;
import java.util.Map;

public class DicTramos {
    private Map<Integer, Integer[]> map;
    public DicTramos() {
        map = new HashMap<Integer, Integer[]>();
    }
    public Map<Integer, Integer[]> getMap() {return map; }
    public void putMap(Map<Integer, Integer[]> newMap){map = newMap;}
}
