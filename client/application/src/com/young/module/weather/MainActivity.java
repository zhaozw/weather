package com.young.module.weather;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import com.young.modules.R;
import com.young.common.CommonData;
import com.young.common.adapter.CityPagerAdapter;
import com.young.common.util.DeviceUtil;
import com.young.common.util.SharePreferenceUtil;
import com.young.common.view.ProgersssDialog;
import com.young.common.view.RotateImageView;
import com.young.db.CityDB;
import com.young.entity.City;
import com.young.module.location.ChangeMyCitiesTask;
import com.young.module.location.CityManageActivity;
import com.young.module.setting.SettingsActivity;
import com.young.tab.slide.PagerSlidingTabStrip;

public class MainActivity extends FragmentActivity {

	public static final int UPDATE_WEATHER_SCUESS = 1;
	public static final int UPDATE_WEATHER_FAIL = 0;
	public static final int LOCATION_CITY_SCUESS = 3;
	public static final int LOCATION_CITY_FAIL = 4;
	public static SharePreferenceUtil mSpUtil;

	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private CityPagerAdapter adapter;
	// private int baseColor = 0xFF96AA39;

	private List<City> citys = new ArrayList<City>();
	protected MenuItem refreshItem;
	protected RotateImageView refreshActionView;
	private int currentItem = 0;
	private Context mContext;
	private ProgersssDialog loadingDialog = null;
	private View emptyView;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPDATE_WEATHER_SCUESS:
				buildAdapter();
				buildPager(currentItem);
				tabs.setViewPager(pager);
				hideRefreshAnimation();
				loadingDialog.dismiss();
				Toast.makeText(mContext, "天气更新成功", Toast.LENGTH_SHORT).show();
				break;
			case UPDATE_WEATHER_FAIL:
				hideRefreshAnimation();
				loadingDialog.dismiss();
				Toast.makeText(mContext, "天气更新失败", Toast.LENGTH_SHORT).show();
				break;
			case LOCATION_CITY_SCUESS:
				new UpdateAllWeatherTask(handler).execute();
				Toast.makeText(mContext, "定位城市成功", Toast.LENGTH_SHORT).show();
				break;
			case LOCATION_CITY_FAIL:
				loadingDialog.dismiss();
				hideRefreshAnimation();
				Toast.makeText(mContext, "定位城市失败", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		CommonData.setMapData();
		DeviceUtil.DEVICE_ID = DeviceUtil.getDeviceId(getBaseContext(),
				getContentResolver());
		if (mSpUtil == null)
			mSpUtil = new SharePreferenceUtil(this);
		mContext = this;
		setContentView(R.layout.activity_main);
		loadingDialog = new ProgersssDialog(MainActivity.this);
		pager = (ViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		emptyView = (View) findViewById(R.id.empty_city);

		citys = loadAllCityFromSharePreference();
		if (citys == null || citys.size() == 0 || citys.get(0) == null) {
			City lbs = null;
			String lbstr = mSpUtil.getLBS();
			try {
				JSONObject lbsObj;
				lbsObj = new JSONObject(lbstr);
				String city = lbsObj.getString("City");
				city = city.substring(0, city.length() - 1);// 去除最后一个“市”
				String district = lbsObj.getString("District");

				CityDB cityDB = new CityDB(this);
				lbs = cityDB.getCity(district);
				if (lbs == null) {
					lbs = cityDB.getCity(city);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (lbs == null) {
				hideRefreshAnimation();
				emptyView.setVisibility(View.VISIBLE);
			} else {
				loadingDialog.setMsg("正在定位城市...");
				loadingDialog.show();
				new ChangeMyCitiesTask(handler, lbs, "InsLocation/merge")
						.execute();
			}
			return;
		}

		buildAdapter();
		buildPager(0);
		tabs.setViewPager(pager);

		new UpdateAllWeatherTask(handler).execute();

	}

	@Override
	protected void onResume() {
		super.onResume();
		citys = loadAllCityFromSharePreference();
		if (citys != null && citys.size() != 0 && citys.get(0) != null) {
			emptyView.setVisibility(View.INVISIBLE);
			buildAdapter();
			buildPager(currentItem);
			tabs.setViewPager(pager);
		} else {
			hideRefreshAnimation();
		}
		MobclickAgent.onResume(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0 && resultCode == RESULT_OK) {
			int cityIndex = data.getExtras().getInt("cityIndex");
			currentItem = cityIndex;
		}
	}

	private void buildAdapter() {
		citys = loadAllCityFromSharePreference();
		adapter = new CityPagerAdapter(getSupportFragmentManager(), citys);
	}

	private void buildPager(int currentItemNum) {

		pager.setAdapter(adapter);
		pager.setCurrentItem(currentItemNum, false);
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
			Intent settingIntent = new Intent(this, SettingsActivity.class);
			startActivity(settingIntent);
			return true;

		case R.id.action_city:
			startCityManageActivityForResult();
			return true;

		case R.id.action_refresh:
			if (citys == null || citys.size() == 0) {
				Toast.makeText(mContext, "要先添加城市哦~", Toast.LENGTH_SHORT).show();
			} else {
				showRefreshAnimation(item);
				currentItem = pager.getCurrentItem();
				new UpdateAllWeatherTask(handler).execute();
			}
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		citys = loadAllCityFromSharePreference();
		if (citys != null && citys.size() != 0 && citys.get(0) != null) {
			showRefreshAnimation(menu.findItem(R.id.action_refresh));
		}
		return true;
	}

	@SuppressLint("NewApi")
	private void showRefreshAnimation(MenuItem item) {
		hideRefreshAnimation();

		refreshItem = item;
		// 这里使用一个ImageView设置成MenuItem的ActionView，这样我们就可以使用这个ImageView显示旋转动画了
		refreshActionView = (RotateImageView) getLayoutInflater().inflate(
				R.layout.action_view, null);
		// refreshActionView.setImageResource(R.drawable.refresh1);
		refreshItem.setActionView(refreshActionView);

		// 显示刷新动画
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

	public void openCityManageView(View v) {
		startCityManageActivityForResult();
	}

	private void startCityManageActivityForResult() {
		Intent manageCityIntent = new Intent(this, CityManageActivity.class);
		startActivityForResult(manageCityIntent, 0);
	}

	public List<City> loadAllCityFromSharePreference() {
		List<City> citys = new ArrayList<City>();
		try {
			Gson gson = new Gson();
			Type lt = new TypeToken<List<City>>() {
			}.getType();
			citys = gson.fromJson(mSpUtil.getAllCity().toString(), lt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return citys;
	}
}