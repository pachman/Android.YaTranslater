package com.example.alexander.yatranslator.dependency;

import com.example.alexander.yatranslator.fragment.ChooseLanguageFragment;
import com.example.alexander.yatranslator.fragment.FavoriteFragment;
import com.example.alexander.yatranslator.fragment.HistoryFragment;
import com.example.alexander.yatranslator.fragment.TranslateFragment;
import com.example.alexander.yatranslator.service.TranslateClient;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {TranslateModule.class})
public interface TranslateComponent {

    TranslateClient provideTranslateClient();

    void inject(TranslateFragment translateFragment);

    void inject(HistoryFragment historyFragment);

    void inject(FavoriteFragment favoriteFragment);

    void inject(ChooseLanguageFragment chooseLanguageFragment);
}
