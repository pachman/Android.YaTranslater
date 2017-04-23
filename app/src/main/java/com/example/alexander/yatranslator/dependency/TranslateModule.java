package com.example.alexander.yatranslator.dependency;

import android.content.Context;
import android.support.annotation.NonNull;
import com.example.alexander.yatranslator.service.TranslateClient;
import com.example.alexander.yatranslator.service.TranslationStorage;
import com.example.alexander.yatranslator.service.YandexTranslateClient;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Created by Alexander on 01.04.2017.
 */
@Module
public class TranslateModule {
    @Provides
    @Singleton
    TranslateClient provideTranslateClient(@NonNull Context context){
        return new YandexTranslateClient(context);
    }

    @Provides
    @Singleton
    TranslationStorage provideTranslationStorage(@NonNull StorIOSQLite storIOSQLite){
        return new TranslationStorage(storIOSQLite);
    }
}

