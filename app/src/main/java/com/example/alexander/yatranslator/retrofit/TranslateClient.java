package com.example.alexander.yatranslator.retrofit;

import com.example.alexander.yatranslator.models.SupportLanguages;
import com.example.alexander.yatranslator.models.TranslatedPhrase;
import io.reactivex.Observable;

/**
 * Created by Alexander on 01.04.2017.
 */

public interface TranslateClient{
    Observable<TranslatedPhrase> translate(String text, String direction);

    Observable<SupportLanguages> getLanguages(String uiLang);

    SupportLanguages getConstantLanguages();
}

