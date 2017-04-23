package com.example.alexander.yatranslator.storio.entities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.example.alexander.yatranslator.storio.tables.ParametersTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

import java.util.Date;

@StorIOSQLiteType(table = ParametersTable.TABLE)
public class TranslationParameters {
    @Nullable
    @StorIOSQLiteColumn(name = ParametersTable.COLUMN_ID, key = true)
    Long id;
    @NonNull
    @StorIOSQLiteColumn(name = ParametersTable.COLUMN_ORDER)
    Long order;
    @NonNull
    @StorIOSQLiteColumn(name = ParametersTable.COLUMN_TYPE)
    Integer type;
    @NonNull
    @StorIOSQLiteColumn(name = ParametersTable.COLUMN_IS_FAVORITE)
    Boolean isFavorite;
    @NonNull
    @StorIOSQLiteColumn(name = ParametersTable.COLUMN_DIRECTION)
    String direction;
    @NonNull
    @StorIOSQLiteColumn(name = ParametersTable.COLUMN_TEXT)
    String text;

    public TranslationParameters() {
    }

    public TranslationParameters(Long id,@NonNull Integer type, @NonNull String direction, @NonNull String text) {
        this.id = id;
        this.type = type;
        this.direction = direction;
        this.text = text;
        refreshOrder();
    }

    @Nullable
    public Long getId() {
        return id;
    }

    @NonNull
    public String getDirection() {
        return direction;
    }

    @NonNull
    public String getText() {
        return text;
    }

    @NonNull
    public Long getOrder() {
        return order;
    }

    @NonNull
    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(@NonNull Boolean favorite) {
        isFavorite = favorite;
    }

    public void refreshOrder() {
        this.order = new Date().getTime();
    }

    @NonNull
    public Integer getType() {
        return type;
    }

    @Override
    public String toString() {
        return "TranslationParameters{" +
                "id=" + id +
                ", order=" + order +
                ", type=" + type +
                ", isFavorite=" + isFavorite +
                ", direction='" + direction + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
