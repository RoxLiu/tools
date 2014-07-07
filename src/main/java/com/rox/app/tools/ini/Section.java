package com.rox.app.tools.ini;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rox on 2014/6/29.
 */
public class Section {
    String name;
    List<Property> properties = new ArrayList<Property>();

    public Section() {}

    public Section(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Property property(String key) {
        return find(key, false);
    }

    public Property property(String key, String value) {
        Property property = find(key, true);
        property.setValue(value);

        return property;
    }

//    public Property find(String key) {
//        return find(key, false);
//    }

    public Property find(String key, boolean addIfAbsent) {
        for(Property one : properties) {
            if(one.getKey().equals(key)) {
                return one;
            }
        }

        if(addIfAbsent) {
            Property property = new Property(key, "");
            properties.add(property);
            return property;
        }

        return null;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public String toString() {
        return "[" + name + "]";
    }
}
