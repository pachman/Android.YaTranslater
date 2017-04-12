package com.example.alexander.yatranslator.dependency;

import android.content.Context;
import android.support.annotation.NonNull;
import com.example.alexander.yatranslator.MainActivity;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Created by Alexander on 09.04.2017.
 */
@Module
public class AppModule {

    @NonNull
    private final MainActivity app;

    public AppModule(@NonNull MainActivity app) {
        this.app = app;
    }

    @Provides
    @NonNull
    @Singleton
    Context provideContext() {
        return app;
    }
}