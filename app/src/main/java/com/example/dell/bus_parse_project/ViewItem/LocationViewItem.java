package com.example.dell.bus_parse_project.ViewItem;

public class LocationViewItem {
    private String arsId;
    private String stationNm;
    private String gpsX;
    private String gpsY;
    private String[] busRouteNm;


    public String[] getBusRouteNm() {
        return busRouteNm;
    }

    public void setBusRouteNm(String[] busRouteNm) {
        this.busRouteNm = busRouteNm;
    }

    public String getArsId() {
        return arsId;
    }

    public void setArsId(String arsId) {
        this.arsId = arsId;
    }

    public String getStationNm() {
        return stationNm;
    }

    public String getGpsX() {
        return gpsX;
    }

    public void setGpsX(String gpsX) {
        this.gpsX = gpsX;
    }

    public String getGpsY() {
        return gpsY;
    }

    public void setGpsY(String gpsY) {
        this.gpsY = gpsY;
    }

    public void setStationNm(String stationNm) {
        this.stationNm = stationNm;
    }
}
