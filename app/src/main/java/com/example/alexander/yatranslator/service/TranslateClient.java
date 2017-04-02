package com.example.alexander.yatranslator.service;

import com.example.alexander.yatranslator.service.model.SupportLanguages;
import com.example.alexander.yatranslator.service.model.TranslatedPhrase;
import io.reactivex.Observable;

/**
 * Created by Alexander on 01.04.2017.
 */

public interface TranslateClient{
    Observable<TranslatedPhrase> translate(String text, String langFrom, String langTo);

    Observable<SupportLanguages> getLanguages(String uiLang);
}

