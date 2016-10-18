package com.techpalle.karan.personaldictionary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.techpalle.karan.personaldictionary.R;
import com.techpalle.karan.personaldictionary.ui.mywords.MyWordsFragment;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE_NEW_WORD = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeScreen();
    }

    private void initializeScreen() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Personal Dictionary");

        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        /*if(requestCode == REQ_CODE_NEW_WORD){
            if(resultCode == RESULT_OK){
                String newWord = data.getExtras().getString(Constants.KEY_BUNDLE_NEW_WORD);
                //Snackbar.make(floatingActionButton, newWord+" has been added to your dictionary." , Snackbar.LENGTH_LONG).show();
            } else if(resultCode == RESULT_CANCELED){
                //Snackbar.make(floatingActionButton, R.string.snack_no_new_words_added, Snackbar.LENGTH_LONG).show();
            }
        }*/
    }

    private class SectionPagerAdapter extends FragmentStatePagerAdapter{
        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0: return new MyWordsFragment();
                case 1: return new MyLanguagesFragment();
                case 2: return new QuizFragment();
                case 3: return new CrowdWordsFragment();
                default: return new MyWordsFragment();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0: return "My Words";
                case 1: return "My Languages";
                case 2: return "Quiz";
                case 3: return "Crowd Words";
                default: return "My Words";
            }
        }

        @Override
        public int getCount() {
            return 4;
        }


    }
}
