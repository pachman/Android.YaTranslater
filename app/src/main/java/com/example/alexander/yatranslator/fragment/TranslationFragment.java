package com.example.alexander.yatranslator.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.example.alexander.yatranslator.db.tables.TranslationType;
import com.example.alexander.yatranslator.dependency.TranslateComponent;
import com.example.alexander.yatranslator.service.model.SupportLanguages;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;
import java.util.*;

public class TranslationFragment extends Fragment {
    String uiLang = Locale.getDefault().getLanguage();

    @BindView(R.id.langFrom)
    Spinner langFromSpinner;
    @BindView(R.id.langTo)
    Spinner langToSpinner;
    @BindView(R.id.translateText)
    MultiAutoCompleteTextView translateTextView;
    @BindView(R.id.translations)
    ListView translations;
    @BindView(R.id.swapLang)
    ImageView swapLangButton;
    @BindView(R.id.translateFab)
    FloatingActionButton translateFab;
    @BindView(R.id.favoriteFromTranslate)
    ImageView favoriteFromTranslate;

    @Inject
    TranslateComponent component;

    private Unbinder unbinder;

    Map<String, String> languagesNameToShortName = new HashMap<>();
    Map<String, String> languagesShortNameName;
    List<String> languagesNames = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("[Debug]", "Open TranslationFragment");
        View rootView = inflater.inflate(R.layout.translate_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        //hideKeyboard(getActivity(), translateTextView);

        swapLangButton.setOnClickListener(view -> {
            int fromPosition = langFromSpinner.getSelectedItemPosition();
            langFromSpinner.setSelection(langToSpinner.getSelectedItemPosition());
            langToSpinner.setSelection(fromPosition);
        });

        fillLanguages();

        translateFab.setOnClickListener(view -> translate());

        favoriteFromTranslate.setOnClickListener(view -> {
            boolean isFavorite = true;
            if (isFavorite) {
                favoriteFromTranslate.setImageResource(R.drawable.ic_bookmark_black_24dp);
            } else {
                favoriteFromTranslate.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
            }
        });

        return rootView;
    }

    private void translate() {
        String text = translateTextView.getText().toString();
        String langFrom = languagesNameToShortName.get(langFromSpinner.getSelectedItem().toString());
        String langTo = languagesNameToShortName.get(langToSpinner.getSelectedItem().toString());
        Log.d("[Debug]", "text = " + text + ", langFrom = " + langFrom + ",langTo = " + langTo);
        if (!canTranslate(text)) {
            return;
        }

        if (component.provideNetworkUtils().isNetworkAvailable()) {
            component.provideTranslateClient()
                    .translate(text, langFrom, langTo)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(translatedPhrase -> {
                        if (!canInsert(text, translatedPhrase.getText())) {
                            return;
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, translatedPhrase.getText());
                        translations.setAdapter(adapter);

                        String direction = langFrom + "-" + langTo;

                        component.provideTranslationStorage()
                                .insertOrUpdate(TranslationType.History, direction, text, translatedPhrase.getText())
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe();
                    });
        }else{
            showMessageInternetNotAvailable();
        }
    }

    private void fillLanguages() {
        io.reactivex.Observable<SupportLanguages> languagesObservable;

        if (component.provideNetworkUtils().isNetworkAvailable()) {
            languagesObservable = component.provideTranslateClient()
                    .getLanguages(uiLang)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            languagesObservable = component.provideTranslateClient()
                    .getConstantLanguages()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread());

            showMessageInternetNotAvailable();
        }

        languagesObservable.subscribe(supportLanguages -> setLanguages(supportLanguages.getLangs()));
    }

    public void setLanguages(Map<String, String> languages) {
        Log.d("[Debug]", "Load languages");
        languagesShortNameName = languages;
        for (Map.Entry<String, String> entry : languagesShortNameName.entrySet()) {
            languagesNames.add(entry.getValue());
            languagesNameToShortName.put(entry.getValue(), entry.getKey());
        }
        Collections.sort(languagesNames);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, languagesNames);

        langFromSpinner.setAdapter(adapter);
        langFromSpinner.setSelection(languagesNames.indexOf(languagesShortNameName.get("ru")));
        langToSpinner.setAdapter(adapter);
        langToSpinner.setSelection(languagesNames.indexOf(languagesShortNameName.get("en")));
    }

    private void showMessageInternetNotAvailable() {
        //todo локализация
        Log.d("[Debug]", "showMessageInternetNotAvailable");
        View view = getView();
        if(view != null)
            Snackbar.make(view, R.string.InternetNotAvailable, Snackbar.LENGTH_LONG).show();
    }

    private boolean canTranslate(String text) {
        text = text.trim();
        return text != null && !text.isEmpty();
    }

    private boolean canInsert(String text, List<String> translations) {
        return canTranslate(text)
                && translations != null
                && !translations.isEmpty()
                && translations.get(0) != text;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
