package com.young.common.adapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.young.common.util.DateUtil;
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

public class FetureWeatherAdapter extends BaseAdapter {

	public static final Map<String, String> WEEK_PARSE = new HashMap<String, String>();

	private JSONArray mFetrueWeathers;
	private LayoutInflater mInflater;
	private Context mContext;
	private final int[] shadeColors = { Color.rgb(103, 172, 250),
			Color.rgb(91, 163, 245), Color.rgb(84, 153, 231),
			Color.rgb(76, 143, 222), Color.rgb(68, 131, 208),
			Color.rgb(60, 121, 200), Color.rgb(52, 111, 190) };

	public FetureWeatherAdapter(Context context, JSONArray fetrueWeathers) {
		mContext = context;
		mFetrueWeathers = fetrueWeathers;

		mInflater = LayoutInflater.from(mContext);
		WEEK_PARSE.put("星期一", "周一");
		WEEK_PARSE.put("星期二", "周二");
		WEEK_PARSE.put("星期三", "周三");
		WEEK_PARSE.put("星期四", "周四");
		WEEK_PARSE.put("星期五", "周五");
		WEEK_PARSE.put("星期六", "周六");
		WEEK_PARSE.put("星期日", "周日");
	}

	@Override
	public int getCount() {
		return mFetrueWeathers.length()-2;
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
		int color_start = shadeColors[arg0 + 1];
		int color_end = shadeColors[arg0];// Color.rgb(91,162,242);
		GradientDrawable grad = new GradientDrawable(// 渐变色
				Orientation.TOP_BOTTOM, new int[] { color_start, color_end });
		convertView.setBackgroundDrawable(grad);
		TextView descTv = (TextView) convertView
				.findViewById(R.id.feture_weather_desc);
		ImageView weatherIv = (ImageView) convertView
				.findViewById(R.id.feture_weather_img);
		TextView dateTv = (TextView) convertView.findViewById(R.id.feture_date);
		TextView tempTv = (TextView) convertView
				.findViewById(R.id.feture_weather_temp);
		int pos = arg0 + 2;

		try {

			String date = mFetrueWeathers.getJSONObject(pos).getString("days");
			String week = mFetrueWeathers.getJSONObject(pos).getString("week");
			String dateDesc = "";
			int tempdays = DateUtil.daysBetween(new Date(),
					DateUtil.StringToDate(date, "yyyy-MM-dd"));
			if (tempdays == 0) {
				dateDesc = "今天";
			} else if (tempdays == 1) {
				dateDesc = "明天";
			} else if (tempdays == 2) {
				dateDesc = "后天";
			} else {
				dateDesc = WEEK_PARSE.get(week);
			}
			descTv.setText(dateDesc
					+ mFetrueWeathers.getJSONObject(pos).getString("weather"));
			dateTv.setText(week + " " + DateUtil.dateParse(date));
			tempTv.setText(mFetrueWeathers.getJSONObject(pos).getString(
					"temp_low")
					+ "℃~"
					+ mFetrueWeathers.getJSONObject(pos)
							.getString("temp_high") + "℃");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}

}
