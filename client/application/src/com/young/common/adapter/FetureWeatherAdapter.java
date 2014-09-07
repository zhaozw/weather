package com.young.common.adapter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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

	public static final Map<String, String> WEEK_PARSE = new HashMap<String, String>();

	private JSONArray mFetrueWeathers;
	private LayoutInflater mInflater;
	private Context mContext;
	private final int[] shadeColors = 
		{ Color.rgb(62, 174, 250),
			Color.rgb(47, 162, 242), Color.rgb(39, 151, 227),
			Color.rgb(28, 141, 219), Color.rgb(18, 128, 203),
			Color.rgb(19, 118, 186)};

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
		int color_start = shadeColors[arg0 + 1];
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
					+ mFetrueWeathers.getJSONObject(arg0).getString("weather"));
			dateTv.setText(week + " " + DateUtil.dateParse(date));
			tempTv.setText(mFetrueWeathers.getJSONObject(arg0).getString(
					"temp_low")
					+ "° - "
					+ mFetrueWeathers.getJSONObject(arg0)
							.getString("temp_high") + "°");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}

}
