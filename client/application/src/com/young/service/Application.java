package com.young.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import android.os.Environment;
import com.young.bean.City;
import com.young.db.CityDB;
import com.young.service.Application;
import com.young.util.L;
import com.young.util.NetUtil;
import com.young.util.SharePreferenceUtil;
import com.young.util.T;

public class Application extends android.app.Application {
	public static final int CITY_LIST_SCUESS = 100;
	private static Application mApplication;
	private CityDB mCityDB;
	private List<City> mCityList;
	private SharePreferenceUtil mSpUtil;
	public static int mNetWorkState;

	public static synchronized Application getInstance() {
		return mApplication;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = this;
		mCityDB = openCityDB();// 这个必须最先复制完,所以我放在单线程中处理,待优化
		initData();
	}

	@Override
	public void onTerminate() {
		L.i("Application onTerminate...");
		super.onTerminate();
		if (mCityDB != null && mCityDB.isOpen())
			mCityDB.close();
	}

	// 当程序在后台运行时，释放这部分最占内存的资源
	public void free() {
		mCityList = null;
		System.gc();
	}

	public void initData() {
		mNetWorkState = NetUtil.getNetworkState(this);
		initCityList();
		mSpUtil = new SharePreferenceUtil(this,
				SharePreferenceUtil.CITY_SHAREPRE_FILE);
	}

	private CityDB openCityDB() {
		String path = "/data"
				+ Environment.getDataDirectory().getAbsolutePath()
				+ File.separator + "com.young.view" + File.separator
				+ CityDB.CITY_DB_NAME;
		File db = new File(path);
		if (!db.exists() || getSharePreferenceUtil().getVersion() < 0) {
			L.i("db is not exists");
			try {
				InputStream is = getAssets().open(CityDB.CITY_DB_NAME);
				FileOutputStream fos = new FileOutputStream(db);
				int len = -1;
				byte[] buffer = new byte[1024];
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
					fos.flush();
				}
				fos.close();
				is.close();
				getSharePreferenceUtil().setVersion(1);// 用于管理数据库版本，如果数据库有重大更新时使用
			} catch (IOException e) {
				e.printStackTrace();
				T.showLong(mApplication, e.getMessage());
				System.exit(0);
			}
		}
		return new CityDB(this, path);
	}
	
	private void initCityList() {	
		new Thread(new Runnable() {
			@Override
			public void run() {
				prepareCityList();
			}
		}).start();
	}

	private boolean prepareCityList() {
		mCityList = new ArrayList<City>();
		mCityList = mCityDB.getAllCity();// 获取数据库中所有城市
		return true;
	}

	public synchronized CityDB getCityDB() {
		if (mCityDB == null || !mCityDB.isOpen())
			mCityDB = openCityDB();
		return mCityDB;
	}

	public synchronized SharePreferenceUtil getSharePreferenceUtil() {
		if (mSpUtil == null)
			mSpUtil = new SharePreferenceUtil(this,
					SharePreferenceUtil.CITY_SHAREPRE_FILE);
		return mSpUtil;
	}
	
	public List<City> getCityList() {
		return mCityList;
	}
}
