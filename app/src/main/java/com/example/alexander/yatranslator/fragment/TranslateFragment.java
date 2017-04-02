package com.example.alexander.yatranslator.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.alexander.yatranslator.R;
import com.example.alexander.yatranslator.dependency.TranslateComponent;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TranslateFragment extends Fragment {
    @BindView(R.id.langFrom)
    Spinner langFrom;
    @BindView(R.id.langTo)
    Spinner langTo;
    @BindView(R.id.translateText)
    MultiAutoCompleteTextView translateTextView;
    @BindView(R.id.translations)
    ListView translations;
    @BindView(R.id.swapLang)
    ImageButton swapLangButton;
    @BindView(R.id.translateFab)
    FloatingActionButton translateFab;

    @Inject
    TranslateComponent component;
    private Unbinder unbinder;

    //ChooseLanguageFragment chooseLanguageFragment = new ChooseLanguageFragment();
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.translate_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        String uiLang = "ru";
        component.provideTranslateClient()
                .getLanguages(uiLang)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(supportLanguages -> {
                    List<String> list = new ArrayList<>();
                    for (Map.Entry<String, String> entry : supportLanguages.getLangs().entrySet()) {
                        list.add(entry.getValue());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list);

                    langFrom.setAdapter(adapter);
                    langTo.setAdapter(adapter);
                });

        translateFab.setOnClickListener(view -> {
            String text = translateTextView.getText().toString();
            String langFrom = "en";
            String langTo = "ru";
            Log.d("text", text);

            component.provideTranslateClient()
                    .translate(text, langFrom, langTo)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(translatedPhrase -> {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, translatedPhrase.getText());
                        translations.setAdapter(adapter);
                    });
        });
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
