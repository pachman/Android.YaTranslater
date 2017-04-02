package com.example.alexander.yatranslater.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.alexander.yatranslater.R;
import com.example.alexander.yatranslater.dependency.TranslateComponent;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Alexander on 02.04.2017.
 */
public class ChooseLanguageFragment extends Fragment {
    @BindView(R.id.historyListView) ListView listView;

    @Inject
    TranslateComponent component;
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.choose_language_fragment, container, false);
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

                    listView.setAdapter(adapter);
                });
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

