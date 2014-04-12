package com.young.common.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceUtil {
	private static final String CITY_SHAREPRE_FILE = "city";
	private static final String MAIN_CITY = "main_city";
	private static final String CITYS_JSON_STRING = "all_city";
	private static final String WEATHERS_JSON_STRING = "all_weather";
	private static final String TIME = "time";
	private static final String VERSION = "version";
	
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;

	public SharePreferenceUtil(Context context) {
		sp = context.getSharedPreferences(CITY_SHAREPRE_FILE, Context.MODE_PRIVATE);
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
		return sp.getString(CITYS_JSON_STRING, "[]");
	}
	
	//all weather
	public void setAllWeather(String weathers) {
		editor.putString(WEATHERS_JSON_STRING, weathers);
		editor.commit();
	}
	
	public String getAllWeather() {
		return sp.getString(WEATHERS_JSON_STRING, "[]");
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
