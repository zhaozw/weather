package com.young.module.location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import com.google.gson.Gson;
import com.young.MyApplication;
import com.young.common.util.HttpUtil;
import com.young.common.util.L;
import com.young.common.util.SharePreferenceUtil;
import com.young.module.weather.GetWeatherTask;
import com.young.module.weather.MainActivity;
import com.young.sort.list.DragSortListView;
import com.young.sort.list.SimpleDragSortCursorAdapter;
import com.young.modules.R;
import com.young.modules.R.id;
import com.young.modules.R.layout;
import com.young.modules.R.menu;

import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.database.Cursor;
import android.widget.TextView;

public class CityManageActivity extends FragmentActivity {

    private SimpleDragSortCursorAdapter adapter;
    private SharePreferenceUtil mSpUtil;
    
    private List<String> cityList;
    private List<String> oCityList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_manage);
        
        if (mSpUtil == null)
			mSpUtil = new SharePreferenceUtil(this);
        
        String[] cols = {"name"};
        int[] ids = {R.id.text};
        adapter = new MAdapter(this,
                R.layout.city_item_click_remove, null, cols, ids, 0);

        DragSortListView dslv = (DragSortListView) findViewById(android.R.id.list);
        dslv.setAdapter(adapter);
        
        oCityList = loadAllCityFromSharePreference();
        cityList = loadAllCityFromSharePreference();
        loadCity(cityList);
        
        ActionBar bar = getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
    }
    
	public List<String> loadAllCityFromSharePreference() {		
		List<String> citys = new ArrayList<String>();
		try {
			JSONArray cityList = new JSONArray(mSpUtil.getAllCity().toString());
			for(int i=0; i<cityList.length();i++){
				String city = (String) cityList.get(i);
				citys.add(city);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return citys;
	}

	private void loadCity(List<String> cityList){
    	MatrixCursor cursor = new MatrixCursor(new String[] {"_id", "name"});        
        if(null != cityList && cityList.size() > 0){
	    	for (int i = 0; i < cityList.size(); i++) {
	            cursor.newRow()
	                    .add(i)
	                    .add(cityList.get(i));
	        }
        }else{
        	cursor.newRow().add(0).add("请添加城市");
        }
        adapter.changeCursor(cursor);
    }

    private class MAdapter extends SimpleDragSortCursorAdapter {
        private Context mContext;

        public MAdapter(Context ctxt, int rmid, Cursor c, String[] cols, int[] ids, int something) {
            super(ctxt, rmid, c, cols, ids, something);
            mContext = ctxt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            TextView tv = (TextView) v.findViewById(R.id.text);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                	String cityName = (String) ((TextView) v).getText();
                    int cityIndex = cityList.indexOf(cityName);
                    startMainActivity(cityIndex);
                }
            });
            return v;
        }
        
        
        public void doAfterDrop(int from, int to) {
        	//do something after drop
        	L.i("drop from "+ from +" to "+to);
        	String cityFrom = cityList.get(from);
        	String cityTo = cityList.get(to);
        	cityList.set(to, cityFrom);
        	cityList.set(from, cityTo);
        	saveCityChangeToSharePreference(cityList);
        }
        
        
        public void doAfterRemove(int which)  {
        	//do something after remove
        	L.i("remove city " + which);
        	cityList.remove(which);
        	saveCityChangeToSharePreference(cityList);
        }
    }
    
    @Override  
    public boolean onCreateOptionsMenu(Menu menu) {  
        MenuInflater inflater = getMenuInflater();  
        inflater.inflate(R.menu.city, menu);  
        return true;  
    }  
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		
		case android.R.id.home:        
			saveCityChangesToServer();		 
			Intent intent = new Intent(this, MainActivity.class);            
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
			startActivity(intent);       
			return true;    

		case R.id.add_city:
			//TO: open searchCity view
			startSearchActivityForResult();
			return true;
			
		}

		return super.onOptionsItemSelected(item);
	}
    
    private void startSearchActivityForResult() {
		Intent searchCityIntent = new Intent(this, CitySearchActivity.class);
		startActivityForResult(searchCityIntent, 0);
	}
    
    private void startMainActivity(int city) {
		Intent i = new Intent();
		i.putExtra("cityIndex", city);
		setResult(RESULT_OK, i);
		saveCityChangesToServer();
		new GetWeatherTask(null, cityList.get(city)).execute();
		finish();
	}
    
    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  
    {  
    	if (requestCode == 0 && resultCode == RESULT_OK) {
    		String cityname = data.getExtras().getString("city");
    		cityList.add(cityname);
			saveCityChangeToSharePreference(cityList);
			loadCity(cityList);
		}
    }  
    
    private void saveCityChangeToSharePreference(List<String> newCitys) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		String citys = gson.toJson(newCitys);
		L.i(citys);
		mSpUtil.setAllCity(citys);		
	}

	private void saveCityChangesToServer() {
    	if(!oCityList.equals(cityList)){
			List<String> otherCitys = new ArrayList<String>(cityList);
			otherCitys.remove(0);
			Map<String ,Object> rawParams = new HashMap<String, Object>();
			rawParams.put("stay", cityList.get(0));
			rawParams.put("other", otherCitys);
			try {
				HttpUtil.postRequest("http://106.187.94.192/weather/index.php?r=machine/ChangeLocation", rawParams);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
    
//    @Override
//    public void onDestroy() {
//    	
//    }
}
