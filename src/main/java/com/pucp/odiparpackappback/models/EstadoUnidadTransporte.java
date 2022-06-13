package com.pucp.odiparpackappback.models;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EstadoUnidadTransporte {
    DISPONIBLE(0),
    RESERVADO(1),
    EN_TRANSITO(2),
    AVERIADO(3),
    EN_MANTENIMIENTO(4);

    private static final Map<Integer, EstadoUnidadTransporte> lookup = new HashMap<Integer, EstadoUnidadTransporte>();

    static {
        for (EstadoUnidadTransporte w : EnumSet.allOf(EstadoUnidadTransporte.class))
            lookup.put(w.getCode(), w);
    }

    private final int code;

    EstadoUnidadTransporte(int code) {
        this.code = code;
    }
    public static EstadoUnidadTransporte get(int code) {
        return lookup.get(code);
    }
    public int getCode() {
        return code;
    }
}
