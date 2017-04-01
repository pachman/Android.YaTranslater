package com.example.alexander.yatranslater.gateway;

import com.example.alexander.yatranslater.gateway.dto.DataResponse;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TranslateApi {
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("tr.json/translate")
    Call<DataResponse> getWrap(@Query("key") String key, @Query("lang") String lang, @Query("text") String text);
}


