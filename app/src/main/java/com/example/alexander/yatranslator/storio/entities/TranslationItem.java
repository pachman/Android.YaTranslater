package com.example.alexander.yatranslator.storio.entities;

import java.util.List;

public class TranslationItem {
    List<String> values;
    TranslationParameters parameters;

    public TranslationItem(TranslationParameters parameters) {
        this.parameters = parameters;
        values = null;
    }

    public TranslationItem(TranslationParameters parameters, List<String> translations, Boolean isFavorite) {
        this.values = translations;
        this.parameters = parameters;
        setIsFavorite(isFavorite);
    }

    public List<String> getValues() {
        return values;
    }

    public TranslationParameters getParameters() {
        return parameters;
    }

    public Boolean getIsFavorite() {
        return parameters.getIsFavorite();
    }

    public void setIsFavorite(Boolean favorite) {
        parameters.setIsFavorite(favorite);
    }
}
