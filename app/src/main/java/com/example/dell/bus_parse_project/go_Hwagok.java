package com.example.dell.bus_parse_project;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.dell.bus_parse_project.Adapter.TabPagerAdapter;
import com.example.dell.bus_parse_project.Fragment.DataBaseFragment;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.ArrayList;


public class go_Hwagok extends AppCompatActivity {

    private ArrayList<String> busRouteNmList = new ArrayList<>();
    private ArrayList<String> busRouteIdList = new ArrayList<>();
    private ArrayList<String> ordList = new ArrayList<>();

    private String key = "2ysbgQFrENYJHhR8eRUwPTG6A19dAGm%2BqRXK%2FRaKcaC4Ml%2FHvbpa2i7N3dwrkhnkJD2iQbSPNbBLZyVFq5tBBw%3D%3D";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go__home);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());

        String tablename = "Go_Away";

        String arsId, stId;

        SQLiteDatabase database = this.openOrCreateDatabase(tablename, MODE_PRIVATE, null);

        Cursor cursor = database.rawQuery("SELECT * FROM "+ tablename, null);

        if (cursor == null){
            Toast.makeText(getApplicationContext(), "등록되지 않았습니다", Toast.LENGTH_SHORT).show();
        }


        if(cursor != null){
            if(cursor.moveToFirst()){
                do {
                    arsId = cursor.getString(cursor.getColumnIndex("arsId"));
                    stId = cursor.getString(cursor.getColumnIndex("stId"));

                    getRouteId(arsId);
                    getSeq(busRouteIdList.toArray(new String[busRouteIdList.size()]), arsId);

                    tabPagerAdapter.addFragment(new DataBaseFragment().newInstance
                            (stId, busRouteIdList.toArray(new String[busRouteIdList.size()]), ordList.toArray(new String[ordList.size()])), "");


                    busRouteIdList.clear();
                    ordList.clear();

                }while (cursor.moveToNext());
            }
        }

        viewPager.setAdapter(tabPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void getRouteId(String arsId) {
        StrictMode.enableDefaults();
        busRouteNmList.clear();
        busRouteIdList.clear();

        String routeId = null, busNum = null;
        boolean isRouteId = false, isBusNum = false;
        String call_url = "http://ws.bus.go.kr/api/rest/stationinfo/getRouteByStation?ServiceKey=";
        try {
            URL url = new URL(call_url + key + "&arsId=" + arsId);

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(url.openStream(), "utf-8");
            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("busRouteId")) {
                            isRouteId = true;
                        }
                        if (parser.getName().equals("busRouteNm")) {
                            isBusNum = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        if (isRouteId) {
                            routeId = parser.getText();
                            isRouteId = false;
                        }
                        if (isBusNum) {
                            busNum = parser.getText();
                            busNum = busNum.replaceAll("[^0-9,-]", "");
                            isBusNum = false;
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("itemList")) {
                            busRouteIdList.add(routeId);
                            busRouteNmList.add(busNum);
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getSeq(String[] busRouteId, String paramArsId) {
        StrictMode.enableDefaults();

        boolean isSeq = false, isBusRouteNm = false, isArsId = false;

        String seq = null, arsId = null, busRouteNm = null;

        String callUrl = "http://ws.bus.go.kr/api/rest/busRouteInfo/getStaionByRoute?ServiceKey=";

        for (int i = 0; i < busRouteId.length; i++) {
            try {
                URL url = new URL(callUrl + key + "&busRouteId=" + busRouteId[i]);

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(url.openStream(), "utf-8");
                int parserEvent = parser.getEventType();

                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                        case XmlPullParser.START_TAG:
                            if (parser.getName().equals("arsId")) {
                                isArsId = true;
                            }
                            if (parser.getName().equals("seq")) {
                                isSeq = true;
                            }
                            if (parser.getName().equals("busRouteNm")) {
                                isBusRouteNm = true;
                            }
                            break;

                        case XmlPullParser.TEXT:
                            if (isArsId) {
                                arsId = parser.getText();
                                isArsId = false;
                            }

                            if (isSeq) {
                                seq = parser.getText();
                                isSeq = false;
                            }
                            if (isBusRouteNm) {
                                busRouteNm = parser.getText();
                                isBusRouteNm = false;
                            }
                            break;

                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("itemList")) {
                                if (arsId.equals(paramArsId)) {
                                    ordList.add(seq);
                                    busRouteNmList.add(busRouteNm);
                                }
                            }
                            break;
                    }
                    parserEvent = parser.next();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}