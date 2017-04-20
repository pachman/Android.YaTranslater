package com.example.alexander.yatranslator.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.annimon.stream.Stream;
import com.example.alexander.yatranslator.R;
import com.example.alexander.yatranslator.TranslationAdapter;
import com.example.alexander.yatranslator.db.entities.TranslationItem;
import com.example.alexander.yatranslator.dependency.TranslateComponent;
import com.example.alexander.yatranslator.service.HistoryService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HistoryFragment extends Fragment {
    @BindView(R.id.historyListView)
    ListView historyListView;
    @BindView(R.id.refreshHistory)
    Button refreshHistory;

    private Unbinder unbinder;
    @Inject
    TranslateComponent component;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.history_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);
refreshHistory.setOnClickListener((view)->{
    Log.d("[Debug]", "HistoryFragment onStart");
    HistoryService historyService = new HistoryService(component.provideStorIOSQLite());
    historyService.getHistory()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(translations -> {
                Log.d("[Debug]", "getHistory subscribe");
                for (TranslationItem trans : translations) {
                    Log.d("id", trans.getParameters().toString());
                    for (String s : trans.getValues()) {
                        Log.d("item = >", s);
                    }
                }

                List<String> strings = Stream.of(translations).map(t -> t.getParameters().getText()).toList();

                ArrayAdapter<TranslationItem> adapter = new TranslationAdapter(getContext(), new ArrayList<TranslationItem>(translations));
                historyListView.setAdapter(adapter);
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
