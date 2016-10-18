package com.techpalle.karan.personaldictionary.model;

/**
 * Created by ADMIN on 9/21/2016.
 */
public class Meaning {
    private int id;
    private String meaning;
    private int score;

    public Meaning(int id, String meaning, int score) {
        this.meaning = meaning;
        this.score = score;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getMeaning() {
        return meaning;
    }

    public int getScore() {
        return score;
    }
}
