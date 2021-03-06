package com.xht97.whulibraryseat.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xht97.whulibraryseat.app.MyApplication;
import com.xht97.whulibraryseat.model.bean.SeatTime;
import com.xht97.whulibraryseat.model.bean.StaredSeat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class AppDataUtil {

    public static SharedPreferences getSharedPref(String name) {
        return MyApplication.getContext().getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static String getTokenFromLocal() {
        SharedPreferences sharedPreferences = getSharedPref("Data");
        return sharedPreferences.getString("token", "NEYIJFUPX101195712");
    }

    public static boolean putToken(String token) {
        SharedPreferences sharedPreferences = getSharedPref("Data");
        return sharedPreferences.edit().putString("token", token).commit();
    }

    public static boolean isAutoLogin() {
        SharedPreferences sharedPreferences = getSharedPref("Data");
        return sharedPreferences.getBoolean("autoLogin", false);
    }

    public static boolean setAutoLogin(boolean autoLogin) {
        SharedPreferences sharedPreferences = getSharedPref("Data");
        return sharedPreferences.edit().putBoolean("autoLogin", autoLogin).commit();
    }

    public static boolean isPasswordExists() {
        SharedPreferences sharedPreferences = getSharedPref("Account");
        String id = sharedPreferences.getString("id1", "");
        String password = sharedPreferences.getString("pwd1", "");
        return (id.length() > 0) && (password.length() > 0);
    }

    public static String getMainId() {
        SharedPreferences sharedPreferences = getSharedPref("Account");
        return sharedPreferences.getString("id1", "");
    }

    public static boolean putMainId(String mainId) {
        SharedPreferences sharedPreferences = getSharedPref("Account");
        return sharedPreferences.edit().putString("id1", mainId).commit();
    }

    public static String getMainPassword() {
        SharedPreferences sharedPreferences = getSharedPref("Account");
        return sharedPreferences.getString("pwd1", "");
    }

    public static boolean putMainPassword(String password) {
        SharedPreferences sharedPreferences = getSharedPref("Account");
        return sharedPreferences.edit().putString("pwd1", password).commit();
    }

    public static boolean putLastSelectedBuilding(int buildingId) {
        SharedPreferences sharedPreferences = getSharedPref("Data");
        return sharedPreferences.edit().putInt("buildingId", buildingId).commit();
    }

    public static int getLastSelectedBuilding() {
        SharedPreferences sharedPreferences = getSharedPref("Data");
        return sharedPreferences.getInt("buildingId", 4);
    }

    public static boolean putLastSelectedBuildingName(String name) {
        SharedPreferences sharedPreferences = getSharedPref("Data");
        return sharedPreferences.edit().putString("buildingName", name).commit();
    }

    public static String getLastSelectedBuildingName() {
        SharedPreferences sharedPreferences = getSharedPref("Data");
        return sharedPreferences.getString("buildingName", "总馆");
    }

    public static List<SeatTime> getFullSeatTime() {
        List<SeatTime> seatTimes = new ArrayList<>();
        String[] timeValue = {"07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30"
                , "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00"
                , "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30"};
        int id = 420;

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int current = hour*60 + minute - 30;

        if ((hour == 22 && minute >= 45) || hour > 22) {
            // 22:45后可以选择第二天的所有时段
            for (int i = 0; i<timeValue.length; i++) {
                seatTimes.add(new SeatTime(String.valueOf(id + 30 * i), timeValue[i]));
            }
        } else {
            // 其他情况下只可以选择当前时间之后的时段
            for (int i = 0; i<timeValue.length; i++) {
                // 只有时段在当前时间之后才可以选择
                if (id + 30 * i >= current) {
                    seatTimes.add(new SeatTime(String.valueOf(id + 30 * i), timeValue[i]));
                }
            }
        }
        return seatTimes;
    }

    public static boolean isSeatStared(StaredSeat seat) {
        SharedPreferences sharedPreferences = getSharedPref("Data");
        String jsonString = sharedPreferences.getString("staredSeats", "{}");
        JSONObject jsonObject = JSON.parseObject(jsonString);
        return jsonObject.containsKey(String.valueOf(seat.getId()));
    }

    public static void setSeatStared(StaredSeat seat, boolean flag) {
        SharedPreferences sharedPreferences = getSharedPref("Data");
        String jsonString = sharedPreferences.getString("staredSeats", "{}");
        JSONObject jsonObject = JSON.parseObject(jsonString);
        if (flag) {
            // 添加收藏一个座位
            jsonObject.put(String.valueOf(seat.getId()), JSON.toJSON(seat));
        } else {
            // 取消收藏一个座位
            jsonObject.remove(String.valueOf(seat.getId()));
        }
        sharedPreferences.edit().putString("staredSeats", jsonObject.toJSONString()).apply();
    }

    public static List<StaredSeat> getStaredSeats() {
        SharedPreferences sharedPreferences = getSharedPref("Data");
        JSONObject jsonObject = JSON.parseObject(sharedPreferences.getString("staredSeats", "{}"));
        Iterator<String> iterator = jsonObject.keySet().iterator();
        List<StaredSeat> staredSeats = new ArrayList<>();
        while (iterator.hasNext()) {
            JSONObject seatObject = JSON.parseObject(jsonObject.getString(iterator.next()));
            StaredSeat staredSeat = JSON.toJavaObject(seatObject, StaredSeat.class);
            staredSeat.setStatus("FREE");
            staredSeats.add(staredSeat);
        }
        return staredSeats;
    }

}
