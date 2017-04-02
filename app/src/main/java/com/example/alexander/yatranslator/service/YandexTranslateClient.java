package com.example.alexander.yatranslator.service;

import com.example.alexander.yatranslator.gateway.TranslateApi;
import com.example.alexander.yatranslator.service.model.SupportLanguages;
import com.example.alexander.yatranslator.service.model.TranslatedPhrase;
import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class YandexTranslateClient implements TranslateClient {
    private static final String translateApiKey = "trnsl.1.1.20170322T195409Z.2b9c4508c62c6808.a6fc70f9fd22b9b152fadc394a7d2d55ebfd04cf";
    private static final String baseUrl = "https://translate.yandex.net/api/v1.5/";
    private final TranslateApi translateApi;

    public YandexTranslateClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        translateApi = retrofit.create(TranslateApi.class);
    }

    @Override
    public Observable<TranslatedPhrase> translate(String text, String langFrom, String langTo) {
        String lang = String.format("%s-%s", langFrom, langTo);

        return translateApi.translate(translateApiKey, lang, text)
                .map(data -> {
                    TranslatedPhrase translatedPhrase = new TranslatedPhrase();
                    translatedPhrase.setLang(data.getLang());
                    translatedPhrase.setText(data.getText());

                    return translatedPhrase;
                });
    }

    @Override
    public Observable<SupportLanguages> getLanguages(String uiLang) {
        return translateApi.getLangs(translateApiKey, uiLang)
                .map(data -> {
                    SupportLanguages supportLanguages = new SupportLanguages();
                    supportLanguages.setLangs(data.getLangs());
                    supportLanguages.setDirs(data.getDirs());

                    return supportLanguages;
                });
    }
}
