package com.example.alexander.yatranslator.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.alexander.yatranslator.R;
import com.example.alexander.yatranslator.fragments.listeners.SelectedFragmentListener;
import com.example.alexander.yatranslator.fragments.listeners.SelectedListItemListener;
import com.example.alexander.yatranslator.storio.entities.TranslationItem;
import com.example.alexander.yatranslator.storio.entities.TranslationType;
import com.example.alexander.yatranslator.ui.adapter.TranslationAdapter;

public class HistoryFragment extends Fragment implements TranslationListFragment {
    @BindView(R.id.historyListView)
    ListView historyListView;
    @BindView(R.id.historyEmpty)
    TextView historyEmpty;

    private Unbinder unbinder;
    private SelectedFragmentListener selectedFragmentListener;
    private SelectedListItemListener selectedListItemListener;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.history_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        historyListView.setOnItemClickListener((adapterView, view, i, l) -> {
            TranslationItem item = (TranslationItem)adapterView.getItemAtPosition(i);
            if(selectedListItemListener != null)
                selectedListItemListener.onSelectedItem(item);
        });

        refresh();

        return rootView;
    }

    public void updateList(TranslationAdapter adapter){
        int visible = adapter.isEmpty() ? View.VISIBLE : View.INVISIBLE;
        historyEmpty.setVisibility(visible);

        historyListView.setAdapter(adapter);
    }

    @Override
    public int getType() {
        return TranslationType.History;
    }

    public void setSelectedFragmentListener(SelectedFragmentListener selectedFragmentListener){
        this.selectedFragmentListener = selectedFragmentListener;
    }

    @Override
    public void setSelectedListItemListener(SelectedListItemListener selectedListItemListener) {
        this.selectedListItemListener = selectedListItemListener;
    }

    @Override
    public void refresh() {
        if(selectedFragmentListener != null)
            selectedFragmentListener.onSelectedFragment(this, getType());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

