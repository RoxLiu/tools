package com.rox.app.tools.ini;

/**
 * Created by Rox on 2014/6/29.
 *
 */
public class Property {
    String key;
    String value;

    public Property() {}

    public Property(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return key + "=" + value;
    }
}
