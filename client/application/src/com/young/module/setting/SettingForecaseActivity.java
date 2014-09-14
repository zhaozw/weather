package com.young.module.setting;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_forecase);

		ImageView forecase = (ImageView) findViewById(R.id.forecase_btn);
		forecaseHourView = (WheelView) findViewById(R.id.month);
		forecaseMinuteView = (WheelView) findViewById(R.id.day);
		
		hours = getResources().getStringArray(R.array.hours);
		minutes = getResources().getStringArray(R.array.minutes);
		hourList = Arrays.asList(hours);
		minuteList = Arrays.asList(minutes);
		
		forecaseHourAdapter = new HourAdapter(this, hourList);
		forecaseMinuteAdapter = new MinuteAdapter(this, minuteList);
		
		forecaseHourView.setViewAdapter(forecaseHourAdapter);
		forecaseMinuteView.setViewAdapter(forecaseMinuteAdapter);
		
		String forecaseTimeStr = MainActivity.mSpUtil.getForecaseTime();
		
		if(forecaseTimeStr == null || forecaseTimeStr.equals("")){
			forecase.setImageResource(R.drawable.kaiqi);
		}else{
			forecase.setImageResource(R.drawable.guanbi);
			JSONObject forecaseJO = new JSONObject();
			try {
				forecaseJO = new JSONObject(forecaseTimeStr);
				hourString = forecaseJO.getString("hour");
				minuteString = forecaseJO.getString("minute");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		forecase.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				String forecaseTimeStr = MainActivity.mSpUtil.getForecaseTime();
				if(forecaseTimeStr == null || forecaseTimeStr.equals("")){
					((ImageView) v).setImageResource(R.drawable.guanbi);
					Date currentTime = new Date();
					int currentHour = currentTime.getHours();
					int currentminute = currentTime.getMinutes();
					Map<String, String> timeMap = new HashMap<String, String>();
					timeMap.put("hour", ""+currentHour);
					timeMap.put("minute", ""+currentminute);
					L.i("timeMap", ""+timeMap);
					MainActivity.mSpUtil.setForecaseTime(timeMap.toString());
 				}else{
					((ImageView) v).setImageResource(R.drawable.kaiqi);
					MainActivity.mSpUtil.setForecaseTime(null);
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
			}
		});


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
