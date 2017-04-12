package com.example.alexander.yatranslator.db.resolvers;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;
import com.annimon.stream.Stream;
import com.example.alexander.yatranslator.db.Translation;
import com.example.alexander.yatranslator.db.TranslationParameters;
import com.example.alexander.yatranslator.db.entities.TranslationItem;
import com.example.alexander.yatranslator.db.tables.TranslationsTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.get.GetResolver;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.pushtorefresh.storio.sqlite.queries.RawQuery;

import java.util.List;

public class TranslationWithParametersGetResolver extends GetResolver<TranslationItem> {
    @NonNull
    private final GetResolver<TranslationParameters> parametersGetResolver;

    @NonNull
    private final ThreadLocal<StorIOSQLite> storIOSQLiteFromPerformGet = new ThreadLocal<>();

    public TranslationWithParametersGetResolver(@NonNull GetResolver<TranslationParameters> parametersGetResolver) {
        this.parametersGetResolver = parametersGetResolver;
    }

    @NonNull
    @Override
    public TranslationItem mapFromCursor(@NonNull Cursor cursor) {
        if(cursor == null ){
            return null;
        }
        Log.d("getColumnCount=>>> ", String.valueOf(cursor.getColumnCount()));
        for (String s : cursor.getColumnNames()) {
            Log.d("getColumnNames=>>> ", s);
        }

        Log.d("getCount =>>> ", String.valueOf(cursor.getCount()));

        final StorIOSQLite storIOSQLite = storIOSQLiteFromPerformGet.get();
        final TranslationParameters translationParameters = parametersGetResolver.mapFromCursor(cursor);

        final List<Translation> translations = storIOSQLite
                .get()
                .listOfObjects(Translation.class)
                .withQuery(Query.builder()
                        .table(TranslationsTable.TABLE)
                        .where(TranslationsTable.COLUMN_PARAMETERS_ID + "=?")
                        .whereArgs(translationParameters.getId())
                        .build())
                .prepare()
                .executeAsBlocking();
        List<String> strings = Stream.of(translations).map(t -> t.getValue()).toList();
        return new TranslationItem(translationParameters, strings);
    }

    @NonNull
    @Override
    public Cursor performGet(@NonNull StorIOSQLite storIOSQLite, @NonNull RawQuery rawQuery) {
        storIOSQLiteFromPerformGet.set(storIOSQLite);
        return storIOSQLite.lowLevel().rawQuery(rawQuery);
    }

    @NonNull
    @Override
    public Cursor performGet(@NonNull StorIOSQLite storIOSQLite, @NonNull Query query) {
        storIOSQLiteFromPerformGet.set(storIOSQLite);
        return storIOSQLite.lowLevel().query(query);
    }
}