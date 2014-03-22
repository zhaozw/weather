package com.young.view;

import java.util.ArrayList;
import java.util.List;
import com.young.adapter.SearchCityAdapter;
import com.young.adapter.HotCityAdapter;
import com.young.app.Application;
import com.young.bean.City;
import com.young.util.L;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SearchView;  

public class CitySearchActivity extends FragmentActivity 
	implements SearchView.OnQueryTextListener {
	
	ArrayList<String> mCitysList = new ArrayList<String>(); 
	ArrayList<String> mHotCitysList = new ArrayList<String>(); 
	GridView hotCityListView;
	ListView searchResultView;
	SearchView searchView; 
	private View mCityContainer;
	private View mSearchContainer;
	Object[] hotCitys;  
	private Application mApplication;
	private List<City> mCities;
	
	private SearchCityAdapter mSearchCityAdapter;
	private HotCityAdapter mHotCityAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city_search);
		mCityContainer = findViewById(R.id.city_content_container);
		mSearchContainer = findViewById(R.id.search_content_container);
		
		hotCityListView = (GridView) findViewById(R.id.hotCitys); 
		hotCityListView
			.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				L.i(mHotCityAdapter.getItem(position));
				startManageActivity(mHotCityAdapter.getItem(position));
			}
		});
		searchResultView = (ListView) findViewById(R.id.search_list);
		searchResultView
			.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				L.i(mSearchCityAdapter.getItem(position).toString());
				startManageActivity(mSearchCityAdapter.getItem(position).getName());
			}
		});
		
		mApplication = Application.getInstance();
				
		mHotCitysList = loadHotCityData();
		mHotCityAdapter = new HotCityAdapter(CitySearchActivity.this,
				mHotCitysList);
        hotCityListView.setAdapter(mHotCityAdapter);  
               
        ActionBar bar = getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
	}
	
	private void startManageActivity(String city) {
		Intent i = new Intent();
		i.putExtra("city", city);
		setResult(RESULT_OK, i);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.city_search, menu);
        //search.collapseActionView();
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_city_search).getActionView();
        SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(info);
        
        searchView.setIconifiedByDefault(false); 
        searchView.setOnQueryTextListener(this);  
        searchView.setSubmitButtonEnabled(false); 
        return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		
		case android.R.id.home:            
	         Intent intent = new Intent(this, CityManageActivity.class);            
	         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
	         startActivity(intent);            
	         return true;    
			
		}

		return super.onOptionsItemSelected(item);
	}
	
	public ArrayList<String> loadHotCityData() {
		String[] hotCitys = getResources().getStringArray(R.array.all_citys);
		ArrayList<String> hotCityList = new ArrayList<String>();
        for (int i = 0; i < hotCitys.length; i++) {
        	hotCityList.add(hotCitys[i]);  
        }
		return hotCityList;
	}
	
	@Override
	public boolean onQueryTextChange(String newText) {
		mSearchCityAdapter = new SearchCityAdapter(CitySearchActivity.this,
				mCities);
		searchResultView.setAdapter(mSearchCityAdapter);
		searchResultView.setTextFilterEnabled(true);
		if (mCities.size() < 1 || TextUtils.isEmpty(newText)) {
			mCityContainer.setVisibility(View.VISIBLE);
			searchResultView.setVisibility(View.INVISIBLE);
		} else {
			mCityContainer.setVisibility(View.INVISIBLE);
			searchResultView.setVisibility(View.VISIBLE);
			mSearchCityAdapter.getFilter().filter(newText);
		}
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		return false;
	}
 

}
