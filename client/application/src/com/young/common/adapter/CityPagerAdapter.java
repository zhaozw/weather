package com.young.common.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.young.common.util.L;
import com.young.entity.City;
import com.young.module.weather.WeatherInfoFragment;

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
		return WeatherInfoFragment.newInstance(position, getPageTitle(position).toString());
	}
	
	@Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}


