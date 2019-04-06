package com.example.dell.bus_parse_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Search_Event extends AppCompatActivity {

    private XmlParser parser = new XmlParser();

    private ArrayList<String> ordList = new ArrayList<>();
    private ArrayList<String> busRouteNmList = new ArrayList<>();
    private ArrayList<String> paramBusRouteId =new ArrayList<>();

    private SwipeRefreshLayout swipe;
    private ImageButton button;
    private ListView listView;
    private TextView stationInfo, setTime;
    private ImageButton star;

    private String[] busRouteId;
    private String stId, Id;

    private String tableName;
    SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__event);

        listView = (ListView) findViewById(R.id.arrived_info);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        button = (ImageButton) findViewById(R.id.refresh);
        stationInfo = (TextView) findViewById(R.id.station_name);
        setTime = (TextView) findViewById(R.id.set_time);
        star = (ImageButton) findViewById(R.id.add);

        Intent intent = getIntent();
        Id = intent.getStringExtra("arsId");
        stId = intent.getStringExtra("stId");
        busRouteId = intent.getStringArrayExtra("busRouteId");

        parser.getSeq(busRouteId, Id, ordList, busRouteNmList, paramBusRouteId);
        for (int i = 0; i < paramBusRouteId.size(); i++) {
            parser.getXml(stId, paramBusRouteId.toArray(new String[paramBusRouteId.size()]), ordList.toArray(new String[ordList.size()]));
        }
        listView.setAdapter(parser.adapter);
        stationInfo.setText(parser.getStationInfo());
        setTime.setText(parser.getSetTime());

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listView.setAdapter(parser.adapter);
                parser.getXml(stId, busRouteId, ordList.toArray(new String[ordList.size()]));
                stationInfo.setText(parser.getStationInfo());
                setTime.setText(parser.getSetTime());
                swipe.setRefreshing(false);
                Toast.makeText(Search_Event.this, "갱신되었습니다", Toast.LENGTH_SHORT).show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setAdapter(parser.adapter);
                parser.getXml(stId, busRouteId, ordList.toArray(new String[ordList.size()]));
                stationInfo.setText(parser.getStationInfo());
                setTime.setText(parser.getSetTime());
                swipe.setRefreshing(false);
                Toast.makeText(Search_Event.this, "갱신되었습니다", Toast.LENGTH_SHORT).show();
            }
        });

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogShow();
            }
        });

    }

    public void DialogShow(){
        final ArrayList<String> items = new ArrayList<>();
        items.add("Go_Away");
        items.add("Go_Home");

        final List selectedItems = new ArrayList();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int defaultItem = 0;
        builder.setTitle("Select");
        builder.setSingleChoiceItems(items.toArray(new String[items.size()]), defaultItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedItems.clear();
                selectedItems.add(which);
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(!selectedItems.isEmpty()) {
                    int index = (int) selectedItems.get(0);
                    tableName = items.get(index);

                    database = getApplicationContext().openOrCreateDatabase(tableName, MODE_PRIVATE, null);
                    database.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + " (arsId VARCHAR(20), stId VARCHAR(20) )");
                    database.execSQL("INSERT INTO " + tableName + "(arsId, stId) VALUES ('" + Id + "', '" + stId + "');");
                    database.close();
                    Toast.makeText(getApplicationContext(), "등록되었습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.show();

    }


}
