package com.example.dell.bus_parse_project.ViewItem;

public class SearchViewItem {
    private String id;
    private String name;
    private String arsId;
    private String routeId;
    private String seq;
    private String[] busRouteId;
    private String tmX;
    private String tmY;

    public String getTmX() {
        return tmX;
    }

    public void setTmX(String tmX) {
        this.tmX = tmX;
    }

    public String getTmY() {
        return tmY;
    }

    public void setTmY(String tmY) {
        this.tmY = tmY;
    }

    public String[] getBusRouteId() {
        return busRouteId;
    }

    public void setBusRouteId(String[] busRouteId) {
        this.busRouteId = busRouteId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArsId() {
        return arsId;
    }

    public void setArsId(String arsId) {
        this.arsId = arsId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }
}
