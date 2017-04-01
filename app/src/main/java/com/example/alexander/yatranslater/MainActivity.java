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
import com.example.alexander.yatranslater.dependency.DaggerTranslateComponent;
import com.example.alexander.yatranslater.dependency.TranslateComponent;
import com.example.alexander.yatranslater.dependency.TranslateModule;
import com.example.alexander.yatranslater.service.TranslateClient;
import com.example.alexander.yatranslater.service.TranslatedPhrase;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    static TranslateClient translateClient;

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

        TranslateComponent component = DaggerTranslateComponent.builder().translateModule(new TranslateModule()).build();

        translateClient = component.provideTranslateClient();

        //ButterKnife.bind(this);
        //Toast.makeText(this, String.valueOf(vehicle.getSpeed()), Toast.LENGTH_SHORT).show();
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
        private static final String ARG_SECTION_NUMBER = "section_number";

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
                    rootView = getView( R.layout.history_fragment, inflater, container);
                    final TextView textView = (TextView) rootView.findViewById(R.id.history_label);
                    ImageButton imageButton= (ImageButton) rootView.findViewById(R.id.buttonSettings);
                    imageButton.setOnClickListener(view-> new TranslateTask(textView,translateClient).execute());
                    break;
                case 2:
                    rootView = getView( R.layout.favorite_fragment, inflater, container);
                    break;
                default:
                    rootView = getView( R.layout.fragment_main, inflater, container);
                    break;
            }

            return rootView;
        }

        private View getView( int idLayout, LayoutInflater inflater, ViewGroup container) {
            View rootView = inflater.inflate(idLayout, container, false);
            return rootView;
        }

   }

}




class TranslateTask extends AsyncTask<Void, Void, String> {

    private final TranslateClient translateApi;
    private TextView textView;

    public TranslateTask(TextView textView, TranslateClient translateApi){

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
        TranslatedPhrase responseCall;
        try {
            responseCall = translateApi.Translate("Hello World!", "en","ru");
            String text = responseCall.getText().get(0);
            return text;
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

