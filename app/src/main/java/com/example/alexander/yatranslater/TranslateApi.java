package com.example.alexander.yatranslater;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import retrofit2.Call;
import retrofit2.http.GET;

import javax.inject.Inject;
import javax.inject.Singleton;

public interface TranslateApi {
    @GET("get")
    Call<DataResponse> getWrap();
}


