package com.app.organizedsports.activities.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.organizedsports.R;
import com.app.organizedsports.activities.admin.fragment.UserPageAdapter;
import com.google.android.material.tabs.TabLayout;

public class UsersActivity extends AppCompatActivity {
    View mRootView;
    int currentTab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        currentTab = intent.getIntExtra("tab", 0);
        mRootView = findViewById(R.id.root);
        UserPageAdapter usersPageAdapter = new UserPageAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(usersPageAdapter);
        final TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.selectTab(tabs.getTabAt(currentTab));
    }
}