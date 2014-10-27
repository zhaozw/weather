package com.young.common.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.young.common.CommonData;
import com.young.common.util.DateUtil;
import com.young.modules.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FetureWeatherAdapter extends BaseAdapter {

	private JSONArray mFetrueWeathers;
	private LayoutInflater mInflater;
	private Context mContext;
	private final int[] shadeColors = { Color.rgb(47, 162, 242),
			Color.rgb(39, 151, 227), Color.rgb(28, 141, 219),
			Color.rgb(18, 128, 203), Color.rgb(19, 118, 186),
			Color.rgb(10, 108, 170) };

	public FetureWeatherAdapter(Context context, JSONArray fetrueWeathers) {
		mContext = context;
		mFetrueWeathers = fetrueWeathers;
		mInflater = LayoutInflater.from(mContext);

	}

	@Override
	public int getCount() {
		return mFetrueWeathers.length();
	}

	@Override
	public JSONObject getItem(int arg0) {
		try {
			return (JSONObject) mFetrueWeathers.get(arg0);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	
	private String getSingleWeatherDesc(String weatherAll){
		String[] weathers = weatherAll.split("转|-");
		List<Integer> indexs = new ArrayList<Integer>();
		for(String weather : weathers){
			indexs.add(CommonData.WEATHER_SORTED_LIST.indexOf(weather));			
		}
		int minIndex = Collections.min(indexs);
		return CommonData.WEATHER_SORTED_LIST.get(minIndex);
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.weather_feture_item, null);
		}
		int color_start = shadeColors[arg0];
		convertView.setBackgroundColor(color_start);
		TextView descTv = (TextView) convertView
				.findViewById(R.id.feture_weather_desc);
		ImageView weatherIv = (ImageView) convertView
				.findViewById(R.id.feture_weather_img);
		TextView dateTv = (TextView) convertView.findViewById(R.id.feture_date);
		TextView tempTv = (TextView) convertView
				.findViewById(R.id.feture_weather_temp);

		try {

			String date = mFetrueWeathers.getJSONObject(arg0).getString("days");
			String week = mFetrueWeathers.getJSONObject(arg0).getString("week");
			String weatherDesc = mFetrueWeathers.getJSONObject(arg0).getString(
					"weather");
			String dateDesc = "";
			int tempdays = DateUtil.daysBetween(new Date(),
					DateUtil.StringToDate(date, "yyyy-MM-dd"));
			if (tempdays == -1) {
				dateDesc = "昨天";
			} else if (tempdays == 0) {
				dateDesc = "今天";
			} else if (tempdays == 1) {
				dateDesc = "明天";
			} else if (tempdays == 2) {
				dateDesc = "后天";
			} else {
				dateDesc = CommonData.WEEK_PARSE.get(week);
			}
			String singleWeather = getSingleWeatherDesc(weatherDesc);
			if(weatherDesc.length()<=4){
				descTv.setText(dateDesc + " " + weatherDesc);
			}else{
				descTv.setText(dateDesc + " " + singleWeather);
			}
			dateTv.setText(week + " " + DateUtil.dateParse(date));
			tempTv.setText(mFetrueWeathers.getJSONObject(arg0).getString(
					"temp_low")
					+ "° - "
					+ mFetrueWeathers.getJSONObject(arg0)
							.getString("temp_high") + "°");
			
			if (CommonData.WEATHER_MAP.get(singleWeather) != null) {
				int weatherImgId = (int) CommonData.WEATHER_MAP
						.get(singleWeather);
				weatherIv.setBackgroundResource(weatherImgId);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}

}
