package com.example.dictionary;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "words")
public class Word {

    @PrimaryKey
    private int _id;
    private String en_word;
    private String en_definition;
    private String example;
    private String synonyms;
    private String antonyms;

    public Word(int _id, String en_word, String en_definition, String example, String synonyms, String antonyms) {
        this._id = _id;
        this.en_word = en_word;
        this.en_definition = en_definition;
        this.example = example;
        this.synonyms = synonyms;
        this.antonyms = antonyms;

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getEn_word() {
        return en_word;
    }

    public void setEn_word(String en_word) {
        this.en_word = en_word;
    }

    public String getEn_definition() {
        return en_definition;
    }

    public void setEn_definition(String en_definition) {
        this.en_definition = en_definition;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }

    public String getAntonyms() {
        return antonyms;
    }

    public void setAntonyms(String antonyms) {
        this.antonyms = antonyms;
    }

}
