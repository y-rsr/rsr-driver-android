package com.ridesharerental.app;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.ridesharerental.fragments.AdminInbox;
import com.ridesharerental.fragments.DirectAdminInbox;
import com.ridesharerental.fragments.DirectInbox;
import com.ridesharerental.fragments.OwnerInbox;

public class MessageActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    RelativeLayout rl_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        rl_back=(RelativeLayout) findViewById(R.id.rl_back);

        tabLayout.addTab(tabLayout.newTab().setText("Conversation with owner"));
        tabLayout.addTab(tabLayout.newTab().setText("Conversation with Admin"));
        tabLayout.addTab(tabLayout.newTab().setText("Direct Conversation with owner"));
        tabLayout.addTab(tabLayout.newTab().setText("Direct Conversation with  Admin"));
        //tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        MyAdapter adapter = new MyAdapter(this,getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
                    return inbox;
                case 1:
                    AdminInbox Admin = new AdminInbox();
                    return Admin;
                case 2:
                    DirectInbox direct = new DirectInbox();
                    return direct;

                case 3:
                    DirectAdminInbox directAdmin = new DirectAdminInbox();
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
}
