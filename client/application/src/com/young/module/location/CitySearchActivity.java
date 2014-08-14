package com.young.module.location;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import com.young.common.adapter.HotCityAdapter;
import com.young.common.adapter.SearchCityAdapter;
import com.young.common.util.L;
import com.young.db.CityDB;
import com.young.entity.City;
import com.young.modules.R;
import android.app.ActionBar;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;  
import android.widget.TextView;

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
	private List<City> mCities;
	
	private SearchCityAdapter mSearchCityAdapter;
	private HotCityAdapter mHotCityAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city_search);
		mCityContainer = findViewById(R.id.city_content_container);
		mSearchContainer = findViewById(R.id.search_content_container);
		mCities = new CityDB(this).getAllCity();
		
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
        
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(16.0f);
        textView.setHintTextColor(Color.argb(120, 255, 255, 255));

   
		 //自定义Searchview
		try {	
	        Class<?> argClass=searchView.getClass();
	        //指定某个私有属性
	        Field mSearchHintIconField = argClass.getDeclaredField("mSearchHintIcon");
	        mSearchHintIconField.setAccessible(true);
	        ImageView mSearchHintIcon = (ImageView)mSearchHintIconField.get(searchView);
	        mSearchHintIcon.setImageResource(R.drawable.zoom);
	        
	        Field mCloseButton = argClass.getDeclaredField("mCloseButton");
	        mCloseButton.setAccessible(true);
	        ImageView backView = (ImageView) mCloseButton.get(searchView);
	        backView.setImageResource(R.drawable.search_del);
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
//        //注意mSearchPlate的背景是stateListDrawable(不同状态不同的图片)  所以不能用BitmapDrawable
//        Field ownField = argClass.getDeclaredField("mSearchPlate");
//        //setAccessible 它是用来设置是否有权限访问反射类中的私有属性的，只有设置为true时才可以访问，默认为false
//        ownField.setAccessible(true);
//        View mView = (View) ownField.get(searchView);
//        mView.setBackground(getResources().getDrawable(R.drawable.person_edittext_selector));
        
       
        
        //修改为展开时的搜索图标
//        Field mSearchButton = argClass.getDeclaredField("mSearchButton");
//        mSearchButton.setAccessible(true);
//        ImageView search = (ImageView) mSearchButton.get(mSearchView);
//        search.setImageResource(R.drawable.main_search_selector);
       
        //修改光标
        //指定某个私有属性  
//        Field mQueryTextView = argClass.getDeclaredField("mQueryTextView");
//        mQueryTextView.setAccessible(true);
//        Class<?> mTextViewClass = mQueryTextView.get(mSearchView).getClass().getSuperclass().getSuperclass().getSuperclass();
//        //mCursorDrawableRes光标图片Id的属性 这个属性是TextView的属性，所以要用mQueryTextView（SearchAutoComplete）
//        //的父类（AutoCompleteTextView）的父  类( EditText）的父类(TextView)  
//        Field mCursorDrawableRes = mTextViewClass.getDeclaredField("mCursorDrawableRes");
//        //setAccessible 它是用来设置是否有权限访问反射类中的私有属性的，只有设置为true时才可以访问，默认为false
//        mCursorDrawableRes.setAccessible(true);
//        //注意第一个参数持有这个属性(mQueryTextView)的对象(mSearchView) 光标必须是一张图片不能是颜色，因为光标有两张图片，
//        //一张是第一次获得焦点的时候的闪烁的图片，一张是后边有内容时候的图片，如果用颜色填充的话，就会失去闪烁的那张图片，
//        //颜色填充的会缩短文字和光标的距离（某些字母会背光标覆盖一部分）。
//        mCursorDrawableRes.set(mQueryTextView.get(mSearchView), R.drawable.divider_selector);  
        
        
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
