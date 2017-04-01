package com.example.alexander.yatranslater.service;

/**
 * Created by Alexander on 01.04.2017.
 */
public class LanguageInfo {
    private String id;

    private String name;

    public LanguageInfo(String id, String name){
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
