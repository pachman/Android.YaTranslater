package com.example.alexander.yatranslater.service;

import java.io.IOException;

/**
 * Created by Alexander on 01.04.2017.
 */

public interface TranslateClient{
    TranslatedPhrase Translate(String text, String langFrom, String langTo) throws IOException;
}

