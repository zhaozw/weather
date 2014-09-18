package com.young.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DateUtil {

	public static JSONArray sortJsonArrayByDate(JSONArray dayArray, String key)
			throws NumberFormatException, JSONException, ParseException {
		JSONObject jObject = null;
		JSONArray jsonArrays = dayArray;
		for (int i = 0; i < jsonArrays.length(); i++) {
			for (int j = i + 1; j < jsonArrays.length(); j++) {
				Date l = StringToDate(jsonArrays.getJSONObject(i)
						.getString(key), "yyyy-MM-dd");
				Date nl = StringToDate(
						jsonArrays.getJSONObject(j).getString(key),
						"yyyy-MM-dd");
				if (l.after(nl)) {
					jObject = jsonArrays.getJSONObject(j);
					jsonArrays.put(j, jsonArrays.getJSONObject(i));
					jsonArrays.put(i, jObject);
				}
			}
		}
		return jsonArrays;
	}

	public static JSONObject getSomeDay(JSONArray daysArray, int dis) {
		try {
			for (int i = 0; i < daysArray.length(); i++) {
				int l;
				l = daysBetween(
						new Date(),
						StringToDate(
								daysArray.getJSONObject(i).getString("days"),
								"yyyy-MM-dd"));
				if (l == dis) {
					return daysArray.getJSONObject(i);
				}

			}
			return daysArray != null ? daysArray.getJSONObject(0)
					: new JSONObject();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new JSONObject();
		}

	}

	public static Date StringToDate(String dateStr, String formatStr) {
		DateFormat sdf = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String dateParse(String dateString) {
		String[] dateStrings = dateString.split("-");
		return Integer.parseInt(dateStrings[1]) + "月" + dateStrings[2] + "日";
	}

	public static int daysBetween(Date smdate, Date bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	public static long getTimeMillis(Date mDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(mDate);
		return cal.getTimeInMillis();
	}

}
