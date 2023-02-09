package com.example.dictionary

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseAccess(context: Context) {
    private var openHelper: SQLiteOpenHelper? = DataOpenHelper(context)
    private var database: SQLiteDatabase? = null
    private var instance: DatabaseAccess? = null

    fun getInstance(context: Context): DatabaseAccess {
        if (instance == null) {
            instance = DatabaseAccess(context)
        }
        return instance as DatabaseAccess
    }

    private fun openDatabase() {

        this.database = openHelper?.writableDatabase

    }

    private fun closeDatabase() {
        this.database?.close()

    }

    fun getAllWords(): ArrayList<Word> {
        var word: Word?
        val words: ArrayList<Word> = ArrayList<Word>()
        openDatabase()
        val cursor: Cursor = database?.rawQuery("select * from " + "words", null)!!
        cursor?.moveToFirst()
        while (!cursor.isAfterLast()) {
            word = Word(cursor.getString(1), cursor.getString(2), cursor.getString(3))
            words.add(word)
            cursor.moveToNext()
        }
        cursor.close()
        closeDatabase()
        return words
    }
}