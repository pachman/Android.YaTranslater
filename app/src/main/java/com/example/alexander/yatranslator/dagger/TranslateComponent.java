package com.example.alexander.yatranslator.dagger;

import android.database.sqlite.SQLiteOpenHelper;
import com.example.alexander.yatranslator.retrofit.TranslateClient;
import com.example.alexander.yatranslator.storio.TranslationStorage;
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
}
