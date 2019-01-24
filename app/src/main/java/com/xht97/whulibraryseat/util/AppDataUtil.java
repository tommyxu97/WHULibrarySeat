package com.xht97.whulibraryseat.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.xht97.whulibraryseat.base.MyApplication;

public class AppDataUtil {

    public static String getTokenFromLocal() {
        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("Data", Context.MODE_PRIVATE);
        return sharedPreferences.getString("token", "ERROR");
    }

}
