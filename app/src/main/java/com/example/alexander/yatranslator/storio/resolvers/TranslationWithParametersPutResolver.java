package com.example.alexander.yatranslator.storio.resolvers;

import android.support.annotation.NonNull;
import android.util.Log;
import com.annimon.stream.Stream;
import com.example.alexander.yatranslator.storio.entities.Translation;
import com.example.alexander.yatranslator.storio.entities.TranslationItem;
import com.example.alexander.yatranslator.storio.tables.ParametersTable;
import com.example.alexander.yatranslator.storio.tables.TranslationsTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResolver;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        List<Translation> translations = Stream.of(object.getValues()).map(s -> new Translation(id, s)).toList();

        PutResults<Translation> putTranslations = storIOSQLite
                .put()
                .objects(translations)
                .prepare()
                .executeAsBlocking();

        final Set<String> affectedTables = new HashSet<>(2);

        affectedTables.add(TranslationsTable.TABLE);
        affectedTables.add(ParametersTable.TABLE);

        int numberOfInserts = putTranslations.numberOfInserts();
        return PutResult.newUpdateResult(numberOfInserts + 1, affectedTables);
    }
}