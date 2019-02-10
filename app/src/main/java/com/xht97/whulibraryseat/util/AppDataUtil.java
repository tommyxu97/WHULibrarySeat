package com.xht97.whulibraryseat.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.xht97.whulibraryseat.app.MyApplication;

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

    public static boolean isPasswordExists() {
        SharedPreferences sharedPreferences = getSharedPref("Account");
        String id = sharedPreferences.getString("id1", "");
        String password = sharedPreferences.getString("pwd1", "");
        return (id.length() == 13) && (password.length() > 0);
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

}
