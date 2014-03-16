package com.young.view;

import java.util.ArrayList;
import java.util.List;
import com.young.adapter.SearchCityAdapter;
import com.young.bean.City;
import com.young.service.Application;
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
	Object[] citys; 
	Object[] hotCitys;  
	private Application mApplication;
	private List<City> mCities;
	
	private SearchCityAdapter mSearchCityAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city_search);
		mCityContainer = findViewById(R.id.city_content_container);
		mSearchContainer = findViewById(R.id.search_content_container);
		
		hotCityListView = (GridView) findViewById(R.id.hotCitys); 
		searchResultView = (ListView) findViewById(R.id.search_list);
		searchResultView
			.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				L.i(mSearchCityAdapter.getItem(position).toString());
				//startActivity(mSearchCityAdapter.getItem(position));
			}
		});
		
		mApplication = Application.getInstance();
		
		initActionbar();
		
		citys = loadCityData();
		hotCitys = loadHotCityData();
        hotCityListView.setAdapter(new ArrayAdapter<Object>(getApplicationContext(),  
        		android.R.layout.simple_expandable_list_item_1, hotCitys));  
               
        ActionBar bar = getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
	}
	
	public void initActionbar() {  
        getActionBar().setDisplayShowHomeEnabled(true);  
        getActionBar().setDisplayShowTitleEnabled(true);  
        getActionBar().setDisplayShowCustomEnabled(true);  
        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        View mTitleView = mInflater.inflate(R.layout.city_search_action_bar,  
                null);  
        getActionBar().setCustomView(  
                mTitleView,  
                new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT,  
                        LayoutParams.WRAP_CONTENT));  
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

	public Object[] loadCityData() {
		mCities = mApplication.getCityList();
        for (int i = 0; i < mCities.size(); i++) {
        	L.i(mCities.get(i).toString());
        	mCitysList.add(mCities.get(i).getName());  
        }
		return mCitysList.toArray();
	}
	
	public Object[] loadHotCityData() {
		String[] hotCitys = getResources().getStringArray(R.array.all_citys);
        for (int i = 0; i < hotCitys.length; i++) {
        	mHotCitysList.add(hotCitys[i]);  
        }
		return mHotCitysList.toArray();
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
	
	public Object[] searchItem(String name) {  
        ArrayList<String> mSearchList = new ArrayList<String>();  
        for (int i = 0; i < mCitysList.size(); i++) {  
            int index = mCitysList.get(i).indexOf(name);  
            if (index != -1) {  
                mSearchList.add(mCitysList.get(i));  
            }  
        }  
        return mSearchList.toArray();  
    }  
 

}
