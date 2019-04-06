package com.example.dell.bus_parse_project.ViewItem;

public class ListViewItem {
    private String num;
    private String info1;
    private String info2;
    private String routeType;
    private String firstTm;
    private String lastTm;
    private String interval;

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getLastTm() {
        return lastTm;
    }

    public void setLastTm(String lastTm) {
        this.lastTm = lastTm;
    }

    public String getFirstTm() {
        return firstTm;
    }

    public void setFirstTm(String firstTm) {
        this.firstTm = firstTm;
    }

    public void setNum(String num){
        this.num = num;
    }

    public void setInfo1(String info1){
        this.info1 = info1;
    }

    public void setInfo2(String info2){
        this.info2 = info2;
    }

    public void setRouteType(String routeType) { this.routeType = routeType; }

    public String getNum() {
        return num;
    }

    public String getInfo1() {
        return info1;
    }

    public String getInfo2() {
        return info2;
    }

    public String getRouteType() { return routeType; }
}
