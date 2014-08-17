package com.young.module.weather;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.young.modules.R;
import com.young.common.adapter.CityPagerAdapter;
import com.young.common.util.DeviceUtil;
import com.young.common.util.SharePreferenceUtil;
import com.young.common.view.RotateImageView;
import com.young.module.location.CityManageActivity;
import com.young.module.setting.SettingsActivity;
import com.young.tab.slide.PagerSlidingTabStrip;

public class MainActivity extends FragmentActivity {

	public static final int UPDATE_WEATHER_SCUESS = 1;
	public static final int UPDATE_WEATHER_FAIL = 0;
	public static SharePreferenceUtil mSpUtil;

	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private CityPagerAdapter adapter;
	// private int baseColor = 0xFF96AA39;
	private ActionBar actionBar;

	private List<String> citys = new ArrayList<String>();
	protected MenuItem refreshItem;
	protected RotateImageView refreshActionView;

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

		actionBar = getActionBar();
		//actionBar.setDisplayShowTitleEnabled(false);
		setContentView(R.layout.activity_main);
		if (mSpUtil == null)
			mSpUtil = new SharePreferenceUtil(this);
		pager = (ViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);

		buildAdapter();
		buildPager(0);
		tabs.setViewPager(pager);

		DeviceUtil.DEVICE_ID = DeviceUtil.getDeviceId(getBaseContext(),
				getContentResolver());

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
		citys = loadAllCityFromSharePreference();
		if (citys.size() == 0)
			citys.add("添加城市");
		adapter = new CityPagerAdapter(getSupportFragmentManager(), citys);
	}

	private void buildPager(int currentItem) {
		pager.setAdapter(adapter);
		pager.setCurrentItem(currentItem, false);
		final int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
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
			// TO: open setting view
			Intent settingIntent = new Intent(this, SettingsActivity.class);
			startActivity(settingIntent);
			return true;

		case R.id.action_city:
			// TO: open city view
			startCityManageActivityForResult();
			return true;

		case R.id.action_refresh:
			// TO: refresh the weather
			showRefreshAnimation(item);
			Toast.makeText(this, "refresh", Toast.LENGTH_SHORT).show();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	
	@SuppressLint("NewApi")
	private void showRefreshAnimation(MenuItem item) {
		hideRefreshAnimation();

		refreshItem = item;
		//这里使用一个ImageView设置成MenuItem的ActionView，这样我们就可以使用这个ImageView显示旋转动画了
		refreshActionView = (RotateImageView) getLayoutInflater().inflate(R.layout.action_view, null);
		//refreshActionView.setImageResource(R.drawable.refresh1);
		refreshItem.setActionView(refreshActionView);
		
		//显示刷新动画
		refreshActionView.startAnim();
	}

	@SuppressLint("NewApi")
	private void hideRefreshAnimation() {
		if (refreshItem != null) {
			View view = refreshItem.getActionView();
			if (view != null) {
				view.clearAnimation();
				refreshItem.setActionView(null);
			}
		}
	}

	private void startCityManageActivityForResult() {
		Intent manageCityIntent = new Intent(this, CityManageActivity.class);
		startActivityForResult(manageCityIntent, 0);
	}

	public List<String> loadAllCityFromSharePreference() {
		List<String> citys = new ArrayList<String>();
		try {
			JSONArray cityList = new JSONArray(mSpUtil.getAllCity().toString());
			for (int i = 0; i < cityList.length(); i++) {
				String city = (String) cityList.get(i);
				citys.add(city);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return citys;
	}
}