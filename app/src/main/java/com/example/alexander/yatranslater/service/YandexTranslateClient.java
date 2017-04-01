package com.example.alexander.yatranslater.service;

import com.example.alexander.yatranslater.gateway.TranslateApi;
import com.example.alexander.yatranslater.gateway.dto.DataResponse;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class YandexTranslateClient implements TranslateClient {
    private static final String translateApiKey = "trnsl.1.1.20170322T195409Z.2b9c4508c62c6808.a6fc70f9fd22b9b152fadc394a7d2d55ebfd04cf";
    private static final String baseUrl = "https://translate.yandex.net/api/v1.5/";
    private final TranslateApi translateApi;

    public YandexTranslateClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        translateApi = retrofit.create(TranslateApi.class);
    }

    @Override
    public TranslatedPhrase Translate(String text, String langFrom, String langTo) throws IOException {
        String lang = String.format("%s-%s",langFrom, langTo);

        DataResponse body = translateApi.getWrap(translateApiKey, lang, text).execute().body();

        TranslatedPhrase translatedPhrase = new TranslatedPhrase();
        translatedPhrase.setLang(body.getLang());
        translatedPhrase.setText(body.getText());

        return translatedPhrase;
    }
}
