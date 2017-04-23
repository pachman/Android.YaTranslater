package com.example.alexander.yatranslator.service;

import com.example.alexander.yatranslator.storio.entities.TranslationItem;
import com.example.alexander.yatranslator.storio.entities.TranslationParameters;
import com.example.alexander.yatranslator.storio.entities.TranslationType;
import com.example.alexander.yatranslator.storio.tables.ParametersTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import io.reactivex.Observable;

import java.util.List;

public class TranslationStorage {
    StorIOSQLite storIOSQLite;

    public TranslationStorage(StorIOSQLite storIOSQLite) {
        this.storIOSQLite = storIOSQLite;
    }

    public Observable<Boolean> delete(TranslationItem translationItem) {
        return Observable.defer(() -> Observable.just(
                storIOSQLite.delete()
                        .object(translationItem)
                        .prepare()
                        .executeAsBlocking().numberOfRowsDeleted() > 0));
    }

    public Observable<List<TranslationItem>> getTranslationItems(Integer type) {
        return Observable.defer(() -> Observable.just(
                storIOSQLite.get()
                        .listOfObjects(TranslationItem.class)
                        .withQuery(ParametersTable.getQueryByType(type))
                        .prepare()
                        .executeAsBlocking()));
    }

    public Observable insertOrUpdateDate(Integer type, String direction, String text, List<String> translations) {
        return Observable.defer(() -> Observable.just(insertOrUpdate(type, direction, text, translations)));
    }

    public Observable<Boolean> changeFavorite(TranslationItem translationItem) {
        return Observable.defer(() -> {
            TranslationParameters parameters = translationItem.getParameters();
            Integer type = translationItem.getParameters().getType();
            boolean isChange = false;
            if (type == TranslationType.History) {
                if (parameters.getIsFavorite()) {
                    insertOrUpdate(TranslationType.Favorite, translationItem.getParameters().getDirection(), parameters.getText(), translationItem.getValues());
                } else {
                    TranslationParameters translationFavorite = storIOSQLite.get()
                            .object(TranslationParameters.class)
                            .withQuery(ParametersTable.getSimilarByType(TranslationType.Favorite, parameters))
                            .prepare()
                            .executeAsBlocking();

                    if (translationFavorite != null) {
                        delete(translationFavorite);
                    }
                }
                put(parameters);
            } else {
                //если Favorite изменятся, то только удаление.
                TranslationParameters translationHistory = storIOSQLite.get()
                        .object(TranslationParameters.class)
                        .withQuery(ParametersTable.getSimilarByType(TranslationType.History, parameters))
                        .prepare()
                        .executeAsBlocking();

                if (translationHistory != null) {
                    translationHistory.setIsFavorite(false);
                    put(translationHistory);
                }

                delete(parameters);

                isChange = true;
            }

            return Observable.just(isChange);
        });
    }

    private PutResult insertOrUpdate(Integer type, String direction, String text, List<String> translations) {
        //поиск дублей (если элемент существует)
        TranslationParameters translationParameters = storIOSQLite.get()
                .object(TranslationParameters.class)
                .withQuery(ParametersTable.getSimilarByType(type, text, direction))
                .prepare()
                .executeAsBlocking();

        PutResult putResult;
        if (translationParameters != null) {
            translationParameters.refreshOrder();

            putResult = put(translationParameters);
        } else {
            boolean isFavorite = type == TranslationType.Favorite;
            //поиск визбранном добавляемого элемента
            if (type == TranslationType.History) {
                TranslationParameters translationFavorite = storIOSQLite.get()
                        .object(TranslationParameters.class)
                        .withQuery(ParametersTable.getSimilarByType(TranslationType.Favorite, text, direction))
                        .prepare()
                        .executeAsBlocking();
                isFavorite = translationFavorite != null;
            }

            TranslationParameters parameters = new TranslationParameters(null, type, direction, text);
            TranslationItem withParameters = new TranslationItem(parameters, translations, isFavorite);

            putResult = storIOSQLite.put()
                    .object(withParameters)
                    .prepare()
                    .executeAsBlocking();
        }
        return putResult;
    }

    private PutResult put(TranslationParameters parameters) {
        return storIOSQLite.put()
                .object(parameters)
                .prepare()
                .executeAsBlocking();
    }

    private DeleteResult delete(TranslationParameters parameters) {
        return storIOSQLite.delete()
                .object(parameters)
                .prepare()
                .executeAsBlocking();
    }
}
