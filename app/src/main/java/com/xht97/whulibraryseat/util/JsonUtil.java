package com.xht97.whulibraryseat.util;

import android.util.Log;

import com.xht97.whulibraryseat.model.bean.Reserve;
import com.xht97.whulibraryseat.model.bean.ReserveHistory;
import com.xht97.whulibraryseat.model.bean.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtil {

    /**
     * 判断请求是否成功
     *
     * @param data 序列化为字符串的原始json数据
     */
    public static boolean isRequestSuccessful(String data) {
        try {
            JSONObject object = new JSONObject(data);
            if (object.getString("status").equals("success")) {
                return true;
            } else {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("JsonUtil", "JSON解析失败");
        }
        return false;
    }

    /**
     * 服务器返回错误时，获得错误原因
     *
     * @param data 序列化为字符串的原始json数据
     */
    public static String getErrorMessage(String data) {
        try {
            JSONObject object = new JSONObject(data);
            return object.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("JsonUtil", "JSON解析失败");
            return data;
        }
    }

    public static String getToken(String data) {
        try {
            JSONObject object = new JSONObject(data);
            if (isRequestSuccessful(object)) {
                JSONObject dataObject = object.getJSONObject("data");
                return dataObject.getString("token");
            } else return "ERROR";
        } catch (JSONException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    public static User getUserInfo(String data) {
        try {
            JSONObject object = new JSONObject(data);
            if (isRequestSuccessful(object)) {
                User user = new User();
                JSONObject dataObject = object.getJSONObject("data");
                user.setId(dataObject.getInt("id"));
                user.setEnabled(dataObject.getBoolean("enabled"));
                user.setName(dataObject.getString("name"));
                user.setStudentId(dataObject.getString("username"));
                user.setStatus(dataObject.getString("status"));
                user.setLastLogin(dataObject.getString("lastLogin"));
                user.setCheckedIn(dataObject.getBoolean("checkedIn"));
                user.setViolationCount(dataObject.getInt("violationCount"));
                return user;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> getDateList(String data) {
        try {
            JSONObject object = new JSONObject(data);
            if (isRequestSuccessful(object)) {
                JSONObject dataObject = object.getJSONObject("data");
                JSONArray datesArray = object.getJSONArray("dates");
                List<String> list = new ArrayList<>();
                for(int i = 0; i<datesArray.length(); i++) {
                    list.add(datesArray.getString(i));
                }
                return list;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Reserve> getReserveList(String data) {
        try {
            JSONObject object = new JSONObject(data);
            if (isRequestSuccessful(object)) {

                JSONArray dataArray = object.getJSONArray("data");
                List<Reserve> reserveList = new ArrayList<>();
                for(int i = 0; i < dataArray.length(); i++) {
                    JSONObject reserveObject = dataArray.getJSONObject(i);
                    Reserve reserve = new Reserve();
                    reserve.setId(reserveObject.getInt("id"));
                    reserve.setReceipt(reserveObject.getString("receipt"));
                    reserve.setOnDate(reserveObject.getString("onDate"));
                    reserve.setSeatId(reserveObject.getInt("seatId"));
                    reserve.setStatus(reserveObject.getString("status"));
                    reserve.setLocation(reserveObject.getString("location"));
                    reserve.setBegin(reserveObject.getString("begin"));
                    reserve.setEnd(reserveObject.getString("end"));
                    reserve.setActualBegin(reserveObject.getString("actualBegin"));
                    reserve.setAwayBegin(reserveObject.getString("awayBegin"));
                    reserve.setAwayEnd(reserveObject.getString("awayEnd"));
                    reserve.setUserEnded(reserveObject.getBoolean("userEnded"));
                    reserve.setMessage(reserveObject.getString("message"));
                    reserveList.add(reserve);
                }

                return reserveList;

            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<ReserveHistory> getReserveHistoryList(String data) {
        try {
            JSONObject object = new JSONObject(data);
            if (isRequestSuccessful(object)) {

                JSONArray dataArray = object.getJSONObject("data").getJSONArray("reservations");
                List<ReserveHistory> reserveHistoryList = new ArrayList<>();
                for(int i = 0; i < dataArray.length(); i++) {
                    JSONObject reserveObject = dataArray.getJSONObject(i);
                    ReserveHistory reserveHistory = new ReserveHistory();
                    reserveHistory.setId(reserveObject.getInt("id"));
                    reserveHistory.setDate(reserveObject.getString("date"));
                    reserveHistory.setBegin(reserveObject.getString("begin"));
                    reserveHistory.setEnd(reserveObject.getString("end"));
                    reserveHistory.setAwayBegin(reserveObject.getString("awayBegin"));
                    reserveHistory.setAwayEnd(reserveObject.getString("awayEnd"));
                    reserveHistory.setLoc(reserveObject.getString("loc"));
                    reserveHistory.setStat(reserveObject.getString("stat"));
                    reserveHistoryList.add(reserveHistory);
                }

                return reserveHistoryList;

            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }




    private static boolean isRequestSuccessful(JSONObject object) {
        try {
            return object.getString("status").equals("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
