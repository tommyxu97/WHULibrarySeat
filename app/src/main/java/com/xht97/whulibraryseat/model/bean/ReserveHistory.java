package com.xht97.whulibraryseat.model.bean;

public class ReserveHistory {

    private int id;
    private String date;
    private String begin;
    private String end;
    private String awayBegin;
    private String awayEnd;
    private String loc;
    private String stat;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getAwayBegin() {
        return awayBegin;
    }

    public void setAwayBegin(String awayBegin) {
        this.awayBegin = awayBegin;
    }

    public String getAwayEnd() {
        return awayEnd;
    }

    public void setAwayEnd(String awayEnd) {
        this.awayEnd = awayEnd;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }
}
