package com.example.alexander.yatranslater.dependency;

import com.example.alexander.yatranslater.service.TranslateClient;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {TranslateModule.class})
public interface TranslateComponent {

    TranslateClient provideTranslateClient();
}
