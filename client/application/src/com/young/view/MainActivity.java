package com.young.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

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

import com.young.app.Application;
import com.young.util.L;
import com.young.view.R;
import com.young.adapter.CityPagerAdapter;
import com.young.client.WeatherClient;
import com.young.tab.slide.PagerSlidingTabStrip;

public class MainActivity extends FragmentActivity {

	private final Handler handler = new Handler();

	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private CityPagerAdapter adapter;

	private Drawable oldBackground = null;
	private int baseColor = 0xFF96AA39;

	private Application mApplication;
	private List<String> citys = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mApplication = Application.getInstance();

		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		L.i("");
		citys = mApplication.loadAllCityFromSharePreference();
		//test data
		if(citys == null || citys.size() == 0){
			citys.add("test1");
			citys.add("test2");
			citys.add("test3");
		}
		adapter = new CityPagerAdapter(getSupportFragmentManager(), citys);

		pager.setAdapter(adapter);

		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
				.getDisplayMetrics());
		pager.setPageMargin(pageMargin);

		tabs.setViewPager(pager);

		setBaseColor(baseColor);

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
			WeatherClient wClient = new WeatherClient("±±¾©");
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
}