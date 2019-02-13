package com.xht97.whulibraryseat.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.xht97.whulibraryseat.model.bean.Building;
import com.xht97.whulibraryseat.model.bean.InstantReserve;
import com.xht97.whulibraryseat.model.bean.Reserve;
import com.xht97.whulibraryseat.model.bean.ReserveHistory;
import com.xht97.whulibraryseat.model.bean.Room;
import com.xht97.whulibraryseat.model.bean.Seat;
import com.xht97.whulibraryseat.model.bean.SeatTime;
import com.xht97.whulibraryseat.model.bean.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class JsonUtil {

    private static Gson mGson = new Gson();

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

    public static Object stringToObject(String json , Class className){
        return  mGson.fromJson(json , className) ;
    }

    public static <T> String objectToString(T object) {
        return mGson.toJson(object);
    }

    public static <T> List<T> stringToList(String json ,Class<T> className){
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for(final JsonElement elem :array){
            list.add(mGson.fromJson(elem, className));
        }
        return list ;
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

    public static List<List> getDateList(String data) {
        try {
            JSONObject object = new JSONObject(data);
            if (isRequestSuccessful(object)) {
                List<List> lists = new ArrayList<>();

                JSONObject dataObject = object.getJSONObject("data");

                List<String> dates = new ArrayList<>();
                JSONArray datesArray = dataObject.getJSONArray("dates");
                for(int i = 0; i<datesArray.length(); i++) {
                    dates.add(datesArray.getString(i));
                }
                lists.add(dates);

                List<Building> buildings = new ArrayList<>();
                JSONArray buildingArray = dataObject.getJSONArray("buildings");
                for (int i = 0; i<buildingArray.length(); i++) {
                    JSONArray array = buildingArray.getJSONArray(i);
                    Building building = new Building();
                    building.setId(array.getInt(0));
                    building.setName(array.getString(1));
                    building.setFloors(array.getInt(2));
                    buildings.add(building);
                }
                lists.add(buildings);

                return lists;
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

                List<Reserve> reserveList = new ArrayList<>();
                // Fixbugs:当目前没有预约的情况
                if (object.get("data") == JSONObject.NULL) {
                    return reserveList;
                }

                JSONArray dataArray = object.getJSONArray("data");
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
                    // 暂时注释，等待测试
//                    reserve.setActualBegin(reserveObject.getString("actualBegin"));
//                    reserve.setAwayBegin(reserveObject.getString("awayBegin"));
//                    reserve.setAwayEnd(reserveObject.getString("awayEnd"));
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

                List<ReserveHistory> reserveHistoryList = new ArrayList<>();
                if (object.getJSONObject("data").get("reservations") == JSONObject.NULL) {
                    return reserveHistoryList;
                }

                JSONArray dataArray = object.getJSONObject("data").getJSONArray("reservations");
                for(int i = 0; i < dataArray.length(); i++) {
                    JSONObject reserveObject = dataArray.getJSONObject(i);
                    ReserveHistory reserveHistory = new ReserveHistory();
                    reserveHistory.setId(reserveObject.getInt("id"));
                    reserveHistory.setDate(reserveObject.getString("date"));
                    reserveHistory.setBegin(reserveObject.getString("begin"));
                    reserveHistory.setEnd(reserveObject.getString("end"));
                    if (reserveObject.get("awayBegin") == JSONObject.NULL) {
                        reserveHistory.setAwayBegin(null);
                    } else {
                        reserveHistory.setAwayBegin(reserveObject.getString("awayBegin"));
                    }
                    if (reserveObject.get("awayEnd") == JSONObject.NULL) {
                        reserveHistory.setAwayEnd(null);
                    } else {
                        reserveHistory.setAwayEnd(reserveObject.getString("awayEnd"));
                    }
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

    public static List<Room> getRoomList(String data) {
        try {
            JSONObject object = new JSONObject(data);

            if(isRequestSuccessful(object)) {
                JSONArray dataArray = object.getJSONArray("data");
                List<Room> roomList = new ArrayList<>();
                for(int i = 0; i < dataArray.length(); i++) {
                    JSONObject roomObject = dataArray.getJSONObject(i);
                    Room room = new Room();
                    room.setRoomId(roomObject.getInt("roomId"));
                    room.setRoom(roomObject.getString("room"));
                    room.setFloor(roomObject.getInt("floor"));
                    room.setMaxHour(roomObject.getInt("maxHour"));
                    room.setReserved(roomObject.getInt("reserved"));
                    room.setInUse(roomObject.getInt("inUse"));
                    room.setAway(roomObject.getInt("away"));
                    room.setTotalSeat(roomObject.getInt("totalSeats"));
                    room.setFree(roomObject.getInt("free"));
                    roomList.add(room);
                }
                return roomList;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static List<Seat> getSeats(String data) {
        try {
            JSONObject object = new JSONObject(data);

            if (isRequestSuccessful(object)) {
                List<Seat> seats =  new ArrayList<>();
                if (object.getJSONObject("data").get("layout") == JSONObject.NULL) {
                    return seats;
                }

                JSONObject layoutObject = object.getJSONObject("data").getJSONObject("layout");
                Iterator<String> iterator = layoutObject.keys();
                while (iterator.hasNext()) {
                    Seat seat = new Seat();
                    String key = iterator.next();
                    JSONObject seatObject = layoutObject.getJSONObject(key);

                    if(seatObject.getString("type").equals("seat")) {

                        seat.setLocation(key);
                        seat.setId(seatObject.getInt("id"));
                        seat.setName(seatObject.getString("name"));
                        seat.setType("seat");
                        seat.setStatus(seatObject.getString("status"));
                        seat.setWindow(seatObject.getBoolean("window"));
                        seat.setPower(seatObject.getBoolean("power"));
                        seat.setComputer(seatObject.getBoolean("computer"));
                        seat.setLocal(seatObject.getBoolean("local"));

                        // 最后把这个位置加入列表
                        seats.add(seat);
                    }
                }

                // BugFixed: 根据座位的名字排序，在显示在界面上时更利于用户选择
                Collections.sort(seats, new Comparator<Seat>() {
                    @Override
                    public int compare(Seat o1, Seat o2) {
                        return Integer.parseInt(o1.getName()) - Integer.parseInt(o2.getName());
                    }
                });

                return seats;
            } else {
                return null;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int[] getLayoutParameter(String data) {
        try {
            JSONObject object = new JSONObject(data);

            if (isRequestSuccessful(object)) {
                JSONObject dataObject = object.getJSONObject("data");
                int[] layout = {dataObject.getInt("cols"), dataObject.getInt("rows")};
                return layout;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<SeatTime> getSeatStartTime(String data) {
        try {
            JSONObject object = new JSONObject(data);

            if (isRequestSuccessful(object)) {
                List<SeatTime> seatTimes =  new ArrayList<>();
                if (object.getJSONObject("data").get("startTimes") == JSONObject.NULL) {
                    return seatTimes;
                }

                JSONArray timeArray = object.getJSONObject("data").getJSONArray("startTimes");
                for (int i = 0; i < timeArray.length(); i++) {
                    SeatTime seatTime = new SeatTime();
                    JSONObject timeObject = timeArray.getJSONObject(i);
                    seatTime.setId(timeObject.getString("id"));
                    seatTime.setValue(timeObject.getString("value"));

                    seatTimes.add(seatTime);
                }
                return seatTimes;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<SeatTime> getSeatEndTime(String data) {
        try {
            JSONObject object = new JSONObject(data);

            if (isRequestSuccessful(object)) {
                List<SeatTime> seatTimes =  new ArrayList<>();
                if (object.getJSONObject("data").get("endTimes") == JSONObject.NULL) {
                    return seatTimes;
                }

                JSONArray timeArray = object.getJSONObject("data").getJSONArray("endTimes");
                for (int i = 0; i < timeArray.length(); i++) {
                    SeatTime seatTime = new SeatTime();
                    JSONObject timeObject = timeArray.getJSONObject(i);
                    seatTime.setId(timeObject.getString("id"));
                    seatTime.setValue(timeObject.getString("value"));

                    seatTimes.add(seatTime);
                }
                return seatTimes;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static InstantReserve getInstantReserve(String data) {
        try {
            JSONObject object = new JSONObject(data);
            if (isRequestSuccessful(object)) {
                InstantReserve reserve = new InstantReserve();
                JSONObject reserveObject = object.getJSONObject("data");
                reserve.setId(reserveObject.getInt("id"));
                reserve.setReceipt(reserveObject.getString("receipt"));
                reserve.setOnDate(reserveObject.getString("onDate"));
                reserve.setBegin(reserveObject.getString("begin"));
                reserve.setEnd(reserveObject.getString("end"));
                reserve.setLocation(reserveObject.getString("location"));
                reserve.setCheckedIn(reserveObject.getBoolean("checkedIn"));

                return reserve;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Seat> getSeatsByTime(String data) {
        try {
            JSONObject object = new JSONObject(data);

            if (isRequestSuccessful(object)) {
                List<Seat> seats =  new ArrayList<>();
                if (object.getJSONObject("data").get("seats") == JSONObject.NULL) {
                    return seats;
                }

                JSONObject layoutObject = object.getJSONObject("data").getJSONObject("seats");
                Iterator<String> iterator = layoutObject.keys();
                while (iterator.hasNext()) {
                    Seat seat = new Seat();
                    String key = iterator.next();
                    JSONObject seatObject = layoutObject.getJSONObject(key);

                    if(seatObject.getString("type").equals("seat")) {

                        seat.setLocation(key);
                        seat.setId(seatObject.getInt("id"));
                        seat.setName(seatObject.getString("name"));
                        seat.setType("seat");
                        seat.setStatus(seatObject.getString("status"));
                        seat.setWindow(seatObject.getBoolean("window"));
                        seat.setPower(seatObject.getBoolean("power"));
                        seat.setComputer(seatObject.getBoolean("computer"));
                        seat.setLocal(seatObject.getBoolean("local"));

                        // 最后把这个位置加入列表
                        seats.add(seat);
                    }
                }

                return seats;
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
