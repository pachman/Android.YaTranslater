package com.example.alexander.yatranslator.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.alexander.yatranslator.R;
import com.example.alexander.yatranslator.db.tables.TranslationType;
import com.example.alexander.yatranslator.ui.adapter.TranslationAdapter;

public class FavoriteFragment extends Fragment implements TranslationListFragment {
    @BindView(R.id.favoriteListView)
    ListView favoriteListView;

    private Unbinder unbinder;

    private SelectedFragmentListener selectedFragmentListener;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("[Debug]", "Open FavoriteFragment");
        View rootView  = inflater.inflate(R.layout.favorite_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        refresh();

        return rootView;
    }

    public void updateList(TranslationAdapter adapter){
        favoriteListView.setAdapter(adapter);
    }

    public void setSelectedFragmentListener(SelectedFragmentListener selectedFragmentListener){
        this.selectedFragmentListener = selectedFragmentListener;
    }

    @Override
    public void refresh() {
        if(selectedFragmentListener != null)
            selectedFragmentListener.onSelectedFragment(this, TranslationType.Favorite);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
