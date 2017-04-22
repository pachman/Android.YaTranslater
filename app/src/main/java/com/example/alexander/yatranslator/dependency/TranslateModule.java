package com.example.alexander.yatranslator.dependency;

import com.example.alexander.yatranslator.service.TranslateClient;
import com.example.alexander.yatranslator.service.YandexTranslateClient;
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
    TranslateClient provideTranslateClient(){
        return new YandexTranslateClient();
    }
}

