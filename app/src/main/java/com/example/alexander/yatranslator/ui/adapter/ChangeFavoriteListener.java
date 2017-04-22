package com.example.alexander.yatranslator.ui.adapter;

import android.view.View;
import com.example.alexander.yatranslator.db.entities.TranslationItem;

public interface ChangeFavoriteListener
{
    void onChangeFavorite(View view, TranslationItem translationItem);
}
