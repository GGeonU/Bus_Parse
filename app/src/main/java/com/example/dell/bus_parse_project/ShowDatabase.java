package com.example.dell.bus_parse_project;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.session.PlaybackState;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.dell.bus_parse_project.Adapter.TabPagerAdapter;
import com.example.dell.bus_parse_project.Fragment.DataBaseFragment;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.ArrayList;

public class ShowDatabase extends AppCompatActivity {

    private ArrayList<String> busRouteNmList = new ArrayList<>();
    private ArrayList<String> busRouteIdList = new ArrayList<>();
    private String key = "2ysbgQFrENYJHhR8eRUwPTG6A19dAGm%2BqRXK%2FRaKcaC4Ml%2FHvbpa2i7N3dwrkhnkJD2iQbSPNbBLZyVFq5tBBw%3D%3D";

    private ArrayList<String> ordList = new ArrayList<>();

    TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_database);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        String tablename = "aaa";

        SQLiteDatabase database = this.openOrCreateDatabase(tablename, MODE_PRIVATE, null);

        viewPager.setAdapter(tabPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        database.close();

    }



}
