package com.ridesharerental.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ridesharerental.app.R;


public class HomeMessage extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    MyAdapter adapter;
    private ActionBar actionBar;
    private int currentScreen = 0;

    public HomeMessage() {
        // Required empty public constructor
    }


//    public static HomeMessage newInstance(String param1, String param2) {
//        HomeMessage fragment = new HomeMessage();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AppCompatActivity actionBarActivity = (AppCompatActivity) getActivity();
        actionBar = actionBarActivity.getSupportActionBar();
        actionBar.show();
        View view = inflater.inflate(R.layout.fragment_home_message, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        tabLayout=(TabLayout)view.findViewById(R.id.tabLayout);
        viewPager=(ViewPager)view.findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("Conversation with owner"));
        tabLayout.addTab(tabLayout.newTab().setText("Conversation with Admin"));
        tabLayout.addTab(tabLayout.newTab().setText("Direct Conversation with owner"));
        tabLayout.addTab(tabLayout.newTab().setText("Direct Conversation with  Admin"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        adapter = new MyAdapter(getActivity(),getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        //viewPager.setCurrentItem(currentScreen);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                currentScreen = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    public Fragment getFragment(int pos) {
        return adapter.getItem(pos);
    }
    class MyAdapter extends FragmentPagerAdapter {

        private Context myContext;
        int totalTabs;

        public MyAdapter(Context context, FragmentManager fm, int totalTabs) {
            super(fm);
            myContext = context;
            this.totalTabs = totalTabs;
        }

        // this is for fragment tabs
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    OwnerInbox inbox = new OwnerInbox();
                    currentScreen=0;
                    return inbox;
                case 1:
                    AdminInbox Admin = new AdminInbox();
                    currentScreen=1;
                    return Admin;
                case 2:
                    DirectInbox direct = new DirectInbox();
                    currentScreen=2;
                    return direct;

                case 3:
                    DirectAdminInbox directAdmin = new DirectAdminInbox();
                    currentScreen=3;
                    return directAdmin;
                default:
                    return null;
            }
        }
        // this counts total number of tabs
        @Override
        public int getCount() {
            return totalTabs;
        }
    }


    public void onResume()
    {
        super.onResume();
        viewPager.setCurrentItem(currentScreen);

    }

}
