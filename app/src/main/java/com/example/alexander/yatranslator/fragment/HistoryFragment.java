package com.example.alexander.yatranslator.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.annimon.stream.Stream;
import com.example.alexander.yatranslator.R;
import com.example.alexander.yatranslator.db.entities.TranslationItem;
import com.example.alexander.yatranslator.dependency.TranslateComponent;
import com.example.alexander.yatranslator.service.HistoryService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;
import java.util.List;

public class HistoryFragment extends Fragment implements OnSelectedFragment {
    @BindView(R.id.historyListView)
    ListView historyListView;

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

    @Override
    public void onSelected() {
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

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, strings);
                    historyListView.setAdapter(adapter);
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
