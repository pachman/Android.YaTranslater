package com.example.alexander.yatranslator.fragment;

import android.database.sqlite.SQLiteOpenHelper;
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
import com.example.alexander.yatranslator.db.tables.TranslationType;
import com.example.alexander.yatranslator.dependency.TranslateComponent;
import com.example.alexander.yatranslator.service.HistoryService;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;
import java.util.*;

public class TranslateFragment extends Fragment {
    @BindView(R.id.langFrom)
    Spinner langFromSpinner;
    @BindView(R.id.langTo)
    Spinner langToSpinner;
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
    @Inject
    SQLiteOpenHelper sqLiteOpenHelper;
    @Inject
    StorIOSQLite storIOSQLite;

    private Unbinder unbinder;

    Map<String,String> LangsNameToShortName = new HashMap<>();
    Map<String,String> LangsShortNameName;
    List<String> LangsNames = new ArrayList<>();

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
                                 LangsShortNameName = supportLanguages.getLangs();
                    for (Map.Entry<String, String> entry : LangsShortNameName.entrySet()) {
                        LangsNames.add(entry.getValue());
                        String key = entry.getKey();
                        LangsNameToShortName.put(entry.getValue(), key);
                    }
                    Collections.sort(LangsNames);

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, LangsNames);

                    langFromSpinner.setAdapter(adapter);
                    langFromSpinner.setSelection(LangsNames.indexOf(LangsShortNameName.get("ru")));
                    langToSpinner.setAdapter(adapter);
                    langToSpinner.setSelection(LangsNames.indexOf(LangsShortNameName.get("en")));
                });

        swapLangButton.setOnClickListener(view -> {
            int fromPosition = langFromSpinner.getSelectedItemPosition();
            langFromSpinner.setSelection(langToSpinner.getSelectedItemPosition());
            langToSpinner.setSelection(fromPosition);
        });

        translateFab.setOnClickListener(view -> {
            String text = translateTextView.getText().toString();
            String langFrom = LangsNameToShortName.get(langFromSpinner.getSelectedItem().toString());
            String langTo = LangsNameToShortName.get(langToSpinner.getSelectedItem().toString());
            Log.d("text", text);
            Log.d("langFrom", langFrom);
            Log.d("langTo", langTo);

            component.provideTranslateClient()
                    .translate(text, langFrom, langTo)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(translatedPhrase -> {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, translatedPhrase.getText());
                        translations.setAdapter(adapter);


                        String direction = langFrom + "-" + langTo;

                        HistoryService historyService = new HistoryService(storIOSQLite);

                        historyService.putTranslateItem(TranslationType.History,direction, text, translatedPhrase.getText())
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe();
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
