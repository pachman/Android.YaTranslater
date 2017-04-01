package com.example.alexander.yatranslater;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.alexander.yatranslater.dependency.DaggerTranslateComponent;
import com.example.alexander.yatranslater.dependency.TranslateComponent;
import com.example.alexander.yatranslater.dependency.TranslateModule;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    static TranslateComponent component;

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
        adapter.addFrag(PlaceholderFragment.newInstance(0), getString(R.string.translate));
        adapter.addFrag(PlaceholderFragment.newInstance(1), getString(R.string.history));
        adapter.addFrag(PlaceholderFragment.newInstance(2), getString(R.string.favorite));
        viewPager.setAdapter(adapter);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container,
                                 Bundle savedInstanceState) {
            int position = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView;
            switch (position) {
                case 1:
                    rootView = getHistoryFragment(inflater, container);
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.favorite_fragment, container, false);
                    break;
                default:
                    rootView = inflater.inflate(R.layout.main_fragment, container, false);
                    break;
            }

            return rootView;
        }

        private View getHistoryFragment(LayoutInflater inflater, ViewGroup container) {
            View rootView = inflater.inflate(R.layout.history_fragment, container, false);
            final TextView textView = (TextView) rootView.findViewById(R.id.history_label);
            ImageButton imageButton= (ImageButton) rootView.findViewById(R.id.buttonSettings);
            ListView listView= (ListView) rootView.findViewById(R.id.historyListView);
            Context context = textView.getContext();

            component.provideTranslateClient()
                    .getLanguages("ru")
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(supportLanguages -> {
                        List<String> list = new ArrayList<>();
                        for (Map.Entry<String, String> entry:supportLanguages.getLangs().entrySet()) {
                            list.add(entry.getValue());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list);

                        listView.setAdapter(adapter);
                    });

            imageButton.setOnClickListener(view-> component.provideTranslateClient()
                    .translate("Hello World!", "en","ru")
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(translatedPhrase -> Toast.makeText(context,translatedPhrase.getText().get(0), Toast.LENGTH_SHORT).show()));
            return rootView;
        }
   }

}
