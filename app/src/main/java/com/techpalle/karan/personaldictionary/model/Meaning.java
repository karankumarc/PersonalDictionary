package com.techpalle.karan.personaldictionary.model;

/**
 * Created by ADMIN on 9/21/2016.
 */
public class Meaning {
    private String meaning;
    private int score;

    public Meaning(String meaning, int score) {
        this.meaning = meaning;
        this.score = score;
    }

    public String getMeaning() {
        return meaning;
    }

    public int getScore() {
        return score;
    }
}
