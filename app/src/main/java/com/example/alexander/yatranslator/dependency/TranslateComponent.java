package com.example.alexander.yatranslator.dependency;

import android.database.sqlite.SQLiteOpenHelper;
import com.example.alexander.yatranslator.fragment.TranslateFragment;
import com.example.alexander.yatranslator.service.TranslateClient;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
        AppModule.class,
        TranslateModule.class,
        DbModule.class})
public interface TranslateComponent {

    TranslateClient provideTranslateClient();

    StorIOSQLite provideStorIOSQLite();

    SQLiteOpenHelper provideSQLiteOpenHelper();

    void inject(TranslateFragment translateFragment);
}
