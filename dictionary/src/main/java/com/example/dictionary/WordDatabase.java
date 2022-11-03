package com.example.dictionary;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Word.class}, version = 1, exportSchema = true)
public abstract class WordDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "dictionary.db";
    private static WordDatabase instance;
    public static synchronized WordDatabase getInstance(Context context){
        if(instance == null){
            instance =  Room.databaseBuilder(context, WordDatabase.class, DATABASE_NAME)
                    .createFromAsset("eng_dictionary.db")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
    public abstract WordDAO wordDAO();
}
