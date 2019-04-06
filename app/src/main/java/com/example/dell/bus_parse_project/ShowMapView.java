package com.example.dell.bus_parse_project;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

public class ShowMapView extends AppCompatActivity {

    private String tmX, tmY; //private String gpsX, gpsY;
    private String stationInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map_view);

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                //Toast.makeText(ShowMapView.this, "권한 허가", Toast.LENGTH_SHORT).show();

                Intent intent = getIntent();
                tmX = intent.getStringExtra("tmX");
                tmY = intent.getStringExtra("tmY");

                stationInfo = intent.getStringExtra("stationInfo");

                MapView mapView = new MapView(ShowMapView.this);
                mapView.setDaumMapApiKey("1a74f6f7e9952f91d8ac6ec73e39d930");
                ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
                MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(Double.parseDouble(tmY), Double.parseDouble(tmX));
                mapView.setMapCenterPoint(mapPoint, true);
                mapView.setZoomLevel(2, true);
                //true면 앱 실행 시 애니메이션 효과가 나오고 false면 애니메이션이 나오지않음.
                mapViewContainer.addView(mapView);

                MapPOIItem marker = new MapPOIItem();
                marker.setItemName(stationInfo);
                //marker.setTag(0);
                marker.setMapPoint(mapPoint);
                // 기본으로 제공하는 BluePin 마커 모양.
                //marker.setCustomImageAnchor(-0.5f, -1.0f);
                marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
                // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
                marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
                mapView.addPOIItem(marker);

                // MapView 객체생성 및 API Key 설정

            }
            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(ShowMapView.this, "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("지도 서비스를 사용하기 위해서는 위치 접근 권한이 필요해요")
                .setDeniedMessage("왜 거부하셨어요...\n하지만 [설정] > [권한] 에서 권한을 허용할 수 있어요.")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }
}
