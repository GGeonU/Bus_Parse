package com.example.dell.bus_parse_project.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.bus_parse_project.R;
import com.example.dell.bus_parse_project.Search;
import com.example.dell.bus_parse_project.ShowMapView;
import com.example.dell.bus_parse_project.ViewItem.SearchViewItem;

import java.util.ArrayList;

public class SearchAdapter extends BaseAdapter {

    private ArrayList<SearchViewItem> searchViewItems = new ArrayList<SearchViewItem>();
    private String tmX;
    private String tmY;
    private String stationInfo;

    public ArrayList<SearchViewItem> getSearchViewItems() {
        return searchViewItems;
    }

    private ImageButton goMapView;

    @Override
    public int getCount() {
        return searchViewItems.size();
    }

    @Override
    public Object getItem(int position) {
        return searchViewItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.searchview_item, parent, false);
        }

        TextView stationName = (TextView) convertView.findViewById(R.id.station_name);
        TextView arrivedBus = (TextView) convertView.findViewById(R.id.arrived_bus);
        goMapView = (ImageButton) convertView.findViewById(R.id.map_view_button);

        SearchViewItem searchViewItem = searchViewItems.get(position);


        stationName.setText(searchViewItem.getArsId() + " [" + searchViewItem.getName() + "]");

        goMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmX = searchViewItems.get(pos).getTmX();
                tmY = searchViewItems.get(pos).getTmY();
                stationInfo = searchViewItems.get(pos).getArsId()+"("+searchViewItems.get(pos).getName()+")";

                Intent intent = new Intent(context, ShowMapView.class);
                intent.putExtra("tmX", tmX);
                intent.putExtra("tmY", tmY);
                intent.putExtra("stationInfo", stationInfo);
                context.startActivity(intent);

            }
        });
        StringBuffer buffer = new StringBuffer();
        buffer.setLength(0);

        for (int i = 0; i < searchViewItem.getBusRouteId().length; i++) {
            if (i == 4) {
                buffer.append(searchViewItem.getBusRouteId()[i] + " …");
                break;
            }
            buffer.append(searchViewItem.getBusRouteId()[i] + " ");
        }
        arrivedBus.setText("정차 버스: " + buffer);
        // = station_id.setText(searchViewItems.get(position).getId());
        return convertView;
    }

    /*ImageButton.OnClickListener mOnClickListener = new ImageButton.OnClickListener(){
        @Override
        public void onClick(View v) {
            int pos = Integer.parseInt(v.getTag().toString());

        }
    };*/


    public void addItem(String name, String id, String arsId, String[] busRouteNm, String tmX, String tmY) {
        SearchViewItem item = new SearchViewItem();
        item.setName(name);
        item.setId(id);
        item.setArsId(arsId);
        item.setBusRouteId(busRouteNm);
        item.setTmX(tmX);
        item.setTmY(tmY);
        searchViewItems.add(item);
    }

}
