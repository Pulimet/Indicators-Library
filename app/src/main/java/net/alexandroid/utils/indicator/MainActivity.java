package net.alexandroid.utils.indicator;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import net.alexandroid.shpref.MyLog;
import net.alexandroid.utils.indicators.IndicatorsView;

public class MainActivity extends AppCompatActivity implements PlaceholderFragment.CategoryFragmentInteractionListener {

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
                MyLog.d("Click on: " + indicatorNumber);
            }
        });

        //mIndicatorsView.setBackgroundColor();

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


    //CategoryFragmentInteractionListener
    @Override
    public void onSetDrawableToSelectedIndicator() {
        mIndicatorsView.setSelectedDrawable(getResources().getDrawable(R.drawable.custom_selected2));
    }

    @Override
    public void onSetDrawableToUnSelectedIndicator() {
        mIndicatorsView.setUnSelectedDrawable(getResources().getDrawable(R.drawable.custom_unselected2));
    }

    @Override
    public void onSetBackgroundColor() {
        mIndicatorsView.setBackgroundColor(Color.YELLOW);
    }
}
