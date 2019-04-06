package com.example.dell.bus_parse_project;

import android.os.StrictMode;
import android.util.Log;

import com.example.dell.bus_parse_project.Adapter.ListViewAdapter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class XmlParser {

    public ListViewAdapter adapter = new ListViewAdapter();

    private String key = "2ysbgQFrENYJHhR8eRUwPTG6A19dAGm%2BqRXK%2FRaKcaC4Ml%2FHvbpa2i7N3dwrkhnkJD2iQbSPNbBLZyVFq5tBBw%3D%3D";
    private String getArrInfoUrl = "http://ws.bus.go.kr/api/rest/arrive/getArrInfoByRoute?ServiceKey=";

    String arsId = null, stNm = null, mkTm = null;


    public void getXml(String stId, String[] routeId, String[] ord) {
        StrictMode.enableDefaults();
        adapter.getListViewItems().clear();

        boolean isArrmsg1 = false, isArrmsg2 = false, isRtNm = false, isStNm = false,
                isArsId = false, isRouteType = false, isFirstTM = false, isLastTM = false, isTerm = false, isMkTm = false;

        String arrmsg1 = null, arrmsg2=null, rtNm = null, routeType = null, firstTm = null, lastTm = null, timeH = null, timeM = null, term = null;

        for (int i = 0; i < ord.length; i++) {
            String callUrl = getArrInfoUrl + key + "&stId=" + stId
                    + "&busRouteId=" + routeId[i] + "&ord=" + ord[i];

            try {
                URL url = new URL(callUrl);
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(url.openStream(), "utf-8");
                int parserEvent = parser.getEventType();

                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                        case XmlPullParser.START_TAG:
                            if (parser.getName().equals("stNm")) {
                                isStNm = true;
                            }
                            if (parser.getName().equals("arsId")) {
                                isArsId = true;
                            }
                            if (parser.getName().equals("routeType")) {
                                isRouteType = true;
                            }
                            if (parser.getName().equals("firstTm")) {
                                isFirstTM = true;
                            }
                            if (parser.getName().equals("lastTm")) {
                                isLastTM = true;
                            }
                            if (parser.getName().equals("rtNm")) {
                                isRtNm = true;
                            }
                            if (parser.getName().equals("arrmsg1")) {
                                isArrmsg1 = true;
                            }
                            if (parser.getName().equals("arrmsg2")) {
                                isArrmsg2 = true;
                            }
                            if (parser.getName().equals("term")) {
                                isTerm = true;
                            }
                            if (parser.getName().equals("mkTm")) {
                                isMkTm = true;
                            }
                            break;

                        case XmlPullParser.TEXT:
                            if (isStNm) {
                                stNm = parser.getText();
                                isStNm = false;
                            }
                            if (isArsId) {
                                arsId = parser.getText();
                                isArsId = false;
                            }
                            if (isRouteType) {
                                routeType = parser.getText();
                                isRouteType = false;
                            }
                            if (isRtNm) {
                                rtNm = parser.getText();
                                rtNm = rtNm.replaceAll("[^0-9,-]", "");
                                isRtNm = false;
                            }
                            if (isFirstTM) {
                                firstTm = parser.getText();
                                timeH = firstTm.substring(firstTm.length() - 6, firstTm.length() - 4);
                                timeM = firstTm.substring(firstTm.length() - 4, firstTm.length() - 2);
                                firstTm = timeH + ":" + timeM;
                                isFirstTM = false;
                            }
                            if (isLastTM) {
                                lastTm = parser.getText();
                                timeH = lastTm.substring(lastTm.length() - 6, lastTm.length() - 4);
                                timeM = lastTm.substring(lastTm.length() - 4, lastTm.length() - 2);
                                lastTm = timeH + ":" + timeM;
                                isLastTM = false;
                            }
                            if (isArrmsg1) {
                                arrmsg1 = parser.getText();
                                isArrmsg1 = false;
                            }
                            if (isArrmsg2) {
                                arrmsg2 = parser.getText();
                                isArrmsg2 = false;
                            }
                            if (isTerm) {
                                term = parser.getText();
                                isTerm = false;
                            }
                            if (isMkTm) {
                                mkTm = parser.getText();
                                mkTm = mkTm.substring(mkTm.length() - 10, mkTm.length() - 2);
                                isMkTm = false;
                            }
                            break;

                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("itemList")) {
                                adapter.addItem(rtNm, arrmsg1, arrmsg2, routeType, firstTm, lastTm, term);
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

    public String getSetTime() {
        return mkTm + " 기준";
    }

    public String getStationInfo() {
        return stNm + "[" + arsId + "]";
    }

    public void getRouteId(String arsId, ArrayList<String> busRouteNmList, ArrayList<String> busRouteIdList) {
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

    public void getArsId(String stname, ArrayList<String> tmXList, ArrayList<String> tmYList, ArrayList<String> arsIdList, ArrayList<String> stIdList, ArrayList<String> stationNameList) {
        StrictMode.enableDefaults();

        tmYList.clear();
        tmXList.clear();
        arsIdList.clear();
        stIdList.clear();
        stationNameList.clear();

        boolean isArsid = false, isStId = false, isStNm = false;
        boolean isTmX = false, isTmY = false;

        String tmX = null, tmY = null;
        String arsId = null, stId = null, stNm = null;

        String station = "http://ws.bus.go.kr/api/rest/stationinfo/getLowStationByName?ServiceKey=" +
                "2ysbgQFrENYJHhR8eRUwPTG6A19dAGm%2BqRXK%2FRaKcaC4Ml%2FHvbpa2i7N3dwrkhnkJD2iQbSPNbBLZyVFq5tBBw%3D%3D&stSrch=";
        String call_url = station + stname;

        try {
            URL url = new URL(call_url);

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(url.openStream(), "utf-8");
            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("stNm")) {
                            isStNm = true;
                        }
                        if (parser.getName().equals("arsId")) {
                            isArsid = true;
                        }
                        if (parser.getName().equals("stId")) {
                            isStId = true;
                        }
                        if (parser.getName().equals("tmX")) {
                            isTmX = true;
                        }
                        if (parser.getName().equals("tmY")) {
                            isTmY = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        if (isStNm) {
                            stNm = parser.getText();
                            isStNm = false;
                        }
                        if (isArsid) {
                            arsId = parser.getText();
                            isArsid = false;
                        }
                        if (isStId) {
                            stId = parser.getText();
                            isStId = false;
                        }
                        if (isTmX) {
                            tmX = parser.getText();
                            isTmX = false;
                        }
                        if (isTmY) {
                            tmY = parser.getText();
                            isTmY = false;
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("itemList")) {
                            stationNameList.add(stNm);
                            arsIdList.add(arsId);
                            stIdList.add(stId);
                            tmXList.add(tmX);
                            tmYList.add(tmY);
                        }
                        break;
                }
                parserEvent = parser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getSeq(String[] busRouteId, String paramArsId, ArrayList<String> ordList, ArrayList<String> busRouteNmList, ArrayList<String> paramBusRouteId) {
        StrictMode.enableDefaults();
        String callUrl = "http://ws.bus.go.kr/api/rest/busRouteInfo/getStaionByRoute?ServiceKey=";

//        busRouteId = new HashSet<String>(Arrays.asList(busRouteId)).toArray(new String[0]);


        boolean isSeq = false, isBusRouteNm = false, isArsId = false, isBusRouteId = false;

        String seq = null, arsId = null, busRouteNm = null, NewbusRouteId = null;

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
                            if(parser.getName().equals("busRouteId")){
                                isBusRouteId = true;
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
                            if(isBusRouteId){
                                NewbusRouteId = parser.getText();
                                isBusRouteId = false;
                            }

                            break;

                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("itemList")) {
                                if (arsId.equals(paramArsId)) {
                                    ordList.add(seq);
                                    busRouteNmList.add(busRouteNm);
                                    paramBusRouteId.add(NewbusRouteId);
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

        for(int j=0; j<paramBusRouteId.size(); j++){
            Log.d("aaa", j+" "+paramBusRouteId.get(j));
        }
    }

}
