package com.example.alexander.yatranslater;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TranslateApi {
    @GET("get")
    Call<DataResponse> getWrap();
}
