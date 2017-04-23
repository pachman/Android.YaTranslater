package com.example.alexander.yatranslator.storio.tables;

import android.support.annotation.NonNull;
import com.example.alexander.yatranslator.storio.entities.TranslationParameters;
import com.pushtorefresh.storio.sqlite.queries.Query;

public class ParametersTable {

    public static final String TABLE = "parameters";

    public static final String COLUMN_ID = "_id";

    public static final String COLUMN_ORDER = "updateDate";

    public static final String COLUMN_TYPE = "type";

    public static final String COLUMN_IS_FAVORITE = "isFavorite";

    public static final String COLUMN_DIRECTION = "direction";

    public static final String COLUMN_TEXT = "text";

    private ParametersTable() {
        throw new IllegalStateException("No instances please");
    }

    @NonNull
    public static String getCreateTableQuery() {
        return "CREATE TABLE " + TABLE + "("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
                + COLUMN_ORDER + " INTEGER NOT NULL, "
                + COLUMN_TYPE + " INTEGER NOT NULL, "
                + COLUMN_IS_FAVORITE + " INTEGER NOT NULL, "
                + COLUMN_DIRECTION + " TEXT NOT NULL, "
                + COLUMN_TEXT + " TEXT NOT NULL"
                + ");";
    }

    @NonNull
    public static String getDropTableQuery() {
        return "DROP TABLE IF EXISTS " + TABLE + ";";
    }

    public static Query getQueryByType(Integer type) {
        return Query.builder()
                .table(TABLE)
                .where(COLUMN_TYPE + "= ?")
                .whereArgs(type)
                .orderBy(COLUMN_ORDER + " DESC")
                .build();
    }

    public static Query getSimilarByType(int type, TranslationParameters parameters){
        return getSimilarByType(type, parameters.getText(), parameters.getDirection());
    }

    public static Query getSimilarByType(int type, String text, String direction){
        return Query.builder()
                .table(ParametersTable.TABLE)
                .where(ParametersTable.COLUMN_TYPE + "=? AND " +
                        ParametersTable.COLUMN_TEXT + "=? AND " +
                        ParametersTable.COLUMN_DIRECTION + "=?")
                .whereArgs(type, text, direction)
                .build();
    }

}
