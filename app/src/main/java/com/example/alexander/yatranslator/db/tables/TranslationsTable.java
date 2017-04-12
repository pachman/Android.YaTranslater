package com.example.alexander.yatranslator.db.tables;

import android.support.annotation.NonNull;
import com.pushtorefresh.storio.sqlite.queries.Query;

/**
 * Created by Alexander on 09.04.2017.
 */
public class TranslationsTable {

    public static final String TABLE = "translations";

    public static final String COLUMN_ID = "_id";

    public static final String COLUMN_PARAMETERS_ID= "param_id";

    public static final String COLUMN_VALUE= "value";

    public static final String COLUMN_ID_WITH_TABLE_PREFIX = TABLE + "." + COLUMN_ID;
    public static final String COLUMN_PARAMETERS_ID_WITH_TABLE_PREFIX = TABLE + "." + COLUMN_PARAMETERS_ID;
    public static final String COLUMN_VALUE_WITH_TABLE_PREFIX = TABLE + "." + COLUMN_VALUE;

    public static final Query QUERY_ALL = Query.builder()
            .table(TABLE)
            .build();

    private TranslationsTable() {
        throw new IllegalStateException("No instances please");
    }

    @NonNull
    public static String getCreateTableQuery() {
        return "CREATE TABLE " + TABLE + "("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
                + COLUMN_PARAMETERS_ID + " INTEGER NOT NULL, "
                + COLUMN_VALUE + " TEXT NOT NULL"
                + ");";
    }

    @NonNull
    public static String getDropTableQuery() {
        return "DROP TABLE IF EXISTS " + TABLE + ";";
    }
}

