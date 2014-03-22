package com.young.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.young.bean.City;
import com.young.view.R;

public class HotCityAdapter extends BaseAdapter {

	private List<String> mHotCities;
	private LayoutInflater mInflater;
	private Context mContext;

	// private String mFilterStr;

	public HotCityAdapter(Context context, ArrayList<String> hotCities) {
		mContext = context;
		mHotCities = hotCities;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mHotCities.size();
	}

	@Override
	public String getItem(int position) {
		return mHotCities.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.hot_city_item, null);
		}
		TextView cityTv = (TextView) convertView
				.findViewById(R.id.ItemText);
		cityTv.setText(mHotCities.get(position));
		return convertView;
	}

}

