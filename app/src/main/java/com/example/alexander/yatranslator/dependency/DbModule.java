package com.example.alexander.yatranslator.dependency;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import com.example.alexander.yatranslator.db.*;
import com.example.alexander.yatranslator.db.entities.TranslationItem;
import com.example.alexander.yatranslator.db.resolvers.TranslationWithParametersDeleteResolver;
import com.example.alexander.yatranslator.db.resolvers.TranslationWithParametersGetResolver;
import com.example.alexander.yatranslator.db.resolvers.TranslationWithParametersPutResolver;
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Created by Alexander on 09.04.2017.
 */
@Module
public class DbModule {

    // We suggest to keep one instance of StorIO (SQLite or ContentResolver)
    // It's thread safe and so on, so just share it.
    // But if you need you can have multiple instances of StorIO
    // (SQLite or ContentResolver) with different settings such as type mapping, logging and so on.
    // But keep in mind that different instances of StorIOSQLite won't share notifications!
    @Provides
    @NonNull
    @Singleton
    public StorIOSQLite provideStorIOSQLite(@NonNull SQLiteOpenHelper sqLiteOpenHelper) {
        TranslationParametersSQLiteTypeMapping typeMapping = new TranslationParametersSQLiteTypeMapping();
        return DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(sqLiteOpenHelper)
                .addTypeMapping(Translation.class, new TranslationSQLiteTypeMapping())
                .addTypeMapping(TranslationParameters.class, typeMapping)
                .addTypeMapping(TranslationItem.class, SQLiteTypeMapping.<TranslationItem>builder()
                        .putResolver(new TranslationWithParametersPutResolver())
                        .getResolver(new TranslationWithParametersGetResolver(typeMapping.getResolver()))
                        .deleteResolver(new TranslationWithParametersDeleteResolver())
                        .build())
                .build();
    }

    @Provides
    @NonNull
    @Singleton
    public SQLiteOpenHelper provideSQLiteOpenHelper(@NonNull Context context) {
        return new DbOpenHelper(context);
    }
}
