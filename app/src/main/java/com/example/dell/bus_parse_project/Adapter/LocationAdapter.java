package com.example.dell.bus_parse_project.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dell.bus_parse_project.CurrentLocation;
import com.example.dell.bus_parse_project.R;
import com.example.dell.bus_parse_project.ShowMapView;
import com.example.dell.bus_parse_project.ViewItem.LocationViewItem;

import java.util.ArrayList;

public class LocationAdapter extends BaseAdapter {

    private ArrayList<LocationViewItem> locationViewItems = new ArrayList<>();

    public ArrayList<LocationViewItem> getLocationViewItems() {
        return locationViewItems;
    }

    @Override
    public int getCount() {
        return locationViewItems.size();
    }

    @Override
    public Object getItem(int position) {
        return locationViewItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.locationview_item, parent, false);
        }

        TextView stationName = (TextView) convertView.findViewById(R.id.station_name);
        TextView arrivedBus = (TextView) convertView.findViewById(R.id.arrived_bus);
        ImageButton goMapView = (ImageButton) convertView.findViewById(R.id.map_view_button);

        LocationViewItem locationViewItem = locationViewItems.get(position);

        goMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), ShowMapView.class);
                intent.putExtra("tmX", locationViewItems.get(pos).getGpsX());
                intent.putExtra("tmY", locationViewItems.get(pos).getGpsY());
                intent.putExtra("stationInfo", locationViewItems.get(pos).getStationNm()
                        +"("+locationViewItems.get(pos).getArsId()+")");
                context.startActivity(intent);
            }
        });

        StringBuffer buffer = new StringBuffer();
        buffer.setLength(0);

        for (int i = 0; i < locationViewItem.getBusRouteNm().length; i++) {
            if (i == 4) {
                buffer.append(locationViewItem.getBusRouteNm()[i] + " …");
                break;
            }
            buffer.append(locationViewItem.getBusRouteNm()[i] + " ");
        }

        arrivedBus.setText("정차 버스: " + buffer);
        stationName.setText(locationViewItem.getStationNm()+" ["+locationViewItem.getArsId()+"]");

        return convertView;
    }

    public void addItem(String stationNm, String arsId, String gpsX, String gpsY, String[] busRouteNm){
        LocationViewItem item = new LocationViewItem();
        item.setStationNm(stationNm);
        item.setArsId(arsId);
        item.setGpsX(gpsX);
        item.setGpsY(gpsY);
        item.setBusRouteNm(busRouteNm);
        locationViewItems.add(item);
    }
}
