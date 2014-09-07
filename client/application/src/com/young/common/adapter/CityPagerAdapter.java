package com.young.common.adapter;

import java.util.ArrayList;
import java.util.List;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;
import com.young.entity.City;
import com.young.module.weather.WeatherFragment;

public class CityPagerAdapter extends FragmentPagerAdapter {

	private List<City> CITYS = new ArrayList<City>();

	public CityPagerAdapter(FragmentManager fm, List<City> citys) {
		super(fm);
		this.CITYS = citys;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return CITYS.get(position).getName();
	}

	@Override
	public int getCount() {
		return CITYS.size();
	}

	@Override
	public Fragment getItem(int position) {
		WeatherFragment fragment = new WeatherFragment();
		return fragment;
	}

	@Override
	 public Object instantiateItem(ViewGroup container, int position) {
		 WeatherFragment f = (WeatherFragment) super.instantiateItem(container, position);
	     String title = CITYS.get(position).getName();
	     f.setPosition(position);
	     f.setPositionTitle(title);
	     return f;
	 }
	
	@Override
	public int getItemPosition(Object object) {
		return PagerAdapter.POSITION_NONE;
	}

}
