package com.example.alexander.yatranslator.fragments;

import com.example.alexander.yatranslator.ui.adapter.TranslationAdapter;

public interface TranslationListFragment {
    void setSelectedFragmentListener(SelectedFragmentListener selectedFragmentListener);
    void setSelectedListItemListener(SelectedListItemListener selectedListItemListener);
    void updateList(TranslationAdapter adapter);
    int getType();
    void refresh();
}
