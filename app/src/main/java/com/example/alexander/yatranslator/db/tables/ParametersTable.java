package com.example.alexander.yatranslator.db.tables;

import android.support.annotation.NonNull;
import com.pushtorefresh.storio.sqlite.queries.Query;

public class ParametersTable {

    public static final String TABLE = "parameters";

    public static final String COLUMN_ID = "_id";

    public static final String COLUMN_ORDER = "updateDate";

    public static final String COLUMN_TYPE = "type";

    public static final String COLUMN_DIRECTION = "direction";

    public static final String COLUMN_TEXT = "text";

    public static final String COLUMN_ID_WITH_TABLE_PREFIX = TABLE + "." + COLUMN_ID;
    public static final String COLUMN_DIRECTION_WITH_TABLE_PREFIX = TABLE + "." + COLUMN_DIRECTION;
    public static final String COLUMN_TEXT_WITH_TABLE_PREFIX = TABLE + "." + COLUMN_TEXT;

    public static final Query QUERY_ALL_HISTORY = Query.builder()
            .table(TABLE)
            .where(COLUMN_TYPE + "= ?")
            .whereArgs(TranslationType.History)
            .orderBy(COLUMN_ORDER + " DESC")
            .build();

    public static final Query QUERY_ALL_FAVORITE = Query.builder()
            .table(TABLE)
            .where(COLUMN_TYPE + "= ?")
            .whereArgs(TranslationType.Favorite)
            .build();

    private ParametersTable() {
        throw new IllegalStateException("No instances please");
    }

    @NonNull
    public static String getCreateTableQuery() {
        return "CREATE TABLE " + TABLE + "("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
                + COLUMN_ORDER + " INTEGER NOT NULL, "
                + COLUMN_TYPE + " INTEGER NOT NULL, "
                + COLUMN_DIRECTION + " TEXT NOT NULL, "
                + COLUMN_TEXT + " TEXT NOT NULL"
                + ");";
    }

    @NonNull
    public static String getDropTableQuery() {
        return "DROP TABLE IF EXISTS " + TABLE + ";";
    }
}
