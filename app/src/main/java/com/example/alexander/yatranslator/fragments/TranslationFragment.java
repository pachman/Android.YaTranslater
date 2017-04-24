package com.example.alexander.yatranslator.fragments;

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
import com.example.alexander.yatranslator.fragments.listeners.TranslateListener;
import com.example.alexander.yatranslator.fragments.listeners.TranslationInitListener;
import com.example.alexander.yatranslator.models.SupportLanguages;
import com.example.alexander.yatranslator.storio.entities.TranslationItem;

import java.util.*;

import static com.example.alexander.yatranslator.utils.BaseUtils.canTranslate;
import static com.example.alexander.yatranslator.utils.BaseUtils.showMessage;

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

    private Unbinder unbinder;

    Map<String, String> languagesNameToShortName = new HashMap<>();
    Map<String, String> languagesShortNameToName = new HashMap<>();
    List<String> languagesNames = new ArrayList<>();
    List<String> languageDirections;
    private TranslateListener translateListener;
    private TranslationInitListener translationInitListenet;

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

        translateFab.setOnClickListener(view -> translate());

        if (translationInitListenet != null) {
            translationInitListenet.onTranslateInit();
        }

        return rootView;
    }

    public void setTranslateListener(TranslateListener translateListener) {
        this.translateListener = translateListener;
    }

    public void setTranslationInitListener(TranslationInitListener translationInitListener) {

        this.translationInitListenet = translationInitListener;
    }

    public void setTranslationItem(TranslationItem translationItem) {
        translateTextView.setText(translationItem.getParameters().getText());
        setTranslations(translationItem.getValues());

        String direction = translationItem.getParameters().getDirection();

        //example: ru-en
        String langFrom = direction.substring(0, 2);
        String langTo = direction.substring(3, 5);

        setSelectedLanguage(langFromSpinner, langFrom);
        setSelectedLanguage(langToSpinner, langTo);

        Log.d("[Debug]", "Values " + translationItem.getValues() + " " + langFrom + " " + langTo);
    }

    private void translate() {
        String text = translateTextView.getText().toString();
        String langFrom = languagesNameToShortName.get(langFromSpinner.getSelectedItem().toString());
        String langTo = languagesNameToShortName.get(langToSpinner.getSelectedItem().toString());
        String direction = String.format("%s-%s", langFrom, langTo);

        Log.d("[Debug]", "text = " + text + ", langFrom = " + langFrom + ",langTo = " + langTo);

        if (!canTranslate(text)) {
            return;
        }

        if (languageDirections.indexOf(direction) < 0) {
            showMessage(getView(), R.string.translateNotSupport);
            return;
        }

        if (translateListener != null) {
            translateListener.onTranslate(text, direction);
        }
    }

    public void setTranslations(List<String> strings) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, strings);
        translations.setAdapter(adapter);
    }

    public void setLanguages(SupportLanguages supportLanguages) {
        //заполняем после инициализации контекста

        languagesNameToShortName.clear();
        languagesShortNameToName.clear();
        languagesNames.clear();

        Log.d("[Debug]", "Languages load");

        languagesShortNameToName = supportLanguages.getLangs();
        languageDirections = supportLanguages.getDirs();
        for (Map.Entry<String, String> entry : languagesShortNameToName.entrySet()) {
            languagesNames.add(entry.getValue());
            languagesNameToShortName.put(entry.getValue(), entry.getKey());
        }
        Collections.sort(languagesNames);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, languagesNames);

        langFromSpinner.setAdapter(adapter);
        langToSpinner.setAdapter(adapter);

        setSelectedLanguage(langFromSpinner, uiLang);
        setSelectedLanguage(langToSpinner, "en");

    }

    private void setSelectedLanguage(Spinner spinner, String lang) {
        spinner.setSelection(languagesNames.indexOf(languagesShortNameToName.get(lang)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
