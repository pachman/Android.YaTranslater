package com.example.alexander.yatranslator.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.alexander.yatranslator.R;
import com.example.alexander.yatranslator.dependency.TranslateComponent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FavoriteFragment extends Fragment {
    @BindView(R.id.favoriteListView)
    ListView favoriteListView;
    @BindView(R.id.refreshFavorite)
    Button refreshFavorite;

    private Unbinder unbinder;
    @Inject
    TranslateComponent component;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.favorite_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }
}
