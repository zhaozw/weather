package com.young.common.adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.young.modules.R;

import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FetureWeatherAdapter extends BaseAdapter{
	

	private JSONArray mFetrueWeathers;
	private LayoutInflater mInflater;
	private Context mContext;
	private final int[] shadeColors = {Color.rgb(103,172,250),Color.rgb(91,163,245),Color.rgb(84,153,231),Color.rgb(76,143,222),Color.rgb(68,131,208)};

	public FetureWeatherAdapter(Context context, String fetrueWeathers) {
		mContext = context;
		try {
			mFetrueWeathers = new JSONArray(fetrueWeathers);
		} catch (JSONException e) {
			e.printStackTrace();
		}
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

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.weather_feture_item, null);
		}
		int color_start = shadeColors[arg0+1];
		int color_end = shadeColors[arg0];//Color.rgb(91,162,242);
		GradientDrawable grad = new GradientDrawable(//渐变色  
	            Orientation.TOP_BOTTOM,  
	            new int[]{color_start, color_end}  
	        ); 
		convertView.setBackgroundDrawable(grad);
		TextView descTv = (TextView) convertView.findViewById(R.id.feture_weather_desc);
		ImageView weatherIv = (ImageView) convertView.findViewById(R.id.feture_weather_img);
		TextView dateTv = (TextView) convertView.findViewById(R.id.feture_date);
		TextView tempTv = (TextView) convertView.findViewById(R.id.feture_weather_temp);
		
		try {
			descTv.setText(mFetrueWeathers.getJSONObject(arg0).getString("weather_desc"));
			dateTv.setText(mFetrueWeathers.getJSONObject(arg0).getString("weather_date"));
			tempTv.setText(mFetrueWeathers.getJSONObject(arg0).getString("weather_temp"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}

}
