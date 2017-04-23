package com.example.alexander.yatranslator.dependency;

import android.content.Context;
import android.support.annotation.NonNull;
import com.example.alexander.yatranslator.utils.NetworkUtils;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class UtilsModule {

    @Provides
    @NonNull
    @Singleton
    public NetworkUtils provideNetworkUtils(@NonNull Context context) {
        return new NetworkUtils(context);
    }
}
