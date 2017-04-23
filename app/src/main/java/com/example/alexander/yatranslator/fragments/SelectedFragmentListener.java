package com.example.alexander.yatranslator.fragments;

import android.support.v4.app.Fragment;
import com.example.alexander.yatranslator.storio.entities.TranslationItem;

public interface SelectedFragmentListener{
    void onSelectedFragment(Fragment fragment, int type);
}

