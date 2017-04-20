package com.example.alexander.yatranslator.dependency;

import com.example.alexander.yatranslator.service.TestTranslateClient;
import com.example.alexander.yatranslator.service.TranslateClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alexander on 01.04.2017.
 */
@Module
public class TranslateModule {
    @Provides
    @Singleton
    TranslateClient provideTranslateClient(){
        return new TestTranslateClient();
    }
}

