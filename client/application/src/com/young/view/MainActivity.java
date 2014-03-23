package com.young.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import com.young.app.Application;
import com.young.util.DeviceUtil;
import com.young.view.R;
import com.young.adapter.CityPagerAdapter;
import com.young.client.HttpUtil;
import com.young.client.WeatherClient;
import com.young.tab.slide.PagerSlidingTabStrip;

public class MainActivity extends FragmentActivity {
	
	public static final int UPDATE_WEATHER_SCUESS = 1;
	public static final int UPDATE_WEATHER_FAIL = 0;

	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private CityPagerAdapter adapter;
	//private int baseColor = 0xFF96AA39;

	private Application mApplication;
	private List<String> citys = new ArrayList<String>();
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPDATE_WEATHER_SCUESS:
				break;
			case UPDATE_WEATHER_FAIL:
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mApplication = Application.getInstance();
		pager = (ViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		
		buildAdapter();
		buildPager(0);
		tabs.setViewPager(pager);

		DeviceUtil.DEVICE_ID = DeviceUtil.getDeviceId(getBaseContext(), getContentResolver());
		
	}
	
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
		if (requestCode == 0 && resultCode == RESULT_OK) {
    		int cityIndex = data.getExtras().getInt("cityIndex");
    		buildAdapter();
    		buildPager(cityIndex);
    		tabs.setViewPager(pager);
		}
    }
	
	private void buildAdapter() {
		citys = mApplication.loadAllCityFromSharePreference();
		//test data
		if(citys == null || citys.size() == 0){
			citys.add("上海");
			citys.add("北京");
		}
		adapter = new CityPagerAdapter(getSupportFragmentManager(), citys);
	}
	
	private void buildPager(int currentItem) {
		pager.setAdapter(adapter);
		pager.setCurrentItem(currentItem, false);
		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
				.getDisplayMetrics());
		pager.setPageMargin(pageMargin);
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
			startCityManageActivityForResult();
			return true;
			
		case R.id.action_refresh:
			//TO: refresh the weather
			//Toast.makeText(this, "refresh", Toast.LENGTH_SHORT).show();
			WeatherClient wClient = new WeatherClient("北京");
			wClient.getWeatherInfo();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	
	private void startCityManageActivityForResult() {
		Intent manageCityIntent = new Intent(this, CityManageActivity.class);
		startActivityForResult(manageCityIntent, 0);
	}
}