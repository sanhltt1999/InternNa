package com.example.dictionary

import android.content.Context
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper

class DataOpenHelper(private val context: Context): SQLiteAssetHelper(context, DB_NAME, null, DB_VERSION) {
    companion object{
        const val DB_NAME = "eng_dictionary.db"
        private const val DB_VERSION = 1
    }
}