package com.young.module.location;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.young.common.util.L;
import com.young.common.util.SharePreferenceUtil;
import com.young.entity.City;
import com.young.module.weather.MainActivity;
import com.young.sort.list.DragSortListView;
import com.young.sort.list.SimpleDragSortCursorAdapter;
import com.young.modules.R;

import android.database.MatrixCursor;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;

public class CityManageActivity extends FragmentActivity {

	public static final int UPDATE_CITY_SCUESS = 1;
	public static final int UPDATE_CITY_FAIL = 0;
	
    private SimpleDragSortCursorAdapter adapter;
    private SharePreferenceUtil mSpUtil;
    private Context mContext;
    
    private List<City> cityList;
    private int removeCityIndex;
    private int selectCityIndex = 0;
    
    private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPDATE_CITY_SCUESS:
				//startManageActivity();
				cityList.remove(removeCityIndex); 
				saveCityChangeToSharePreference(cityList);
				break;
			case UPDATE_CITY_FAIL:
				cityList.remove(removeCityIndex); 
				saveCityChangeToSharePreference(cityList);
				Toast.makeText(mContext, "update fail", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.city_manage);
        
        if (mSpUtil == null)
			mSpUtil = new SharePreferenceUtil(this);
        
        String[] cols = {"name"};
        int[] ids = {R.id.text};
        adapter = new MAdapter(this,
                R.layout.city_item_click_remove, null, cols, ids, 0);

        DragSortListView dslv = (DragSortListView) findViewById(android.R.id.list);
        dslv.setAdapter(adapter);
        
        cityList = loadAllCityFromSharePreference();
        loadCity(cityList);
        
        ActionBar bar = getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
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

	private void loadCity(List<City> cityList){
    	MatrixCursor cursor = new MatrixCursor(new String[] {"_id", "name"});        
        if(null != cityList && cityList.size() > 0){
	    	for (int i = 0; i < cityList.size(); i++) {
	            cursor.newRow()
	                    .add(i)
	                    .add(cityList.get(i).getName());
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            TextView tv = (TextView) v.findViewById(R.id.text);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startMainActivity(position);
                }
            });
            return v;
        }
        
        
        public void doAfterDrop(int from, int to) {
        	//do something after drop
        	L.i("drop from "+ from +" to "+to);
        	City cityFrom = cityList.get(from);
        	City cityTo = cityList.get(to);
        	cityList.set(to, cityFrom);
        	cityList.set(from, cityTo);
        	saveCityChangeToSharePreference(cityList);
        }
        
        
        public void doAfterRemove(int which)  {
        	//do something after remove
        	L.i("remove city " + which);
        	new ChangeMyCitiesTask(handler, cityList.get(which), "ModLocation/delete").execute();
        	removeCityIndex = which;
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
		Intent i = new Intent(this, MainActivity.class);
		i.putExtra("cityIndex", city);
		setResult(RESULT_OK, i);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		startActivity(i); 
	}
    
    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  
    {  
    	if (requestCode == 0 && resultCode == RESULT_OK) {
//    		String cityname = data.getExtras().getString("city");
//    		cityList.add(cityname);
//			saveCityChangeToSharePreference(cityList);
    		cityList = loadAllCityFromSharePreference();
			loadCity(cityList);
		}
    }  
    
    private void saveCityChangeToSharePreference(List<City> newCitys) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		String citys = gson.toJson(newCitys);
		L.i(citys);
		MainActivity.mSpUtil.setAllCity(citys);		
	}

}
