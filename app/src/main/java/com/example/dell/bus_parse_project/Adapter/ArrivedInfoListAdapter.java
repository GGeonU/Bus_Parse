package com.example.dell.bus_parse_project.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dell.bus_parse_project.ViewItem.ListViewItem;
import com.example.dell.bus_parse_project.R;

import java.util.ArrayList;


// http://recipes4dev.tistory.com/43

public class ArrivedInfoListAdapter extends BaseAdapter {

    private ArrayList<ListViewItem> listViewItems = new ArrayList<ListViewItem>();

    public ArrayList<ListViewItem> getListViewItems() {
        return listViewItems;
    }

    @Override
    public int getCount() {
        return listViewItems.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        TextView bus_num = (TextView) convertView.findViewById(R.id.bus_num);
        TextView first_info = (TextView) convertView.findViewById(R.id.textView1);
        TextView second_info = (TextView) convertView.findViewById(R.id.textView2);
        TextView bus_info = (TextView) convertView.findViewById(R.id.bus_info);
        TextView interval = (TextView) convertView.findViewById(R.id.interval);

        ListViewItem listViewItem = listViewItems.get(position);

        first_info.setText(listViewItem.getInfo1());
        second_info.setText(listViewItem.getInfo2());
        interval.setText("배차 간격: " + listViewItem.getInterval() + "분");
        bus_info.setText("운행 시간: " + listViewItem.getFirstTm() + "~" + listViewItem.getLastTm());


        if (listViewItem.getRouteType().equals("3")) {
            bus_num.setTextColor(Color.parseColor("#0068b7"));
            bus_num.setText(listViewItem.getNum().replaceAll("[^0-9,-]",""));
        }
        else if(listViewItem.getRouteType().equals("2")){
            bus_num.setTextColor(Color.parseColor("#70AD47"));
            bus_num.setText(listViewItem.getNum());
        }
        else if (listViewItem.getRouteType().equals("4")) {
            bus_num.setTextColor(Color.parseColor("#53b332"));
            bus_num.setText(listViewItem.getNum());
        }
        else if(listViewItem.getRouteType().equals("5")){
            bus_num.setTextColor(Color.parseColor("#FFC000"));
            bus_num.setText(listViewItem.getNum().replaceAll("[^0-9,-]",""));
        }
        else if (listViewItem.getRouteType().equals("8")) {
            bus_num.setTextColor(Color.parseColor("#009e96"));
            bus_num.setText(listViewItem.getNum());
        }
        else if(listViewItem.getRouteType().equals("6") || listViewItem.getRouteType().equals("1")){
            bus_num.setTextColor(Color.parseColor("#E84C22"));
            bus_num.setText(listViewItem.getNum().replaceAll("[^0-9,-]",""));
        }
        else {
            bus_num.setTextColor(Color.parseColor("#000000"));
            bus_num.setText(listViewItem.getNum());
        }

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return listViewItems.get(position);
    }

    public void addItem(String num, String info1, String info2, String type, String firstTm, String lastTm, String interval){
        ListViewItem item = new ListViewItem();
        item.setNum(num);
        item.setInfo1(info1);
        item.setInfo2(info2);
        item.setRouteType(type);
        item.setFirstTm(firstTm);
        item.setLastTm(lastTm);
        item.setInterval(interval);
        listViewItems.add(item);
    }
}
