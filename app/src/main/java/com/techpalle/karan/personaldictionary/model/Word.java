package com.techpalle.karan.personaldictionary.model;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ADMIN on 9/18/2016.
 */
public class Word implements ParentListItem, Serializable{

    private int id;
    private String name;
    private String meaning;
    private String wordForm;
    private String usage;
    private String source;
    private boolean isHighlighted;
    private int score;

    public String getWordForm() {
        return wordForm;
    }

    public String getUsage() {
        return usage;
    }

    public String getSource() {
        return source;
    }

    private List<Meaning> mChildItemList;

    public Word(int id, String name, String meaning, int score, int isHighlighted) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.meaning = meaning;
        if(isHighlighted == 0){
            this.isHighlighted = false;
        } else if(isHighlighted == 1){
            this.isHighlighted = true;
        }
    }

    public Word(int id, String name, String meaning, int score, int isHighlighted, String usage, String source, String wordForm) {

        this.id = id;
        this.name = name;
        this.score = score;
        this.meaning = meaning;
        this.usage = usage;
        this.source = source;
        this.wordForm = wordForm;

        if(isHighlighted == 0){
            this.isHighlighted = false;
        } else if(isHighlighted == 1){
            this.isHighlighted = true;
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMeaning() {
        return meaning;
    }

    public boolean getIsHighlighted() {
        return isHighlighted;
    }

    public void setHighlighted(boolean highlighted) {
        isHighlighted = highlighted;
    }

    public int getScore() {
        return score;
    }

    @Override
    public List<?> getChildItemList() {
        return mChildItemList;
    }

    public void setChildItemList(List<Meaning> list) {
        mChildItemList = list;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
