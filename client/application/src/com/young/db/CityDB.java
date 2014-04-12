package com.young.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.text.TextUtils;
import com.young.common.util.L;
import com.young.common.util.T;
import com.young.config.AppConfig.DBContent;
import com.young.entity.City;

public class CityDB {
	private static final String CITY_TABLE_NAME = "city";
	private SQLiteDatabase db;

	public CityDB(Context context) {
		String path = "/data"
				+ Environment.getDataDirectory().getAbsolutePath()
				+ File.separator + "com.young.modules" + File.separator
				+ DBContent.CITY_DB_NAME;
		File dbFile = new File(path);
		if (!dbFile.exists()) {
			L.i("db is not exists");
			try {
				InputStream is = context.getAssets().open(DBContent.CITY_DB_NAME);
				FileOutputStream fos = new FileOutputStream(dbFile);
				int len = -1;
				byte[] buffer = new byte[1024];
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
					fos.flush();
				}
				fos.close();
				is.close();
				} catch (IOException e) {
				e.printStackTrace();
				T.showLong(context, e.getMessage());
				System.exit(0);
			}
		}
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
		City item = getCityInfo(city);
		if (item == null) {
			item = getCityInfo(parseName(city));
		}
		return item;
	}

	/**

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
