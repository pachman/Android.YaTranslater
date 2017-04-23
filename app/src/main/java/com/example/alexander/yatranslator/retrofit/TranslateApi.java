package com.example.alexander.yatranslator.retrofit;

import com.example.alexander.yatranslator.retrofit.models.LanguagesResponse;
import com.example.alexander.yatranslator.retrofit.models.TranslateResponse;
import io.reactivex.Observable;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TranslateApi {
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("tr.json/translate")
    Observable<TranslateResponse> translate(@Query("key") String key, @Query("lang") String lang, @Query("text") String text);

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("tr.json/getLangs")
    Observable<LanguagesResponse> getLangs(@Query("key") String key, @Query("ui") String uiLang);
}


