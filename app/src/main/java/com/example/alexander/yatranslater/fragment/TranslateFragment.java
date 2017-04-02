package com.example.alexander.yatranslater.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.alexander.yatranslater.R;
import com.example.alexander.yatranslater.dependency.TranslateComponent;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;

public class TranslateFragment extends Fragment {
    @BindView(R.id.translateText)
    MultiAutoCompleteTextView translateTextView;
    @BindView(R.id.translations)
    ListView translations;
    @BindView(R.id.swapLang)
    ImageButton swapLangButton;

    @Inject
    TranslateComponent component;
    private Unbinder unbinder;

    //ChooseLanguageFragment chooseLanguageFragment = new ChooseLanguageFragment();
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);


        swapLangButton.setOnClickListener(view -> {
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
