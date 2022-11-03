package com.example.dictionary;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WordDAO {
    @Query("SELECT *FROM words")
    List<Word> getListWord();
}
