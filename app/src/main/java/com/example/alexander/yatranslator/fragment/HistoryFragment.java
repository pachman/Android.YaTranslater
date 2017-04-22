package com.example.alexander.yatranslator.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.alexander.yatranslator.R;
import com.example.alexander.yatranslator.dependency.TranslateComponent;
import com.example.alexander.yatranslator.ui.adapter.TranslationAdapter;

import javax.inject.Inject;

public class HistoryFragment extends Fragment implements SelectFragmentImpl{
    @BindView(R.id.historyListView)
    ListView historyListView;

    private Unbinder unbinder;
    @Inject
    TranslateComponent component;
    private SelectedFragmentListener selectedFragmentListener;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.history_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        selectFragment();

        return rootView;
    }

    public void updateHistoryList(TranslationAdapter adapter){
        historyListView.setAdapter(adapter);
    }

    public void setSelectedFragmentListener(SelectedFragmentListener selectedFragmentListener){
        this.selectedFragmentListener = selectedFragmentListener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void selectFragment() {
        if(selectedFragmentListener != null)
            selectedFragmentListener.onSelectedFragment(this, R.layout.history_fragment);
    }
}

