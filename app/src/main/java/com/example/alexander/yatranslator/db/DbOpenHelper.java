package com.example.alexander.yatranslator.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import com.example.alexander.yatranslator.db.tables.ParametersTable;
import com.example.alexander.yatranslator.db.tables.TranslationsTable;

/**
 * Created by Alexander on 09.04.2017.
 */
public class DbOpenHelper extends SQLiteOpenHelper {

    public DbOpenHelper(@NonNull Context context) {
        super(context, "ya_translator_db_1", null, 4);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL(ParametersTable.getCreateTableQuery());
        db.execSQL(TranslationsTable.getCreateTableQuery());
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL(ParametersTable.getDropTableQuery());
//        db.execSQL(TranslationsTable.getDropTableQuery());
    }
}