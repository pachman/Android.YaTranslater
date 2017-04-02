package com.example.alexander.yatranslater.dependency;

import com.example.alexander.yatranslater.fragment.ChooseLanguageFragment;
import com.example.alexander.yatranslater.fragment.FavoriteFragment;
import com.example.alexander.yatranslater.fragment.HistoryFragment;
import com.example.alexander.yatranslater.fragment.TranslateFragment;
import com.example.alexander.yatranslater.service.TranslateClient;
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
