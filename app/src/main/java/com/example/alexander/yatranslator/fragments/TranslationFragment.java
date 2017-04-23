package com.example.alexander.yatranslator.fragments;

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
import com.example.alexander.yatranslator.storio.entities.TranslationItem;
import com.example.alexander.yatranslator.storio.entities.TranslationType;
import com.example.alexander.yatranslator.dagger.TranslateComponent;
import com.example.alexander.yatranslator.service.models.SupportLanguages;
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
//    @BindView(R.id.favoriteFromTranslate)
//    ImageView favoriteFromTranslate;

    @Inject
    TranslateComponent component;

    private Unbinder unbinder;

    Map<String, String> languagesNameToShortName = new HashMap<>();
    Map<String, String> languagesShortNameName = new HashMap<>();
    List<String> languagesNames = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.translate_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        swapLangButton.setOnClickListener(view -> {
            int fromPosition = langFromSpinner.getSelectedItemPosition();
            langFromSpinner.setSelection(langToSpinner.getSelectedItemPosition());
            langToSpinner.setSelection(fromPosition);
        });

        fillLanguages();

        translateFab.setOnClickListener(view -> translate());

//        favoriteFromTranslate.setOnClickListener(view -> {
//            boolean isFavorite = translations.isEm;
//            if (isFavorite) {
//                favoriteFromTranslate.setImageResource(R.drawable.ic_bookmark_black_24dp);
//            } else {
//                favoriteFromTranslate.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
//            }
//        });

        return rootView;
    }

    public void setTranslationItem(TranslationItem translationItem){
        translateTextView.setText(translationItem.getParameters().getText());
        fillTranslations(translationItem.getValues());

        String direction = translationItem.getParameters().getDirection();

        //example: ru-en
        String langFrom = direction.substring(0, 2);
        String langTo = direction.substring(3, 5);

        Log.d("[Debug]", "Values " + translationItem.getValues()+" " + langFrom + " "+langTo);
        setSelectedLanguage(langFromSpinner, langFrom);
        setSelectedLanguage(langToSpinner, langTo);
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
                        List<String> strings = translatedPhrase.getText();
                        if (!canInsert(text, strings)) {
                            return;
                        }

                        fillTranslations(strings);

                        String direction = langFrom + "-" + langTo;

                        component.provideTranslationStorage()
                                .insertOrUpdateDate(TranslationType.History, direction, text, strings)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe();
                    });
        }else{
            showMessageInternetNotAvailable();
        }
    }

    private void fillTranslations(List<String> strings) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, strings);
        translations.setAdapter(adapter);
    }

    private void fillLanguages() {
        io.reactivex.Observable<SupportLanguages> languagesObservable;

        if (component.provideNetworkUtils().isNetworkAvailable()) {
            languagesObservable = component.provideTranslateClient().getLanguages(uiLang);
        } else {
            languagesObservable = component.provideTranslateClient().getConstantLanguages();
            showMessageInternetNotAvailable();
        }

        languagesObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(supportLanguages -> setLanguages(supportLanguages.getLangs()));
    }

    public void setLanguages(Map<String, String> languages) {
        languagesNameToShortName.clear();
        languagesShortNameName.clear();
        languagesNames.clear();

        Log.d("[Debug]", "Load languages");
        languagesShortNameName = languages;
        for (Map.Entry<String, String> entry : languagesShortNameName.entrySet()) {
            languagesNames.add(entry.getValue());
            languagesNameToShortName.put(entry.getValue(), entry.getKey());
        }
        Collections.sort(languagesNames);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, languagesNames);

        langFromSpinner.setAdapter(adapter);
        langToSpinner.setAdapter(adapter);

        setSelectedLanguage(langFromSpinner, "ru");
        setSelectedLanguage(langToSpinner, "en");
    }

    private void setSelectedLanguage(Spinner spinner, String lang) {
        spinner.setSelection(languagesNames.indexOf(languagesShortNameName.get(lang)));
    }

    private void showMessageInternetNotAvailable() {
        //todo локализация
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
