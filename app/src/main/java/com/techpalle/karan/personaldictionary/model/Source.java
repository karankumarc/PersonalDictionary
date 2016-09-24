package com.techpalle.karan.personaldictionary.model;

/**
 * Created by ADMIN on 9/19/2016.
 */
public class Source {
    private int id;
    private String name;

    public Source(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
