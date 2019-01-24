package com.xht97.whulibraryseat.model.bean;

public class Reserve {
    private int id;
    private String receipt;
    private String onDate;
    private int seatId;
    private String status;
    private String location;
    private String begin;
    private String end;
    private String actualBegin;
    private String awayBegin;
    private String awayEnd;
    private boolean userEnded;
    private String message;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public String getOnDate() {
        return onDate;
    }

    public void setOnDate(String onDate) {
        this.onDate = onDate;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActualBegin() {
        return actualBegin;
    }

    public void setActualBegin(String actualBegin) {
        this.actualBegin = actualBegin;
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

    public boolean isUserEnded() {
        return userEnded;
    }

    public void setUserEnded(boolean userEnded) {
        this.userEnded = userEnded;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
