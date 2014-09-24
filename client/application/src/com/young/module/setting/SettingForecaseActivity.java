package com.young.module.setting;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.young.wheel.scroll.OnWheelScrollListener;

import android.widget.ImageView;
import android.widget.TextView;

import com.young.common.util.L;
import com.young.module.weather.MainActivity;
import com.young.modules.R;
import com.young.wheel.scroll.WheelView;
import com.young.wheel.scroll.adapter.AbstractWheelTextAdapter;

public class SettingForecaseActivity extends Activity {
	
	private WheelView forecaseHourView,forecaseMinuteView;
	private HourAdapter forecaseHourAdapter;
	private MinuteAdapter forecaseMinuteAdapter;
	private String[] hours,minutes;
	private List<String> hourList,minuteList;

	private String hourString;
	private String minuteString;
	private int hourIndex;
	private int minuteIndex;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_forecase);
		ActionBar bar = getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

		ImageView forecase = (ImageView) findViewById(R.id.forecase_btn);
		forecaseHourView = (WheelView) findViewById(R.id.month);
		forecaseMinuteView = (WheelView) findViewById(R.id.day);
		final View timeSettingView = (View) findViewById(R.id.time_setting_view);
		final View timeSettingTitle = (View) findViewById(R.id.time_setting_title);
		
		hours = getResources().getStringArray(R.array.hours);
		minutes = getResources().getStringArray(R.array.minutes);
		hourList = Arrays.asList(hours);
		minuteList = Arrays.asList(minutes);
		
		forecaseHourAdapter = new HourAdapter(this, hourList);
		forecaseMinuteAdapter = new MinuteAdapter(this, minuteList);
		
		forecaseHourView.setViewAdapter(forecaseHourAdapter);
		forecaseMinuteView.setViewAdapter(forecaseMinuteAdapter);
		
		boolean isForecase = MainActivity.mSpUtil.getForecase();
		String forecaseTimeStr = MainActivity.mSpUtil.getForecaseTime();
		try {
			JSONObject forecaseJO = new JSONObject();
			forecaseJO = new JSONObject(forecaseTimeStr);
			hourString = forecaseJO.getString("hour");
			minuteString = forecaseJO.getString("minute");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(!isForecase){
			forecase.setImageResource(R.drawable.kaiqi);
			timeSettingView.setVisibility(View.INVISIBLE);
			timeSettingTitle.setVisibility(View.INVISIBLE);
		}else{
			forecase.setImageResource(R.drawable.guanbi);
			timeSettingView.setVisibility(View.VISIBLE);
			timeSettingTitle.setVisibility(View.VISIBLE);
		}
		
		hourIndex = hourList.indexOf(buildHour(hourString)) != -1 ? hourList.indexOf(buildHour(hourString)) : hourList.indexOf("07");
		forecaseHourView.setCurrentItem(hourIndex);
		minuteIndex = minuteList.indexOf(minuteString) != -1 ? minuteList.indexOf(minuteString) : minuteList.indexOf("30");
		forecaseMinuteView.setCurrentItem(minuteIndex);
		
		forecase.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isForecase = MainActivity.mSpUtil.getForecase();
				if(!isForecase){
					timeSettingView.setVisibility(View.VISIBLE);
					timeSettingTitle.setVisibility(View.VISIBLE);
					((ImageView) v).setImageResource(R.drawable.guanbi);
					MainActivity.mSpUtil.setForecase(true);;
 				}else{
 					timeSettingView.setVisibility(View.INVISIBLE);
 					timeSettingTitle.setVisibility(View.INVISIBLE);
					((ImageView) v).setImageResource(R.drawable.kaiqi);
					MainActivity.mSpUtil.setForecase(false);
				}
			}
		});
		
		forecaseHourView.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				hourIndex = wheel.getCurrentItem();
				String hour = (String) forecaseHourAdapter.getItemText(hourIndex);
				String minute = (String) forecaseMinuteAdapter.getItemText(minuteIndex);
				saveForecaseTime(hour,minute);
			}
		});
		
		forecaseMinuteView.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				minuteIndex = wheel.getCurrentItem();
				String hour = (String) forecaseHourAdapter.getItemText(hourIndex);
				String minute = (String) forecaseMinuteAdapter.getItemText(minuteIndex);
				saveForecaseTime(hour,minute);
			}
		});


	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		
		case android.R.id.home:  
			finish();
			return true;    
		}
		return super.onOptionsItemSelected(item);
	}
	
	private String buildHour(String hour){
		if(hour == null) return "";
		return hour.length() == 1 ? "0"+hour : hour;
	}
	
	private void saveForecaseTime(String hour, String minute){
		Map<String, String> timeMap = new HashMap<String, String>();
		timeMap.put("hour", hour);
		timeMap.put("minute", minute);
		L.i("saveForecaseTime", ""+timeMap.toString());
		MainActivity.mSpUtil.setForecaseTime(timeMap.toString());
	}
	
	private class HourAdapter extends AbstractWheelTextAdapter {
		/**
		 * Constructor
		 */
		public List<String> list;
		protected HourAdapter(Context context,List<String> list) {
			super(context, R.layout.wheel_view_layout, NO_RESOURCE);
			this.list = list;
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);

			TextView textCity = (TextView) view.findViewById(R.id.textView);
			textCity.setText(list.get(index));
			return view;
		}

		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return list.get(index);
		}
	}
	private class MinuteAdapter extends AbstractWheelTextAdapter {
		/**
		 * Constructor
		 */
		public List<String> list;
		protected MinuteAdapter(Context context,List<String> list) {
			super(context, R.layout.wheel_view_layout, NO_RESOURCE);
			this.list = list;
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);

			TextView textCity = (TextView) view.findViewById(R.id.textView);
			textCity.setText(list.get(index));
			return view;
		}

		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return list.get(index);
		}
	}
}
