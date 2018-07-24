package com.listad;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    TabLayout tabs;
    ViewPager pager;
    PagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabs = findViewById(R.id.tabs);
        pager = findViewById(R.id.pager);

        adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ItemListFragment(), "상품목록");
        adapter.addFragment(new ItemListFragment(), "공지사항");
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);

    }
}
