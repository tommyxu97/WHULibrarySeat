package com.xht97.whulibraryseat.model.bean;

import android.content.Intent;

import java.io.Serializable;

public class Seat implements Serializable {

    private int id;
    private String name;
    private String type;
    private String status;
    private boolean window;
    private boolean power;
    private boolean computer;
    private boolean local;
    // 为了方便实现布局选座的功能添加的成员变量
    private String location;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isWindow() {
        return window;
    }

    public void setWindow(boolean window) {
        this.window = window;
    }

    public boolean isPower() {
        return power;
    }

    public void setPower(boolean power) {
        this.power = power;
    }

    public boolean isComputer() {
        return computer;
    }

    public void setComputer(boolean computer) {
        this.computer = computer;
    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public StaredSeat toStaredSeat() {
        StaredSeat staredSeat = new StaredSeat();
        staredSeat.setId(id);
        staredSeat.setName(name);
        staredSeat.setType(type);
        staredSeat.setStatus(status);
        staredSeat.setWindow(window);
        staredSeat.setPower(power);
        staredSeat.setComputer(computer);
        staredSeat.setLocal(local);
        staredSeat.setLocation(location);
        return staredSeat;
    }

    public int getSeatRow() {
        if (location.length() < 4) {
            return 1;
        } else {
            return Integer.parseInt(location.substring(0, location.length() - 3));
        }
    }

    public int getSeatColumn() {
        if (location.length() < 4) {
            return Integer.parseInt(location);
        } else {
            return Integer.parseInt(location.substring(location.length() - 3));
        }
    }
}
