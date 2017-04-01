package com.example.alexander.yatranslater.dependency;

import com.example.alexander.yatranslater.service.TranslateClient;
import com.example.alexander.yatranslater.service.YandexTranslateClient;
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

