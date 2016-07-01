package com.techpalle.karan.personaldictionary;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {


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

        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
    }

    private class SectionPagerAdapter extends FragmentStatePagerAdapter{
        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0: return new MyWordsFragment();
                case 1: return new CrowdWordsFragment();
                case 2: return new QuizFragment();
                default: return new MyWordsFragment();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0: return "My Words";
                case 1: return "Crowd Words";
                case 2: return "Quiz";
                default: return "My Words";
            }
        }

        @Override
        public int getCount() {
            return 3;
        }


    }
}
