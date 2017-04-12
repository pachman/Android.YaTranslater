package com.example.alexander.yatranslator.db.tables;

import android.support.annotation.NonNull;
import com.example.alexander.yatranslator.db.entities.TranslationItem;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.RawQuery;

import java.util.List;

/**
 * Created by Alexander on 09.04.2017.
 */
public class Relations {

    public static final String QUERY_COLUMN_PARAMETERS_ID = "parameters_id";

    public static final String QUERY_COLUMN_DIRECTION = "parameters_direct";

    public static final String QUERY_COLUMN_TEXT = "parameters_text";

    public static final String QUERY_COLUMN_TRANSLATION_ID = "translations_id";

    public static final String QUERY_COLUMN_TRANSLATION_VALUE = "translations_value";

    private final StorIOSQLite storIOSQLite;

    public Relations(@NonNull StorIOSQLite storIOSQLite) {
        this.storIOSQLite = storIOSQLite;
    }

    public List<TranslationItem> getTranslations() {
        return storIOSQLite
                .get()
                .listOfObjects(TranslationItem.class)
                .withQuery(RawQuery.builder()
                        .query("SELECT "
                                // Unfortunately we have columns with same names, so we need to give them aliases.
                                + ParametersTable.COLUMN_ID_WITH_TABLE_PREFIX + " AS \"" + QUERY_COLUMN_PARAMETERS_ID + "\""
                                + ", "
                                + ParametersTable.COLUMN_DIRECTION_WITH_TABLE_PREFIX + " AS \"" + QUERY_COLUMN_DIRECTION + "\""
                                + ", "
                                + ParametersTable.COLUMN_TEXT_WITH_TABLE_PREFIX + " AS \"" + QUERY_COLUMN_TEXT + "\""
                                + ", "
                                + TranslationsTable.COLUMN_VALUE_WITH_TABLE_PREFIX + " AS \"" + QUERY_COLUMN_TRANSLATION_VALUE + "\""
                                + " FROM " + ParametersTable.TABLE
                                + " JOIN " + TranslationsTable.TABLE
                                + " ON " + ParametersTable.COLUMN_ID_WITH_TABLE_PREFIX
                                + " = " + TranslationsTable.COLUMN_PARAMETERS_ID_WITH_TABLE_PREFIX)
                        .build())
                .prepare()
                .executeAsBlocking();
    }
}
