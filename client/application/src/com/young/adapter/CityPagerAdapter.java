package com.young.adapter;

import java.util.ArrayList;
import java.util.List;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.young.view.WeatherInfoFragment;

public class CityPagerAdapter extends FragmentPagerAdapter {

	private List<String> CITYS = new ArrayList<String>();

	public CityPagerAdapter(FragmentManager fm, List<String> citys) {
		super(fm);
		this.CITYS = citys;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return CITYS.get(position);
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


