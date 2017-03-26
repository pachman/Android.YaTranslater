package com.example.alexander.yatranslater;

import android.os.AsyncTask;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private Retrofit retrofit;
    private static TranslateApi translateApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        TabLayout tabLayout2 = (TabLayout) findViewById(R.id.bottom_tabs);
        tabLayout2.setupWithViewPager(mViewPager);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://httpbin.org/") //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .build();
        translateApi = retrofit.create(TranslateApi.class);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.history);
                case 1:
                    return getString(R.string.favorite);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container,
                                 Bundle savedInstanceState) {
            int position = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView;
            switch (position) {
                case 1:
                    rootView = inflater.inflate(R.layout.history_fragment, container, false);
                    final TextView textView = (TextView) rootView.findViewById(R.id.history_label);
                    ImageButton imageButton = (ImageButton) rootView.findViewById(R.id.buttonSettings);
                    imageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new TranslateTask(textView,translateApi).execute();
                        }
                    });
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.favorite_fragment, container, false);
                    break;
                default:
                    rootView = inflater.inflate(R.layout.fragment_main, container, false);
                    break;
            }

            return rootView;
        }
    }
}


class TranslateTask extends AsyncTask<Void, Void, String> {

    private TextView textView;
    private TranslateApi translateApi;

    public TranslateTask(TextView textView, TranslateApi translateApi){

        this.textView = textView;
        this.translateApi = translateApi;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        textView.setText("Полез на крышу");


    }

    @Override
    protected String doInBackground(Void... params) {
        Call<DataResponse> responseCall = translateApi.getWrap();
        try {
            String origin = responseCall.execute().body().getOrigin();
            return origin;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        textView.setText("Залез");

        Toast.makeText(textView.getContext(),result, Toast.LENGTH_SHORT).show();
    }
}

