package com.example.dictionary

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class SQLite(private val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION ){
    companion object {
         const val DB_NAME = "databases/eng_dictionary.db"
        private const val DB_VERSION = 1
        private lateinit var database: SQLiteDatabase
        private const val TABLE = "words"

    }

    override fun onCreate(db: SQLiteDatabase?) {

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    private fun openDatabase(){
        val DBPath : String = context.getDatabasePath(DB_NAME).path

        database = SQLiteDatabase.openDatabase(DBPath,null, SQLiteDatabase.OPEN_READWRITE)
    }
    private fun closeDatabase(){
        database?.close()
    }
    fun  getAllWords() : ArrayList<Word>{
        var word : Word?
        val words : ArrayList<Word> = ArrayList<Word>()
        openDatabase()
        val cursor : Cursor = database.rawQuery("select * from " + TABLE, null)
        cursor.moveToFirst()
        while (!cursor.isAfterLast()){
            word = Word(cursor.getString(1), cursor.getString(2), cursor.getString(3))
            words.add(word)
            cursor.moveToNext()
        }
        cursor.close()
        closeDatabase()
        return words

    }

}