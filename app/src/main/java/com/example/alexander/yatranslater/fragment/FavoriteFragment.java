package com.example.alexander.yatranslater.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.alexander.yatranslater.R;
import com.example.alexander.yatranslater.dependency.TranslateComponent;

import javax.inject.Inject;

public class FavoriteFragment extends Fragment {
    @Inject
    TranslateComponent component;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.favorite_fragment, container, false);
        return rootView;
    }
}
