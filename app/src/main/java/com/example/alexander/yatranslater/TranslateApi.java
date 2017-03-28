package com.example.alexander.yatranslater;

import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TranslateApi {
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("tr.json/translate")
    Call<DataResponse> getWrap(@Query("lang") String lang, @Query("key") String key, @Query("text") String text);
}


