package com.test.BTClient;

public class Config {
	public static final String madaiBaseUrl = "http://madaitech.com/user.json";
	public static final String apiPrefix = "http://api.dotide.com/v1/tempmonitor";
	public static final String global_write_key = "bc76c8b3b24b9e10c38b57478cb954906ab3e2c7315ed84dde45f7385e11b880";
	
	public static final String STEPS = "steps";
	public static final String DISTANCE = "distance";
	public static final String CALORIES = "fat";
	public static final String RUN_DURATION = "duration_max";
	public static final String TOTAL_DURATION = "duration_total";
	
	public static final int LOGIN_SUCCESS = 1;
	public static final int AUTHOR_ILLEGAL = 2;
	public static final int CONNECTION_EOOR = 3;
	public static final int LOGOUT_SUCCESS = 4;
	public static final int GET_PROPERTIES_SUCCESS = 5;
	public static final int GET_SPORT_INFO_SUCCESS = 6;
	public static final int UPLOAD_SPORT_INFO_SUCCESS = 7;
	public static final int GET_USER_INFO_SUCCESS = 8;
	public static final int UPDATE_USER_INFO_SUCCESS = 9;
}
