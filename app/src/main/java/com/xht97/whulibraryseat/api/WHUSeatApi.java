package com.xht97.whulibraryseat.api;

public class WHUSeatApi {

    // BASE API URL
    public static final String BASE_SEAT_API = "https://seat.lib.whu.edu.cn:8443";

    // USER
    public static final String USER_LOGIN = BASE_SEAT_API + "/rest/auth?"; // username=&password=
    public static final String USER_INFO = BASE_SEAT_API + "/rest/v2/user";
    public static final String USER_RESERVE = BASE_SEAT_API + "/rest/v2/user/reservations";
    public static final String USER_RESERVE_HISTORY = BASE_SEAT_API + "/rest/v2/history/1/"; // 30
    public static final String USER_VIOLATION_HISTORY = BASE_SEAT_API + "/rest/v2/violations";

    // SEAT SUM DATA
    public static final String BUILDING_INFO = BASE_SEAT_API + "/rest/v2/free/filters";
    public static final String BUILDING_STATUS = BASE_SEAT_API + "/rest/v2/room/stats2/"; // 1-4

    // RESERVE SEAT
    public static final String ROOM_STATUS = BASE_SEAT_API + "/rest/v2/room/layoutByDate"; // /60/2019-01-19
    public static final String SELECT_SEAT = BASE_SEAT_API + "/rest/v2/searchSeats"; // /2019-01-19/1200/1320

    public static final String SEAT_START_TIME = BASE_SEAT_API + "/rest/v2/startTimesForSeat"; // /35117/2019-01-19
    public static final String SEAT_END_TIME = BASE_SEAT_API + "/rest/v2/endTimesForSeat"; // /35117/2018-12-28/now

    public static final String RESERVE = BASE_SEAT_API + "/rest/v2/freeBook"; // POST
    public static final String CANCEL = BASE_SEAT_API + "/rest/v2/cancel/"; // 拼接预约ID
    public static final String STOP = BASE_SEAT_API + "/rest/v2/stop";
    public static final String CHECKIN = BASE_SEAT_API + "/rest/v2/checkIn";
    public static final String LEAVE = BASE_SEAT_API + "/rest/v2/leave";

}
