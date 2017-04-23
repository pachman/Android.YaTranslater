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

@Module
public class StorIOModule {

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
