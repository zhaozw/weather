package com.young.view;

import com.young.app.Application;
import com.young.view.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

public class WeatherInfoFragment extends Fragment {

	public static final int GET_WEATHER_SCUESS = 1;
	public static final int GET_WEATHER_FAIL = 0;
	private static final String ARG_POSITION = "position";

	private int position;
	private String weatherInfoString;
	private Application mApplication;
	private TextView v;

	public static WeatherInfoFragment newInstance(int position) {
		WeatherInfoFragment f = new WeatherInfoFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		FrameLayout fl = new FrameLayout(getActivity());
		fl.setLayoutParams(params);

		final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
				.getDisplayMetrics());

		v = new TextView(getActivity());
		params.setMargins(margin, margin, margin, margin);
		v.setLayoutParams(params);
		v.setLayoutParams(params);
		v.setGravity(Gravity.CENTER);
		v.setBackgroundResource(R.drawable.background_card);
		v.setText("CITY " + (position + 1) + "------" + weatherInfoString);

		fl.addView(v);
		return fl;
	}
	
	@Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume
        	weatherInfoString = mApplication.loadWeather(position);
        	//v.setText("WEATHER CHANGED " + (position + 1) + "------" + weatherInfoString);
        } else {
            //相当于Fragment的onPause
        }
    }
	
	private void initData(){
		mApplication = Application.getInstance();
		position = getArguments().getInt(ARG_POSITION);
		weatherInfoString = mApplication.loadWeather(position);
	}

}