package com.young.view;

import java.util.ArrayList;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.SearchView;  

public class CitySearchActivity extends FragmentActivity 
	implements SearchView.OnQueryTextListener {
	
	ArrayList<String> mCitysList = new ArrayList<String>();  
	GridView hotCityListView;
	SearchView searchView; 
	Object[] citys;  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_search);
		hotCityListView = (GridView) findViewById(R.id.hotCitys); 
		
		initActionbar();
		
//		ArrayList<HashMap<String, Object>> lstCityItem = new ArrayList<HashMap<String, Object>>();
		citys = loadCityData();
        
//        SimpleAdapter saImageItems = new SimpleAdapter(this, 
//                lstCityItem,   
//                R.layout.hot_city_item,                   
//                new String[] {"ItemText"},                    
//                new int[] {R.id.ItemText});  

//        hotCityListView.setAdapter(saImageItems); 
        hotCityListView.setAdapter(new ArrayAdapter<Object>(getApplicationContext(),  
        		android.R.layout.simple_expandable_list_item_1, citys));  
        //hotCityListView.setTextFilterEnabled(true);  
        searchView.setOnQueryTextListener(this);  
        searchView.setSubmitButtonEnabled(false); 
        
        ActionBar bar = getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
	}
	
	public void initActionbar() {  
        // 自定义标题栏  
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
        searchView = (SearchView) mTitleView.findViewById(R.id.search_view);  
    }  

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.city_search, menu);
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
		String[] hotCitys = getResources().getStringArray(R.array.all_citys);
        for (int i = 0; i < hotCitys.length; i++) {
        	mCitysList.add(hotCitys[i]);  
        }
		return mCitysList.toArray();
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		Object[] obj = searchItem(newText);  
        updateLayout(obj);  
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
            // 存在匹配的数据  
            if (index != -1) {  
                mSearchList.add(mCitysList.get(i));  
            }  
        }  
        return mSearchList.toArray();  
    }  
  
    public void updateLayout(Object[] obj) {  
    	hotCityListView.setAdapter(new ArrayAdapter<Object>(getApplicationContext(),  
    			android.R.layout.simple_expandable_list_item_1, obj));  
    }  
 

}
