package com.young.modules.location;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import com.young.common.util.L;
import com.young.common.util.SharePreferenceUtil;
import com.young.common.util.T;
import com.young.common.view.ProgersssDialog;
import com.young.entity.City;
import com.young.modules.weather.MainActivity;
import com.young.sort.list.DragSortListView;
import com.young.sort.list.SimpleDragSortCursorAdapter;
import com.young.modules.R;

import android.database.MatrixCursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.database.Cursor;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class CityManageActivity extends FragmentActivity {

	public static final int UPDATE_CITY_SCUESS = 3;
	public static final int UPDATE_CITY_FAIL = 4;
	public static final int RESULT_REPEAT = 11;
	
	private SimpleDragSortCursorAdapter adapter;
	private SharePreferenceUtil mSpUtil;
	private Context mContext;

	private List<City> cityList;
	private int removeCityIndex;
	private ProgersssDialog loadingDialog = null;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPDATE_CITY_SCUESS:
				loadingDialog.dismiss();
				cityList.remove(removeCityIndex);
				saveCityChangeToSharePreference(cityList);
				break;
			case UPDATE_CITY_FAIL:
				loadingDialog.dismiss();
				loadCity(cityList);
				Toast.makeText(mContext, "删除城市失败，请检查网络！", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.city_manage);
		loadingDialog = new ProgersssDialog(CityManageActivity.this);

		if (mSpUtil == null)
			mSpUtil = new SharePreferenceUtil(this);

		String[] cols = { "name" };
		int[] ids = { R.id.text };
		adapter = new MAdapter(this, R.layout.city_item_click_remove, null,
				cols, ids, 0);

		DragSortListView dslv = (DragSortListView) findViewById(android.R.id.list);
		dslv.setAdapter(adapter);

		cityList = loadAllCityFromSharePreference();
		loadCity(cityList);

		ActionBar bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);

	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
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

	private void loadCity(List<City> cityList) {
		MatrixCursor cursor = new MatrixCursor(new String[] { "_id", "name" });
		if (null == cityList || cityList.size() <= 0) {
			cursor.close();
			return;
		}
		for (int i = 0; i < cityList.size(); i++) {
			cursor.newRow().add(i).add(cityList.get(i).getName());
		}
		adapter.changeCursor(cursor);
	}

	private class MAdapter extends SimpleDragSortCursorAdapter {
		private Context mContext;

		public MAdapter(Context ctxt, int rmid, Cursor c, String[] cols,
				int[] ids, int something) {
			super(ctxt, rmid, c, cols, ids, something);
			mContext = ctxt;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View v = super.getView(position, convertView, parent);
			TextView tv = (TextView) v.findViewById(R.id.text);
			tv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					startMainActivity(position);
				}
			});
			return v;
		}

		public void doAfterDrop(int from, int to) {
			// do something after drop
			L.i("drop from " + from + " to " + to);
			City cityFrom = cityList.get(from);
			City cityTo = cityList.get(to);
			cityList.set(to, cityFrom);
			cityList.set(from, cityTo);
			saveCityChangeToSharePreference(cityList);
		}

		public boolean doBeforeRemove(int which) {
			L.i("before remove city :" + cityList.size());
			if (cityList.size() == 1) {
				T.showShort(mContext, "请至少保留一个城市！");
				loadCity(cityList);
				return false;
			} else {
				loadingDialog.setMsg("正在删除城市...");
				loadingDialog.show();
				new ChangeMyCitiesTask(handler, cityList.get(which),
						"ModLocation/delete").execute();
				removeCityIndex = which;
			}
			return true;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.city, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case android.R.id.home:
			Intent i = new Intent();
			i.putExtra("cityIndex", 0);
			setResult(RESULT_OK, i);
			finish();
			return true;

		case R.id.add_city:
			// TO: open searchCity view
			startSearchActivityForResult();
			return true;

		}

		return super.onOptionsItemSelected(item);
	}

	private void startSearchActivityForResult() {
		Intent searchCityIntent = new Intent(this, CitySearchActivity.class);
		startActivityForResult(searchCityIntent, 0);
	}

	private void startMainActivity(int city) {
		Intent i = new Intent();
		i.putExtra("cityIndex", city);
		setResult(RESULT_OK, i);
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0 && resultCode == RESULT_OK) {
			cityList = loadAllCityFromSharePreference();
			loadCity(cityList);
		} else if (requestCode == 0 && resultCode == RESULT_REPEAT) {
			T.showShort(this, "该城市已经在列表当中!");
		}
	}

	private void saveCityChangeToSharePreference(List<City> newCitys) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		String citys = gson.toJson(newCitys);
		L.i("citys", citys);
		MainActivity.mSpUtil.setAllCity(citys);
	}

}
