package com.example.alexander.yatranslator.db;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.example.alexander.yatranslator.db.tables.TranslationsTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

/**
 * Created by Alexander on 09.04.2017.
 */

@StorIOSQLiteType(table = TranslationsTable.TABLE)
public class Translation {

    @Nullable
    @StorIOSQLiteColumn(name = TranslationsTable.COLUMN_ID, key = true)
    Long id;

    @NonNull
    @StorIOSQLiteColumn(name = TranslationsTable.COLUMN_PARAMETERS_ID)
    Long paramId;

    @NonNull
    @StorIOSQLiteColumn(name = TranslationsTable.COLUMN_VALUE)
    String value;

    public Translation() {
    }

    public Translation(@NonNull Long paramId, @NonNull String value) {
        this.paramId = paramId;
        this.value = value;
    }

    @Nullable
    public Long getId() {
        return id;
    }

    @NonNull
    public Long getParamId() {
        return paramId;
    }

    @NonNull
    public String getValue() {
        return value;
    }
}

