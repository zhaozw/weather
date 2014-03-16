package com.young.db;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.young.bean.City;

public class CityDB {
	public static final String CITY_DB_NAME = "city.db";
	private static final String CITY_TABLE_NAME = "city";
	private SQLiteDatabase db;

	public CityDB(Context context, String path) {
		db = context.openOrCreateDatabase(path, Context.MODE_PRIVATE, null);
	}

	public boolean isOpen() {
		return db != null && db.isOpen();
	}

	public void close() {
		if (db != null && db.isOpen())
			db.close();
	}

	public List<City> getAllCity() {
		List<City> list = new ArrayList<City>();
		Cursor c = db.rawQuery("SELECT * from " + CITY_TABLE_NAME, null);
		while (c.moveToNext()) {
			String province = c.getString(c.getColumnIndex("province"));
			String city = c.getString(c.getColumnIndex("name"));
			String number = c.getString(c.getColumnIndex("number"));
			String allPY = c.getString(c.getColumnIndex("pinyin"));
			String allFirstPY = c.getString(c.getColumnIndex("py"));
			City item = new City(province, city, number, allPY, allFirstPY);
			list.add(item);
		}
		return list;
	}

	public City getCity(String city) {
		if (TextUtils.isEmpty(city))
			return null;
		City item = getCityInfo(city);//��ȫ������
		if (item == null) {
			item = getCityInfo(parseName(city));//����һ��֮��������
		}
		return item;
	}

	/**
	 * ȥ���л�������
	 * 
	 * @param city
	 * @return
	 */
	private String parseName(String city) {
		city = city.replaceAll("��$", "").replaceAll("��$", "")
				.replaceAll("��$", "");
		return city;
	}

	private City getCityInfo(String city) {
		City item = null;
		Cursor c = db.rawQuery("SELECT * from " + CITY_TABLE_NAME
				+ " where name=?", new String[] { city });
		if (c.moveToFirst()) {
			String province = c.getString(c.getColumnIndex("province"));
			String name = c.getString(c.getColumnIndex("name"));
			String number = c.getString(c.getColumnIndex("number"));
			String allPY = c.getString(c.getColumnIndex("pinyin"));
			String allFirstPY = c.getString(c.getColumnIndex("py"));
			item = new City(province, name, number, allPY, allFirstPY);
		}
		return item;
	}
	
	public Cursor getCityInfoCursor(String city) {
		Cursor c = db.rawQuery("SELECT * from " + CITY_TABLE_NAME
				+ " where name like '% ? %'", new String[] { city });
		return c;
	}
}
