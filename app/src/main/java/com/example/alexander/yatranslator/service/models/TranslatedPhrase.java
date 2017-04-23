package com.example.alexander.yatranslator.service.models;

import java.util.List;

public class TranslatedPhrase {

    private String lang;

    private List<String> text = null;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }

}
