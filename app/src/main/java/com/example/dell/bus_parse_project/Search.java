package com.example.dell.bus_parse_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dell.bus_parse_project.Adapter.SearchAdapter;

import java.util.ArrayList;

public class Search extends AppCompatActivity {

    SearchAdapter adapter = new SearchAdapter();
    XmlParser parser = new XmlParser();

    private ArrayList<String> arsIdList = new ArrayList<>();   // arsId
    private ArrayList<String> stIdList = new ArrayList<>();    // stationId
    private ArrayList<String> stationNameList = new ArrayList<>();  // 정류소 이름
    private ArrayList<String> busRouteNmList = new ArrayList<>();  // number
    private ArrayList<String> busRouteIdList = new ArrayList<>(); // routeId
    private ArrayList<String> tmXList = new ArrayList<>(); // 위도
    private ArrayList<String> tmYList = new ArrayList<>(); // 경도

    private EditText editText;  // 검색 텍스트
    private ImageButton button; // 검색 버튼
    private ListView listView;  // 검색결과 리스트
    private TextView text;

    private Handler handler;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        editText = (EditText) findViewById(R.id.search_editText);
        button = (ImageButton) findViewById(R.id.search_button);
        listView = (ListView) findViewById(R.id.listview);
        text = (TextView) findViewById(R.id.text);
        listView.setFocusable(false);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getSearchViewItems().clear();
                parser.getArsId(editText.getText().toString(), tmXList, tmYList, arsIdList, stIdList, stationNameList);
                text.setText("검색 결과");
                if (arsIdList.size() == 0) {
                    text.setText("검색결과가 없습니다");
                    adapter.getSearchViewItems().clear();
                    listView.setAdapter(adapter);
                }

                for (int i = 0; i < arsIdList.size(); i++) {
                    parser.getRouteId(arsIdList.get(i), busRouteNmList, busRouteIdList);
                    adapter.addItem(arsIdList.get(i), stIdList.get(i), stationNameList.get(i), busRouteNmList.toArray(new String[busRouteNmList.size()]), tmXList.get(i), tmYList.get(i));
                    listView.setAdapter(adapter);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), SearchResult.class);
                parser.getRouteId(arsIdList.get(position),busRouteNmList, busRouteIdList);
                intent.putExtra("arsId", arsIdList.get(position));
                intent.putExtra("stId", stIdList.get(position));
                intent.putExtra("busRouteId", busRouteIdList.toArray(new String[busRouteIdList.size()]));

                startActivity(intent);

                handler = new Handler();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = progressDialog.show(Search.this, "", "잠시만 기다려주세요", true);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (progressDialog != null && progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 5000);
                    }
                });
            }
        });


    }
}
