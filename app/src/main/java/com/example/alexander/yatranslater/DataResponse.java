package com.example.alexander.yatranslater;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alexander on 26.03.2017.
 */
public class DataResponse {
    @SerializedName("origin")
    @Expose
    private String origin;
    @SerializedName("url")
    @Expose
    private String url;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
