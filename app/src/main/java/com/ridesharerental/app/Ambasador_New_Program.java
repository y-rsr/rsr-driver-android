package com.ridesharerental.app;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ridesharerental.adapter.ViewPagerAdapter;
import com.ridesharerental.ambasador.Ambasador_Lates;
import com.ridesharerental.ambasador.Ambasador_Payout;
import com.ridesharerental.ambasador.Ambasador_Transaction;
import com.ridesharerental.ambasador.Ambasador_program;

/**
 * Created by user65 on 2/6/2018.
 */

public class Ambasador_New_Program extends Fragment
{
    private ActionBar actionBar;
    Context context;
    ViewPager viewPager;
    TabLayout tabs;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        AppCompatActivity actionBarActivity = (AppCompatActivity) getActivity();
        View rootView = inflater.inflate(R.layout.ambasador_program_new, container, false);
        ViewCompat.setElevation(rootView, 50);
        actionBar = actionBarActivity.getSupportActionBar();
        actionBar.show();
        context = getActivity();
        init(rootView);
        setupViewPager(viewPager);
        tabs.setupWithViewPager(viewPager);
        tabs.setOnTabSelectedListener(onTabSelectedListener(viewPager));
        return rootView;
    }


    public void init(View rootView)
    {
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        tabs = (TabLayout) rootView.findViewById(R.id.result_tabs);
    }


    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new Ambasador_program(), "Info");
        adapter.addFragment(new Ambasador_Transaction(), "Transaction");
        adapter.addFragment(new Rank(), "Rank");
        adapter.addFragment(new Bonus(), "Bonus");
        adapter.addFragment(new Ambasador_Payout(), "Payout");
       // adapter.addFragment(new Genomic_tree_New(), "Genomic Tree");
        adapter.addFragment(new Ambasador_Lates(), "Genomic Tree");
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        onTabSelectedListener(viewPager);
    }

    private TabLayout.OnTabSelectedListener onTabSelectedListener(final ViewPager pager)
    {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
    }

}
