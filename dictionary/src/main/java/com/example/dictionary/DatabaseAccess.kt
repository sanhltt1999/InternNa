package com.example.dictionary

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.getIntOrNull

class DatabaseAccess(context: Context) {
    private var openHelper: SQLiteOpenHelper? = DataOpenHelper(context)
    private var database: SQLiteDatabase? = null
    private var instance: DatabaseAccess? = null
    private val table : String = "words"
    private val columnEnWord = "en_word"
    private val columnMean = "en_definition"
    private val columnExample = "example"
    private val columnSynonyms = "synonyms"
    private val columnAntonyms = "antonyms"
    private val columnSearched = "searched"
    private val columnFavorite = "favorite"


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
        val cursor: Cursor = database?.rawQuery("select * from " + table, null)!!
        cursor?.moveToFirst()
        while (!cursor.isAfterLast()) {
            word = Word(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getIntOrNull(6), cursor.getIntOrNull(7))
            words.add(word)
            cursor.moveToNext()
        }
        cursor.close()
        closeDatabase()
        return words
    }

    fun getHistory(): ArrayList<Word> {
        var word: Word?
        val words: ArrayList<Word> = ArrayList<Word>()
        openDatabase()
        val cursor: Cursor = database?.rawQuery("select * from " + table, null)!!
        cursor?.moveToFirst()
        while (!cursor.isAfterLast()) {
            if(cursor.getIntOrNull(6) == 1){
                word = Word(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                    cursor.getString(5), cursor.getIntOrNull(6), cursor.getIntOrNull(7))
                words.add(word)
            }
            cursor.moveToNext()
        }
        cursor.close()
        closeDatabase()
        return words
    }

    fun getFavoriteWord(): ArrayList<Word> {
        var word: Word?
        val words: ArrayList<Word> = ArrayList<Word>()
        openDatabase()
        val cursor: Cursor = database?.rawQuery("select * from " + table, null)!!
        cursor?.moveToFirst()
        while (!cursor.isAfterLast()) {
            if(cursor.getIntOrNull(7) == 1){
                word = Word(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                    cursor.getString(5), cursor.getIntOrNull(6), cursor.getIntOrNull(7))
                words.add(word)
            }
            cursor.moveToNext()
        }
        cursor.close()
        closeDatabase()
        return words
    }


    fun update(word: Word){
        openDatabase()
        var cv = ContentValues()
        cv.put(columnEnWord, word.en_Word)
        cv.put(columnMean, word.meaning)
        cv.put(columnExample, word.ex)
        cv.put(columnSynonyms, word.syn)
        cv.put(columnAntonyms, word.ant)
        cv.put(columnSearched, word.history)
        cv.put(columnFavorite, word.favorite)

        database?.update(table, cv, columnEnWord + "=?", arrayOf(word.en_Word))

    }
}