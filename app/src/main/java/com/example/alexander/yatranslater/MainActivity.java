package com.example.alexander.yatranslater;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.TextView;
import com.example.alexander.yatranslater.dependency.DaggerApp;
import com.example.alexander.yatranslater.dependency.DaggerTranslateComponent;
import com.example.alexander.yatranslater.dependency.TranslateComponent;
import com.example.alexander.yatranslater.dependency.TranslateModule;
import com.example.alexander.yatranslater.fragment.FavoriteFragment;
import com.example.alexander.yatranslater.fragment.HistoryFragment;
import com.example.alexander.yatranslater.fragment.TranslateFragment;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private static TranslateComponent component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        component = DaggerTranslateComponent.builder().translateModule(new TranslateModule()).build();


        //ButterKnife.bind(this);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        createViewPager(mViewPager);

        tabLayout = (TabLayout) findViewById(R.id.bottom_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        createTabIcons();
    }

    private void createTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText(getString(R.string.translate));
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_translate_black_24dp, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText(getString(R.string.history));
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_schedule_black_24dp, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText(getString(R.string.favorite));
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_book_black_24dp, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);
    }

    private void createViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());

        TranslateFragment translateFragment = new TranslateFragment();
        component.inject(translateFragment);
        adapter.addFrag(translateFragment, getString(R.string.translate));

        HistoryFragment historyFragment = new HistoryFragment();
        component.inject(historyFragment);
        adapter.addFrag(historyFragment, getString(R.string.history));

        FavoriteFragment favoriteFragment = new FavoriteFragment();
        component.inject(favoriteFragment);
        adapter.addFrag(favoriteFragment, getString(R.string.favorite));

        viewPager.setAdapter(adapter);
    }

    public static TranslateComponent component(Context context){
        return ((DaggerApp) context.getApplicationContext()).component;
    }
}



