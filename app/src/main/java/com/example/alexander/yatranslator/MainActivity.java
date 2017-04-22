package com.example.alexander.yatranslator;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;
import com.example.alexander.yatranslator.dependency.*;
import com.example.alexander.yatranslator.fragment.FavoriteFragment;
import com.example.alexander.yatranslator.fragment.HistoryFragment;
import com.example.alexander.yatranslator.fragment.SelectFragmentImpl;
import com.example.alexander.yatranslator.fragment.TranslateFragment;
import com.example.alexander.yatranslator.service.HistoryService;
import com.example.alexander.yatranslator.ui.SectionsPagerAdapter;
import com.example.alexander.yatranslator.ui.adapter.TranslationAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static TranslateComponent component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        component = DaggerTranslateComponent.builder()
                .appModule(new AppModule(this))
                .translateModule(new TranslateModule())
                .dbModule(new DbModule())
                .build();

        //ButterKnife.bind(this);
        Log.d("[Debug]", "Locale " + Locale.getDefault().getLanguage());



        ViewPager viewPager = (ViewPager) findViewById(R.id.container);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.bottom_tabs);
        createViewPager(viewPager, tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        createTabIcons(tabLayout);
    }

    private void createTabIcons(TabLayout tabLayout) {
        int[] names = new int[]{
                R.string.translation,
                R.string.history,
                R.string.favorite};
        int[] icons = new int[]{
                R.drawable.ic_translate_black_24dp,
                R.drawable.ic_schedule_black_24dp,
                R.drawable.ic_book_black_24dp};

        for (int i = 0; i < names.length; i++) {
            TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tabOne.setText(getString(names[i]));
            tabOne.setCompoundDrawablesWithIntrinsicBounds(0, icons[i], 0, 0);
            tabLayout.getTabAt(i).setCustomView(tabOne);
        }
    }

    private void createViewPager(ViewPager viewPager, TabLayout tabLayout) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());

        appendTranslationFragment(adapter);

        appendHistoryFragment(adapter);

        appendFavoriteFragment(adapter);

        TabLayout.TabLayoutOnPageChangeListener listener = new TabLayout.TabLayoutOnPageChangeListener(tabLayout){
            @Override
            public void onPageSelected(int position) {
                Log.d("[Debug]", "Selected " + position);
                Fragment item = adapter.getItem(position);
                if(item instanceof SelectFragmentImpl){
                    ((SelectFragmentImpl)item).selectFragment();
                }

            }
        };
        viewPager.addOnPageChangeListener(listener);

        viewPager.setAdapter(adapter);
    }

    private void appendFavoriteFragment(SectionsPagerAdapter adapter) {
        FavoriteFragment favoriteFragment = new FavoriteFragment();
        component.inject(favoriteFragment);
        adapter.addFrag(favoriteFragment, getString(R.string.favorite));
    }

    private void appendTranslationFragment(SectionsPagerAdapter adapter) {
        TranslateFragment translateFragment = new TranslateFragment();
        component.inject(translateFragment);
        adapter.addFrag(translateFragment, getString(R.string.translation));
    }

    private void appendHistoryFragment(SectionsPagerAdapter adapter) {
        HistoryFragment historyFragment = new HistoryFragment();
        historyFragment.setSelectedFragmentListener((fragment, layoutId) -> {
            Log.d("[Debug]", "HistoryFragment onStart");
            HistoryService historyService = new HistoryService(component.provideStorIOSQLite());
            historyService.getHistory()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(translations -> {


                        TranslationAdapter translationAdapter = new TranslationAdapter(fragment.getContext(), new ArrayList<>(translations));
                        translationAdapter.setOnDeleteItemListener((v, translationItem) -> {
                            historyService.Delete(translationItem)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(isDeleted -> {
                                        if (isDeleted) {
                                            Log.d("[Debug]", "refresh history");
                                            historyFragment.selectFragment();
                                        }
                                    });
                        });

                        historyFragment.updateHistoryList(translationAdapter);
                    });
        });
        component.inject(historyFragment);
        adapter.addFrag(historyFragment, getString(R.string.history));
    }
}



