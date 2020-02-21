package com.ridesharerental.app;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ridesharerental.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user65 on 12/13/2017.
 */

public class My_Reservation_New extends Fragment
{
    private ActionBar actionBar;
    Context context;
    ViewPager viewPager;
    TabLayout tabs;
    My_Reservations active_reservation=new My_Reservations();
    My_Reservation_Past past_reservation=new My_Reservation_Past();
    ViewPagerAdapter adapter;
   // static  SwipeRefreshLayout swipeRefreshLayout;
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
        View rootView = inflater.inflate(R.layout.my_reservation_new, container, false);
        ViewCompat.setElevation(rootView, 50);
        actionBar = actionBarActivity.getSupportActionBar();
        actionBar.show();
        context = getActivity();
        //active_reservation=new My_Reservations();
        ///past_reservation=new My_Reservation_Past();
        init(rootView);
        /*getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                setupViewPager(viewPager);
                tabs.setupWithViewPager(viewPager);
            }
        });
*/
        /*
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                setupViewPager(viewPager);
                tabs.setupWithViewPager(viewPager);

            }
        });*/




        /*tabs.post(new Runnable() {
            @Override
            public void run() {
                setupViewPager(viewPager);
                tabs.setupWithViewPager(viewPager);
            }
        });*/



        setupViewPager(viewPager);
        tabs.setupWithViewPager(viewPager);
        tabs.setOnTabSelectedListener(onTabSelectedListener(viewPager));









        return rootView;


    }

    public void init(View rootView)
    {
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        tabs = (TabLayout) rootView.findViewById(R.id.result_tabs);
       // swipeRefreshLayout=(SwipeRefreshLayout)rootView.findViewById(R.id.activity_main_swipe_refresh_layout);
    }


    // Add Fragments to Tabs
   /* private void setupViewPager(ViewPager viewPager)
    {
         adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(active_reservation, "Active Reservation");
        adapter.addFragment(past_reservation, "Past Reservation");
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }*/


    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title)
        {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
           // swipeRefreshLayout.setRefreshing(false);
            return mFragmentTitleList.get(position);
        }
    }


    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new My_Reservations(), "  Active Reservation");
        adapter.addFragment(new My_Reservation_Past(), "  Past Reservation");
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        onTabSelectedListener(viewPager);
    }



    private TabLayout.OnTabSelectedListener onTabSelectedListener(final ViewPager pager) {
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







    /*class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager)
        {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }*/



}
