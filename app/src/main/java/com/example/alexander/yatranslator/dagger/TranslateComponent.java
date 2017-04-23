package com.example.alexander.yatranslator.dagger;

import android.database.sqlite.SQLiteOpenHelper;
import com.example.alexander.yatranslator.fragments.TranslationFragment;
import com.example.alexander.yatranslator.service.TranslateClient;
import com.example.alexander.yatranslator.service.TranslationStorage;
import com.example.alexander.yatranslator.utils.NetworkUtils;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
        AppModule.class,
        TranslateModule.class,
        StorIOModule.class,
        UtilsModule.class})
public interface TranslateComponent {

    TranslateClient provideTranslateClient();

    StorIOSQLite provideStorIOSQLite();

    TranslationStorage provideTranslationStorage();

    SQLiteOpenHelper provideSQLiteOpenHelper();

    NetworkUtils provideNetworkUtils();

    void inject(TranslationFragment translateFragment);
}
