package com.young.modules.location;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;
import com.young.common.adapter.HotCityAdapter;
import com.young.common.adapter.SearchCityAdapter;
import com.young.common.util.L;
import com.young.common.util.SharePreferenceUtil;
import com.young.common.util.T;
import com.young.common.view.ProgersssDialog;
import com.young.db.CityDB;
import com.young.entity.City;
import com.young.modules.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;  
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class CitySearchActivity extends FragmentActivity 
	implements SearchView.OnQueryTextListener {
	
	public static final int UPDATE_CITY_SCUESS = 3;
	public static final int UPDATE_CITY_FAIL = 4;
	public static final int UPDATE_CITY_REPEAT = 2;
	public static final int RESULT_REPEAT = 11;
	
	private Context mContext;
	
	ArrayList<String> mCitysList = new ArrayList<String>(); 
	ArrayList<String> mHotCitysList = new ArrayList<String>(); 
	GridView hotCityListView;
	ListView searchResultView;
	SearchView searchView; 
	private View mCityContainer;
	Object[] hotCitys;  
	private List<City> mCities;
	private ArrayList<City> hotCities;
	
	private SearchCityAdapter mSearchCityAdapter;
	private HotCityAdapter mHotCityAdapter;
	private SharePreferenceUtil mSpUtil;
	private ProgersssDialog loadingDialog = null;
	private ChangeMyCitiesTask changeMyCitiesTask;
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPDATE_CITY_SCUESS:
				loadingDialog.dismiss();
				startManageActivity();
				break;
			case UPDATE_CITY_REPEAT:
				Intent i = new Intent();
				setResult(RESULT_REPEAT, i);
				finish();
				break;
			case UPDATE_CITY_FAIL:
				loadingDialog.dismiss();
				Toast.makeText(mContext, "添加城市失败，请检查你的网络", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		if (mSpUtil == null)
			mSpUtil = new SharePreferenceUtil(this);
		loadingDialog = new ProgersssDialog(CitySearchActivity.this);
		
		setContentView(R.layout.activity_city_search);
		mCityContainer = findViewById(R.id.city_content_container);
		mCities = new CityDB(this).getAllCity();
		
		hotCityListView = (GridView) findViewById(R.id.hotCitys); 
		hotCityListView
			.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(mHotCityAdapter.getItem(position) == null){
					Toast.makeText(mContext, "城市定位中...", Toast.LENGTH_SHORT).show();
					return;
				}
				List<City> cityList =  loadAllCityFromSharePreference();
				if(cityList.contains(mHotCityAdapter.getItem(position))){
					handler.sendEmptyMessage(UPDATE_CITY_REPEAT);
				}
				else{
					loadingDialog.setMsg("正在添加城市...");
					loadingDialog.show();
					changeMyCitiesTask = new ChangeMyCitiesTask(handler, mHotCityAdapter.getItem(position), "InsLocation/merge");
					changeMyCitiesTask.execute();
				}				
			}
		});
		searchResultView = (ListView) findViewById(R.id.search_list);
		searchResultView
			.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				L.i(mSearchCityAdapter.getItem(position).toString());
				loadingDialog.setMsg("正在添加城市...");
				loadingDialog.show();
				changeMyCitiesTask = new ChangeMyCitiesTask(handler, mSearchCityAdapter.getItem(position), "InsLocation/merge");
				changeMyCitiesTask.execute();
			}
		});
        
        hotCities = buildHotCity();
        mHotCityAdapter = new HotCityAdapter(CitySearchActivity.this,hotCities);
        hotCityListView.setAdapter(mHotCityAdapter); 
               
        ActionBar bar = getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	private void startManageActivity() {
		Intent i = new Intent();
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
	
	public ArrayList<City> buildHotCity() {
		ArrayList<City> hotCityList = new ArrayList<City>();
		SharePreferenceUtil sp = new SharePreferenceUtil(this);
		City lbs = null;
		
		try {
			String lbstr = sp.getLBS();
			JSONObject lbsObj;
			lbsObj = new JSONObject(lbstr);
			String city = lbsObj.getString("City");
			city = city.substring(0,city.length()-1);//去除最后一个“市”
			String district = lbsObj.getString("District");
			
			CityDB cityDB = new CityDB(this);
			lbs = cityDB.getCity(district);
			if(lbs == null){
				lbs = cityDB.getCity(city);
			}
			L.i(city+":"+district);
		} catch (JSONException e) {
			e.printStackTrace();
			L.i("定位城市失败！");
		}
		hotCityList.add(lbs);
		hotCityList.add(new City("上海","上海","101020100","shanghai","sh"));
		hotCityList.add(new City("北京","北京","101010100","beijing","bj"));
		hotCityList.add(new City("广东","广州","101280101","guangzhou","gz"));
		hotCityList.add(new City("广东","深圳","101280601","shenzhen","sz"));
		hotCityList.add(new City("湖北","武汉","101200101","wuhan","wh"));
		hotCityList.add(new City("江苏","南京","101190101","nanjing","nj"));
		hotCityList.add(new City("陕西","西安","101110101","xian","xa"));
		hotCityList.add(new City("四川","成都","101270101","chengdou","cd"));
		hotCityList.add(new City("浙江","杭州","101210101","hangzhou","hz"));
		hotCityList.add(new City("河南","郑州","101180101","zhengzhou","zz"));
		hotCityList.add(new City("重庆","重庆","101040100","chongqing","cq"));
		hotCityList.add(new City("辽宁","沈阳","101070101","shenyang","sy"));
		hotCityList.add(new City("黑龙江","哈尔滨","101050101","haerbin","heb"));
		hotCityList.add(new City("湖南","长沙","101250101","changsha","cs"));
		hotCityList.add(new City("江苏","苏州","101190401","suzhou","sz"));
		hotCityList.add(new City("辽宁","大连","101070201","dalian","dl"));
		hotCityList.add(new City("天津","天津","101030100","tianjin","tj"));
		return hotCityList;
	}
	
	public ArrayList<String> loadHotCityData() {
		String[] hotCitys = getResources().getStringArray(R.array.all_citys);
		ArrayList<String> hotCityList = new ArrayList<String>();
        for (int i = 0; i < hotCitys.length; i++) {
        	hotCityList.add(hotCitys[i]);  
        }
		return hotCityList;
	}
	
    public List<City> loadAllCityFromSharePreference() {		
		List<City> citys = new ArrayList<City>();
		try {
			Gson gson = new Gson();
			Type lt=new TypeToken<List<City>>(){}.getType();
			citys=gson.fromJson(mSpUtil.getAllCity().toString(),lt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return citys;
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
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 
        if (keyCode == KeyEvent.KEYCODE_BACK
                 && event.getRepeatCount() == 0) {
        	System.out.println("aa--");
        	if(loadingDialog.isShowing()){
        		System.out.println("hh--");
        		changeMyCitiesTask.cancel(true);
        		return false;
        	}        
         }
         return super.onKeyDown(keyCode, event);
     }
 

}
