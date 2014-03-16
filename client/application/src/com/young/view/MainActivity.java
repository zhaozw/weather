package com.young.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.young.view.R;
import com.young.client.WeatherClient;
import com.young.tab.slide.PagerSlidingTabStrip;

public class MainActivity extends FragmentActivity {

	private final Handler handler = new Handler();

	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private MyPagerAdapter adapter;

	private Drawable oldBackground = null;
	private int baseColor = 0xFF96AA39;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new MyPagerAdapter(getSupportFragmentManager());

		pager.setAdapter(adapter);

		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
				.getDisplayMetrics());
		pager.setPageMargin(pageMargin);

		tabs.setViewPager(pager);

		setBaseColor(baseColor);
		
//		String DB_PATH = "/data/data/com.young.db/databases/";
//		String DB_NAME = "test.db";
//
//		// 检查 SQLite 数据库文件是否存在
//		if ((new File(DB_PATH + DB_NAME)).exists() == false) {
//			// 如 SQLite 数据库文件不存在，再检查一下 database 目录是否存在
//			File f = new File(DB_PATH);
//			// 如 database 目录不存在，新建该目录
//			if (!f.exists()) {
//				f.mkdir();
//			}
//
//			try {
//				// 得到 assets 目录下我们实现准备好的 SQLite 数据库作为输入流
//				InputStream is = getBaseContext().getAssets().open(DB_NAME);
//				// 输出流
//				OutputStream os = new FileOutputStream(DB_PATH + DB_NAME);
//
//				// 文件写入
//				byte[] buffer = new byte[1024];
//				int length;
//				while ((length = is.read(buffer)) > 0) {
//					os.write(buffer, 0, length);
//				}
//
//				// 关闭文件流
//				os.flush();
//				os.close();
//				is.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.action_setting:
			//TO: open setting view
			Intent settingIntent = new Intent(this, SettingsActivity.class);
		    startActivity(settingIntent);
			return true;
		
		case R.id.action_city:
			//TO: open city view
			Intent cityIntent = new Intent(this, CityManageActivity.class);
		    startActivity(cityIntent);
			return true;
			
		case R.id.action_refresh:
			//TO: refresh the weather
			//Toast.makeText(this, "refresh", Toast.LENGTH_SHORT).show();
			WeatherClient wClient = new WeatherClient("123");
			wClient.getWeatherInfo();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void setBaseColor(int color) {

		tabs.setIndicatorColor(color);

		// change ActionBar color just if an ActionBar is available
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

			Drawable colorDrawable = new ColorDrawable(color);
			Drawable bottomDrawable = getResources().getDrawable(R.drawable.actionbar_bottom);
			LayerDrawable ld = new LayerDrawable(new Drawable[] { colorDrawable, bottomDrawable });

			if (oldBackground == null) {

				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
					ld.setCallback(drawableCallback);
				} else {
					getActionBar().setBackgroundDrawable(ld);
				}

			} else {

				TransitionDrawable td = new TransitionDrawable(new Drawable[] { oldBackground, ld });

				// workaround for broken ActionBarContainer drawable handling on
				// pre-API 17 builds
				// https://github.com/android/platform_frameworks_base/commit/a7cc06d82e45918c37429a59b14545c6a57db4e4
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
					td.setCallback(drawableCallback);
				} else {
					getActionBar().setBackgroundDrawable(td);
				}

				td.startTransition(200);

			}

			oldBackground = ld;

			// http://stackoverflow.com/questions/11002691/actionbar-setbackgrounddrawable-nulling-background-from-thread-handler
			getActionBar().setDisplayShowTitleEnabled(false);
			getActionBar().setDisplayShowTitleEnabled(true);

		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("baseColor", baseColor);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		baseColor = savedInstanceState.getInt("baseColor");
		setBaseColor(baseColor);
	}

	private Drawable.Callback drawableCallback = new Drawable.Callback() {
		@Override
		public void invalidateDrawable(Drawable who) {
			getActionBar().setBackgroundDrawable(who);
		}

		@Override
		public void scheduleDrawable(Drawable who, Runnable what, long when) {
			handler.postAtTime(what, when);
		}

		@Override
		public void unscheduleDrawable(Drawable who, Runnable what) {
			handler.removeCallbacks(what);
		}
	};

	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = { "上海", "厦门", "乌鲁木齐", "三亚", "拉斯维加斯", "杭州",
				"南京", "武夷山" };

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			return WeatherInfoFragment.newInstance(position);
		}

	}

}