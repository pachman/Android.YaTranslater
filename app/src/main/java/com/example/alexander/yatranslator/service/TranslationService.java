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


public class TranslationService {
    StorIOSQLite storIOSQLite;

    public TranslationService(StorIOSQLite storIOSQLite) {
        this.storIOSQLite = storIOSQLite;
    }

    public Observable<Boolean> deleteFavorite(TranslationItem translationItem) {
        return Observable.defer(() -> {
            TranslationParameters parameters = translationItem.getParameters();
            TranslationParameters translationFavorite = storIOSQLite.get()
                    .object(TranslationParameters.class)
                    .withQuery(
                            Query.builder()
                                    .table(ParametersTable.TABLE)
                                    .where(ParametersTable.COLUMN_TYPE + "=? AND " +
                                            ParametersTable.COLUMN_TEXT + "=? AND " +
                                            ParametersTable.COLUMN_DIRECTION + "=?")
                                    .whereArgs(TranslationType.Favorite, parameters.getText(), parameters.getDirection())
                                    .build())
                    .prepare()
                    .executeAsBlocking();

            if (translationFavorite != null) {
                boolean item = storIOSQLite.delete()
                        .object(translationFavorite)
                        .prepare()
                        .executeAsBlocking().numberOfRowsDeleted() > 0;

                return Observable.just(item);
            }
            return Observable.just(false);
        });
    }

    public Observable<Boolean> delete(TranslationItem translationItem) {
        Log.d("[Debug]", "TranslationService -> delete -> translationItem " + translationItem.getParameters().getId());

        return Observable.defer(() -> Observable.just(
                storIOSQLite.delete()
                        .object(translationItem)
                        .prepare()
                        .executeAsBlocking().numberOfRowsDeleted() > 0));
    }

    public Observable<List<TranslationItem>> getTranslationItems(Integer type) {
        Log.d("[Debug]", "getTranslationItems by type -> " + type);

        return Observable.defer(() -> Observable.just(
                storIOSQLite.get()
                        .listOfObjects(TranslationItem.class)
                        .withQuery(ParametersTable.getQueryByType(type))
                        .prepare()
                        .executeAsBlocking()));
    }

    public Observable insertOrUpdate(Integer type, String direction, String text, List<String> translations) {
        Log.d("[Debug]", "TranslationService -> insertOrUpdate by type " + type);

        return Observable.defer(() -> {
            List<TranslationParameters> translationParametersList = storIOSQLite.get()
                    .listOfObjects(TranslationParameters.class)
                    .withQuery(
                            Query.builder()
                                    .table(ParametersTable.TABLE)
                                    .where(ParametersTable.COLUMN_TYPE + "=? AND " +
                                            ParametersTable.COLUMN_TEXT + "=? AND " +
                                            ParametersTable.COLUMN_DIRECTION + "=?")
                                    .whereArgs(type, text, direction)
                                    .build())
                    .prepare()
                    .executeAsBlocking();

            PutResult putResult;
            if (translationParametersList.size() > 0) {
                TranslationParameters translationParameters = translationParametersList.get(0);
                Log.d("[Debug]", "TranslationService -> Update " + translationParameters.getId());
                translationParameters.refreshOrder();

                putResult = storIOSQLite.put()
                        .object(translationParameters)
                        .prepare()
                        .executeAsBlocking();

            } else {
                Log.d("[Debug]", "TranslationService -> Insert ");

                TranslationParameters parameters = new TranslationParameters(null, type, direction, text);
                TranslationItem withParameters = new TranslationItem(parameters, translations, type == TranslationType.Favorite);

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
