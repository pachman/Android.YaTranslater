package com.example.alexander.yatranslator.service;

import android.util.Log;
import com.example.alexander.yatranslator.db.TranslationParameters;
import com.example.alexander.yatranslator.db.entities.TranslationItem;
import com.example.alexander.yatranslator.db.tables.ParametersTable;
import com.example.alexander.yatranslator.db.tables.TranslationType;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.queries.Query;
import io.reactivex.Observable;

import java.util.List;


public class HistoryService {
    StorIOSQLite storIOSQLite;

    public HistoryService(StorIOSQLite storIOSQLite) {
        this.storIOSQLite = storIOSQLite;
    }

    public Observable<Boolean> Delete(TranslationItem translationItem) {
        Log.d("[Debug]", "HistoryService -> Delete -> translationItem " + translationItem.getParameters().getId());

        return Observable.defer(() -> Observable.just(
                storIOSQLite.delete()
                        .object(translationItem)
                        .prepare()
                        .executeAsBlocking().numberOfRowsDeleted() > 0));
    }

    public Observable<List<TranslationItem>> getHistory() {
        Log.d("[Debug]", "getHistory");

        return Observable.defer(() -> Observable.just(
                storIOSQLite.get()
                        .listOfObjects(TranslationItem.class)
                        .withQuery(ParametersTable.QUERY_ALL_HISTORY)
                        .prepare()
                        .executeAsBlocking()));
    }

    public Observable insertOrUpdate(Integer type, String direction, String text, List<String> translations) {
        return Observable.defer(() -> {
            List<TranslationParameters> translationParametersList = storIOSQLite.get()
                    .listOfObjects(TranslationParameters.class)
                    .withQuery(
                        Query.builder()
                            .table(ParametersTable.TABLE)
                            .where(ParametersTable.COLUMN_TYPE + "=? AND " +
                                   ParametersTable.COLUMN_TEXT + "=? AND " +
                                   ParametersTable.COLUMN_DIRECTION + "=?")
                            .whereArgs(TranslationType.History, text, direction)
                            .build())
                    .prepare()
                    .executeAsBlocking();

            PutResult putResult;
            if (translationParametersList.size() > 0) {
                TranslationParameters translationParameters = translationParametersList.get(0);
                Log.d("[Debug]", "HistoryService -> Update " + translationParameters.getId());
                translationParameters.refreshOrder();

                putResult = storIOSQLite.put()
                        .object(translationParameters)
                        .prepare()
                        .executeAsBlocking();

            } else {
                Log.d("[Debug]", "HistoryService -> Insert ");

                TranslationParameters parameters = new TranslationParameters(null, type, direction, text);
                TranslationItem withParameters = new TranslationItem(parameters, translations);

                putResult = storIOSQLite.put()
                        .object(withParameters)
                        .prepare()
                        .executeAsBlocking();
            }

            return Observable.just(putResult);
        });
    }

    private boolean canInsert(String text, List<String> translations) {
        return text == null || text.isEmpty()
                || translations == null
                || translations.isEmpty()
                || translations.get(0) == text;
    }
}
