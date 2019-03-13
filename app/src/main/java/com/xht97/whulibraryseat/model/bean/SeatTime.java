package com.xht97.whulibraryseat.model.bean;

public class SeatTime {

    private String id;
    private String value;

    public SeatTime() {}

    public SeatTime(String id, String value) {
        this.id = id; this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
