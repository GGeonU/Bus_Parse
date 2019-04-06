package com.example.dell.bus_parse_project.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dell.bus_parse_project.R;
import com.example.dell.bus_parse_project.XmlParser;

public class DataBaseFragment extends Fragment {

    private String stId;
    private String[] busRouteIdArray, ordArray;

    XmlParser parser = new XmlParser();

    public DataBaseFragment() {
    }

    public static DataBaseFragment newInstance(String stId, String[] busRouteIdArray, String[] ordArray) {
        DataBaseFragment fragment = new DataBaseFragment();
        Bundle args = new Bundle();
        args.putString("stId", stId);
        args.putStringArray("busRoute", busRouteIdArray);
        args.putStringArray("ordArray", ordArray);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            stId = getArguments().getString("stId");
            busRouteIdArray = getArguments().getStringArray("busRoute");
            ordArray = getArguments().getStringArray("ordArray");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.arrived_info, container, false);

        TextView stationName = (TextView) view.findViewById(R.id.station_name);
        TextView setTime = (TextView) view.findViewById(R.id.set_time);

        ListView listView = (ListView) view.findViewById(R.id.arrived_info);

        parser.getXml(stId, busRouteIdArray, ordArray);
        stationName.setText(parser.getStationInfo());
        setTime.setText(parser.getSetTime());
        listView.setAdapter(parser.adapter);

        return view;
    }

}
