package com.young.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceUtil {
	public static final String CITY_SHAREPRE_FILE = "city";
	private static final String MAIN_CITY = "main_city";
	private static final String CITYS_JSON_STRING = "all_city";
	private static final String WEATHERS_JSON_STRING = "all_weather";
	private static final String TIMESAMP = "timesamp";
	private static final String TIME = "time";
	private static final String VERSION = "version";
	
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;

	public SharePreferenceUtil(Context context, String file) {
		sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
		editor = sp.edit();
	}

	//main city
	public void setMainCity(String city) {
		editor.putString(MAIN_CITY, city);
		editor.commit();
	}

	public String getMainCity() {
		return sp.getString(MAIN_CITY, "");
	}
	
	//all city
	public void setAllCity(String citys) {
		editor.putString(CITYS_JSON_STRING, citys);
		editor.commit();
	}
	
	public String getAllCity() {
		return sp.getString(CITYS_JSON_STRING, "{allWeather:[{city:'test1'},{city:'test2'}]}");
	}
	
	//all weather
	public void setAllWeather(String weathers) {
		editor.putString(WEATHERS_JSON_STRING, weathers);
		editor.commit();
	}
	
	public String getAllWeather() {
		return sp.getString(WEATHERS_JSON_STRING, "");
	}

	// timesamp
	public void setTimeSamp(long time) {
		editor.putLong(TIMESAMP, time);
		editor.commit();
	}

	public long getTimeSamp() {
		return sp.getLong(TIMESAMP, System.currentTimeMillis());
	}

	// time
	public void setTime(String time) {
		editor.putString(TIME, time);
		editor.commit();
	}

	public String getTime() {
		return sp.getString(TIME, "");
	}

	// database
	public void setVersion(int version) {
		editor.putInt(VERSION, version);
		editor.commit();
	}

	public int getVersion() {
		return sp.getInt(VERSION, -1);
	}
}
