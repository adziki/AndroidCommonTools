package com.meyouhealth.myhandroid.util;

public enum Environment {

    STAGING ("Staging", 200),
    PRODUCTION("Production", 300),
    DEV("Development", 400);

    public int id;
    public String displayName;

    Environment(String displayName, int id) {
        this.displayName = displayName;
        this.id = id;
    }

    public static Environment getById(int id) {

        if (id == STAGING.id) {
            return STAGING;
        } else if (id == PRODUCTION.id) {
            return PRODUCTION;
        } else {
            return DEV;
        }
    }
}
