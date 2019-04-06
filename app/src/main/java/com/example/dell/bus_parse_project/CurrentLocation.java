package com.example.dell.bus_parse_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.bus_parse_project.Adapter.LocationAdapter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CurrentLocation extends AppCompatActivity {

    LocationAdapter adapter = new LocationAdapter();

    private ImageButton refreshButton;
    private TextView txtResult;
    private TextView setTime;
    private String key = "2ysbgQFrENYJHhR8eRUwPTG6A19dAGm%2BqRXK%2FRaKcaC4Ml%2FHvbpa2i7N3dwrkhnkJD2iQbSPNbBLZyVFq5tBBw%3D%3D";
    private String getStationByPos = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByPos?ServiceKey=";

    private ListView listView;

    private ArrayList<String> busRouteNm = new ArrayList<>();
    private ArrayList<String> busRouteId = new ArrayList<>();
    private ArrayList<String> stationNmList = new ArrayList<>();
    private ArrayList<String> stationIdList = new ArrayList<>();
    private ArrayList<String> gpsXList = new ArrayList<>();
    private ArrayList<String> gpsYList = new ArrayList<>();
    private ArrayList<String> arsIdList = new ArrayList<>();

    private String arsId = null, stationNm = null, stationId = null, gpsX = null, gpsY = null;
    private boolean isArsId = false, isStationNm = false, isStationId = false, isGpsX = false, isGpsY = false;

    private Handler handler;
    private ProgressDialog progressDialog;

    double longitude = 0, latitude = 0;

    Date today = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location);
        adapter.getLocationViewItems().clear();


        refreshButton = (ImageButton) findViewById(R.id.refresh);
        txtResult = (TextView) findViewById(R.id.search_info);
        listView = (ListView) findViewById(R.id.listview);
        setTime = (TextView) findViewById(R.id.set_time);

        LocationManager manager;

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        }

        manager = (LocationManager) getSystemService(LOCATION_SERVICE);


        boolean isGPSEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Log.d("enabled", isGPSEnabled+" "+isNetworkEnabled);

        Location location;

        location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(location == null){
            location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Toast.makeText(getApplicationContext(), "Network Provider를 사용합니다. ",Toast.LENGTH_LONG).show();
        }

        if(location != null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();

            Log.d("aa", longitude+" "+latitude);

            getStationInfo(longitude, latitude);
            SimpleDateFormat time = new SimpleDateFormat("kk:mm:ss");
            setTime.setText(time.format(today) + " 기준");

            for (int i = 0; i < arsIdList.size(); i++) {
                getRouteId(arsIdList.get(i));
                adapter.addItem(stationNmList.get(i), arsIdList.get(i),
                        gpsXList.get(i), gpsYList.get(i),
                        busRouteNm.toArray(new String[busRouteNm.size()]));
            }

            txtResult.setText("주변 250m 근처 버스 정류장 입니다");
            listView.setAdapter(adapter);

        }

        else{
            Log.d("GPS Error", "GPS Error");
            txtResult.setText("GPS를 사용할 수 없습니다. 다시 시도해주세요.");
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), SearchResult.class);
                getRouteId(arsIdList.get(position));
                intent.putExtra("arsId", arsIdList.get(position));
                intent.putExtra("stId", stationIdList.get(position));
                intent.putExtra("busRouteId", busRouteId.toArray(new String[busRouteId.size()]));
                startActivity(intent);
                handler = new Handler();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = progressDialog.show(CurrentLocation.this, null, null, true);
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


    public void getStationInfo(double longitude, double latitude) {
        StrictMode.enableDefaults();
        arsIdList.clear();
        stationNmList.clear();
        gpsXList.clear();
        gpsYList.clear();
        try {
            URL url = new URL(getStationByPos + key + "&tmX=" + longitude + "&tmY=" + latitude + "&radius=250");

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
                        if (parser.getName().equals("stationId")) {
                            isStationId = true;
                        }
                        if (parser.getName().equals("stationNm")) {
                            isStationNm = true;
                        }
                        if (parser.getName().equals("gpsX")) {
                            isGpsX = true;
                        }
                        if (parser.getName().equals("gpsY")) {
                            isGpsY = true;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if (isArsId) {
                            arsId = parser.getText();
                            isArsId = false;
                        }
                        if (isStationNm) {
                            stationNm = parser.getText();
                            isStationNm = false;
                        }
                        if (isStationId) {
                            stationId = parser.getText();
                            isStationId = false;
                        }
                        if (isGpsX) {
                            gpsX = parser.getText();
                            isGpsX = false;
                        }
                        if (isGpsY) {
                            gpsY = parser.getText();
                            isGpsY = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("itemList")) {
                            arsIdList.add(arsId);
                            stationNmList.add(stationNm);
                            stationIdList.add(stationId);
                            gpsXList.add(gpsX);
                            gpsYList.add(gpsY);
                        }
                }
                parserEvent = parser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getRouteId(String arsId) {
        StrictMode.enableDefaults();
        busRouteNm.clear();
        busRouteId.clear();

        String routeId = null, busNum = null;
        boolean isrouteId = false, isbusNum = false;
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
                            isrouteId = true;
                        }
                        if (parser.getName().equals("busRouteNm")) {
                            isbusNum = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        if (isrouteId) {
                            routeId = parser.getText();
                            isrouteId = false;
                        }
                        if (isbusNum) {
                            busNum = parser.getText();
                            busNum = busNum.replaceAll("[^0-9,-]", "");
                            isbusNum = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("itemList")) {
                            busRouteId.add(routeId);
                            busRouteNm.add(busNum);
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.

            Log.d("test", "onLocationChanged, location:" + location);
            longitude = location.getLongitude(); //경도
            latitude = location.getLatitude();   //위도
            double altitude = location.getAltitude();   //고도
            float accuracy = location.getAccuracy();    //정확도
            String provider = location.getProvider();   //위치제공자
            //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
            //Network 위치제공자에 의한 위치변화
            //Network 위치는 Gps에 비해 정확도가 많이 떨어진다.


        }

        public void onProviderDisabled(String provider) {
            // Disabled시
            Log.d("test", "onProviderDisabled, provider:" + provider);
        }

        public void onProviderEnabled(String provider) {
            // Enabled시
            Log.d("test", "onProviderEnabled, provider:" + provider);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 변경시
            Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
        }
    };
}


