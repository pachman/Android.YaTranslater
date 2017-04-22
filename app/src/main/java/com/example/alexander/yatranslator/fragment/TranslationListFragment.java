package com.example.alexander.yatranslator.fragment;

import com.example.alexander.yatranslator.ui.adapter.TranslationAdapter;

public interface TranslationListFragment {
    void setSelectedFragmentListener(SelectedFragmentListener selectedFragmentListener);
    void updateList(TranslationAdapter adapter);
    void refresh();
}
