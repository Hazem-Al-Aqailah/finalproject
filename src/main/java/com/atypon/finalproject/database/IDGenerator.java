package com.atypon.finalproject.database;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IDGenerator {

    private IDGenerator() {
    }

    @JsonProperty
    protected static long jsonId = 0;

    protected static void resetId() {
        jsonId = 0;
    }

    public static synchronized long generateId() {
        return jsonId++;
    }
}
