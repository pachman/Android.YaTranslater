package com.example.alexander.yatranslator;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;
import com.example.alexander.yatranslator.dagger.*;
import com.example.alexander.yatranslator.fragments.FavoriteFragment;
import com.example.alexander.yatranslator.fragments.HistoryFragment;
import com.example.alexander.yatranslator.fragments.TranslationFragment;
import com.example.alexander.yatranslator.fragments.TranslationListFragment;
import com.example.alexander.yatranslator.service.TranslationStorage;
import com.example.alexander.yatranslator.storio.entities.TranslationItem;
import com.example.alexander.yatranslator.storio.entities.TranslationType;
import com.example.alexander.yatranslator.ui.SectionsPagerAdapter;
import com.example.alexander.yatranslator.ui.adapter.TranslationAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    String uiLang = Locale.getDefault().getLanguage();
    private static TranslateComponent component;
    private static TranslationStorage translationService;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        component = DaggerTranslateComponent.builder()
                .appModule(new AppModule(this))
                .translateModule(new TranslateModule())
                .utilsModule(new UtilsModule())
                .storIOModule(new StorIOModule())
                .build();

        //todo перенести в DI
        translationService = new TranslationStorage(component.provideStorIOSQLite());

        //ButterKnife.bind(this);

        viewPager = (ViewPager) findViewById(R.id.container);

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

        TabLayout.TabLayoutOnPageChangeListener listener = new TabLayout.TabLayoutOnPageChangeListener(tabLayout) {
            @Override
            public void onPageSelected(int position) {
                Log.d("[Debug]", "Selected " + position);
                Fragment item = adapter.getItem(position);
                if (item instanceof TranslationListFragment) {
                    ((TranslationListFragment) item).refresh();
                }
            }
        };

        viewPager.addOnPageChangeListener(listener);
        viewPager.setAdapter(adapter);
    }

    private void appendFavoriteFragment(SectionsPagerAdapter adapter) {
        FavoriteFragment favoriteFragment = new FavoriteFragment();
        adapter.addFrag(favoriteFragment, getString(R.string.favorite));

        initTranslationListFragment(favoriteFragment, adapter);
    }

    private void appendTranslationFragment(SectionsPagerAdapter adapter) {
        TranslationFragment translateFragment = new TranslationFragment();
        component.inject(translateFragment);
        adapter.addFrag(translateFragment, getString(R.string.translation));
    }

    private void appendHistoryFragment(SectionsPagerAdapter adapter) {
        HistoryFragment historyFragment = new HistoryFragment();
        adapter.addFrag(historyFragment, getString(R.string.history));

        initTranslationListFragment(historyFragment, adapter);
    }

    private void initTranslationListFragment(TranslationListFragment fragment, SectionsPagerAdapter adapter) {
        int translateIndex = 0;
        fragment.setSelectedFragmentListener((f, type) -> {
            Log.d("[Debug]", "Fragment selected -> " + type);

            fragment.setSelectedListItemListener(translationItem -> {
                viewPager.setCurrentItem(translateIndex);
                Fragment translationFragment = adapter.getItem(translateIndex);
                if (translationFragment instanceof TranslationFragment) {
                    ((TranslationFragment) translationFragment).setTranslationItem(translationItem);
                }
            });

            translationService.getTranslationItems(type)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(translations -> {
                        TranslationAdapter translationAdapter = new TranslationAdapter(f.getContext(), new ArrayList<>(translations));
                        translationAdapter.setOnChangeFavoriteListener((v, translationItem) -> changeFavorite(fragment, translationItem));
                        translationAdapter.setOnDeleteItemListener((v, translationItem) -> {
                            if (fragment.getType() == TranslationType.Favorite) {
                                changeFavorite(fragment, translationItem);
                            } else {
                                deleteItem(fragment, translationItem);
                            }
                        });

                        fragment.updateList(translationAdapter);
                    });
        });
    }

    private Disposable changeFavorite(TranslationListFragment fragment, TranslationItem translationItem) {
        return translationService.changeFavorite(translationItem)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(changed -> {
                    if (changed) fragment.refresh();
                });
    }

    private void deleteItem(TranslationListFragment fragment, TranslationItem translationItem) {
        translationService.delete(translationItem)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isDeleted -> {
                    if (isDeleted) fragment.refresh();
                });
    }
}



