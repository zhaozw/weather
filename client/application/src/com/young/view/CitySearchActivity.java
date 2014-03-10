package com.young.view;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.SimpleAdapter;

public class CitySearchActivity extends FragmentActivity  {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_search);
		GridView hotCityListView = (GridView) findViewById(R.id.hotCitys); 
		
		ArrayList<HashMap<String, Object>> lstCityItem = new ArrayList<HashMap<String, Object>>();
		String[] hotCitys = getResources().getStringArray(R.array.city_names);
        for (int i = 0; i < hotCitys.length; i++) {
        	HashMap<String, Object> map = new HashMap<String, Object>();  
            map.put("ItemText", hotCitys[i]); 
            lstCityItem.add(map);  
        }
        
        SimpleAdapter saImageItems = new SimpleAdapter(this, 
                lstCityItem,   
                R.layout.hot_city_item,                   
                new String[] {"ItemText"},                    
                new int[] {R.id.ItemText});  

        hotCityListView.setAdapter(saImageItems);  
        
        ActionBar bar = getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
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

}
