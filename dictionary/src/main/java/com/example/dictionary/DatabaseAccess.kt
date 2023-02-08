package com.example.dictionary

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseAccess(context: Context) {
    private var openHelper: SQLiteOpenHelper? = DataOpenHelper(context)
    private var database: SQLiteDatabase? = null
    private var instance: DatabaseAccess? = null

     fun getInstance (context: Context) : DatabaseAccess {
        if(instance == null){
            instance = DatabaseAccess(context)
        }
        return instance as DatabaseAccess
    }
    private fun openDatabase(){

        this.database = openHelper?.writableDatabase

    }
    private fun closeDatabase(){
            this.database?.close()

    }
    fun  getAllWords() : ArrayList<Word>{
        var word : Word?
        val words : ArrayList<Word> = ArrayList<Word>()
        openDatabase()
        val cursor : Cursor = database?.rawQuery("select * from " + "words", null)!!
        cursor?.moveToFirst()
        while (!cursor.isAfterLast()){
            word = Word(cursor.getString(1), cursor.getString(2), cursor.getString(3))
            words.add(word)
            cursor.moveToNext()
        }
        cursor.close()
        closeDatabase()
        return words
    }
//    private fun requestPermission(){
//        when {
//            ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.MANAGE_EXTERNAL_STORAGE
//            ) == PackageManager.PERMISSION_GRANTED -> {
//                // You can use the API that requires the permission.
//            }
//            shouldShowRequestPermissionRationale() -> {
//            // In an educational UI, explain to the user why your app requires this
//            // permission for a specific feature to behave as expected, and what
//            // features are disabled if it's declined. In this UI, include a
//            // "cancel" or "no thanks" button that lets the user continue
//            // using your app without granting the permission.
//            showInContextUI(...)
//        }
//            else -> {
//                // You can directly ask for the permission.
//                // The registered ActivityResultCallback gets the result of this request.
//                requestPermissions(CONTEXT,
//                    arrayOf(Manifest.permission.REQUESTED_PERMISSION),
//                    REQUEST_CODE)
//            }
//            }
//        }
//    }
}