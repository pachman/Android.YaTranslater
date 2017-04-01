package com.example.alexander.yatranslater.service;

import io.reactivex.Observable;

/**
 * Created by Alexander on 01.04.2017.
 */

public interface TranslateClient{
    Observable<TranslatedPhrase> translate(String text, String langFrom, String langTo);

    Observable<SupportLanguages> getLanguages(String uiLang);
}

