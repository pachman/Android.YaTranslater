package com.example.alexander.yatranslator.db.resolvers;

import android.support.annotation.NonNull;
import android.util.Log;
import com.annimon.stream.Stream;
import com.example.alexander.yatranslator.db.Translation;
import com.example.alexander.yatranslator.db.entities.TranslationItem;
import com.example.alexander.yatranslator.db.tables.ParametersTable;
import com.example.alexander.yatranslator.db.tables.TranslationsTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResolver;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

/**
 * Created by Alexander on 09.04.2017.
 */
public class TranslationWithParametersPutResolver extends PutResolver<TranslationItem>{
    @NonNull
    @Override
    public PutResult performPut(@NonNull StorIOSQLite storIOSQLite, @NonNull TranslationItem object) {
        PutResult putParameters = storIOSQLite
                .put()
                .object(object.getParameters())
                .prepare()
                .executeAsBlocking();

        Long id = putParameters.insertedId();

        Log.d("insertedId", id.toString());

        Object[] translations = Stream.of(object.getValues())
                .map(s -> new Translation(id, s))
                .toArray();

        PutResults<Object> putTranslations = storIOSQLite
                .put()
                .objects(asList(translations))
                .prepare()
                .executeAsBlocking();

        final Set<String> affectedTables = new HashSet<>(2);

        affectedTables.add(TranslationsTable.TABLE);
        affectedTables.add(ParametersTable.TABLE);

        int numberOfInserts = putTranslations.numberOfInserts();
        return PutResult.newUpdateResult(numberOfInserts + 1, affectedTables);
    }
}