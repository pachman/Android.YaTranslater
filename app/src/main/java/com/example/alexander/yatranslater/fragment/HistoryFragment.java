package com.example.alexander.yatranslater.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.alexander.yatranslater.R;
import com.example.alexander.yatranslater.dependency.TranslateComponent;

import javax.inject.Inject;

public class HistoryFragment extends Fragment {
    @BindView(R.id.buttonSettings) ImageButton imageButton;
    @BindView(R.id.history_label) TextView textView;

    private Unbinder unbinder;
    @Inject
    TranslateComponent component;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.history_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);


        return rootView;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
