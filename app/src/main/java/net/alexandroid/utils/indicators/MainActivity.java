package net.alexandroid.utils.indicators;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import net.alexandroid.shpref.MyLog;

// TODO 1. Enhance circle movement
// TODO 2. Remove logs from library

public class MainActivity extends AppCompatActivity {

    private IndicatorsView mIndicatorsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);

        setFab();

        mIndicatorsView = findViewById(R.id.indicatorsView);
        mIndicatorsView.setViewPager(viewPager);
        mIndicatorsView.setSmoothTransition(true);
        mIndicatorsView.setIndicatorsClickChangePage(true);
        mIndicatorsView.setIndicatorsClickListener(new IndicatorsView.OnIndicatorClickListener() {
            @Override
            public void onClick(int indicatorNumber) {
                MyLog.d("Click on: "+ indicatorNumber);
            }
        });

        // usable when viewpager is not attached
        //mIndicatorsView.setSelectedIndicator(2);
    }

    private void setFab() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIndicatorsView.setSelectedIndicator(2);

            }
        });
    }


}
