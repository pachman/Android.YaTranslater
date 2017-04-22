package com.example.alexander.yatranslator.db.resolvers;

import android.support.annotation.NonNull;

import android.util.Log;
import com.example.alexander.yatranslator.db.Translation;
import com.example.alexander.yatranslator.db.entities.TranslationItem;
import com.example.alexander.yatranslator.db.tables.ParametersTable;
import com.example.alexander.yatranslator.db.tables.TranslationsTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResolver;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResult;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Alexander on 09.04.2017.
 */
public class TranslationWithParametersDeleteResolver extends DeleteResolver<TranslationItem> {
    @NonNull
    @Override
    public DeleteResult performDelete(@NonNull StorIOSQLite storIOSQLite, @NonNull TranslationItem object) {
        Log.d("[Debug]", "DeleteResolver TranslationItem " + object.getParameters().getId());

        final List<Translation> translations = storIOSQLite
                .get()
                .listOfObjects(Translation.class)
                .withQuery(Query.builder()
                        .table(TranslationsTable.TABLE)
                        .where(TranslationsTable.COLUMN_PARAMETERS_ID + "=?")
                        .whereArgs(object.getParameters().getId())
                        .build())
                .prepare()
                .executeAsBlocking();

        storIOSQLite
                .delete()
                .object(object.getParameters())
                .prepare()
                .executeAsBlocking();

        Log.d("[Debug]", "DeleteResolver Translations " + object.getValues());

        storIOSQLite
                .delete()
                .objects(translations)
                .prepare()
                .executeAsBlocking();


        final Set<String> affectedTables = new HashSet<>(2);

        affectedTables.add(TranslationsTable.TABLE);
        affectedTables.add(ParametersTable.TABLE);

        return DeleteResult.newInstance(translations.size() + 1, affectedTables);
    }
}
