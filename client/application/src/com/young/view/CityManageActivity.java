package com.young.view;

import java.util.List;

import com.young.app.Application;
import com.young.app.GetWeatherTask;
import com.young.sort.list.DragSortListView;
import com.young.sort.list.SimpleDragSortCursorAdapter;
import com.young.util.L;

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
    private Application mApplication;
    
    private List<String> cityList;
    private List<String> oCityList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_manage);
        
        mApplication = Application.getInstance();

        String[] cols = {"name"};
        int[] ids = {R.id.text};
        adapter = new MAdapter(this,
                R.layout.city_item_click_remove, null, cols, ids, 0);

        DragSortListView dslv = (DragSortListView) findViewById(android.R.id.list);
        dslv.setAdapter(adapter);
        
        oCityList = mApplication.loadAllCityFromSharePreference();
        cityList = mApplication.loadAllCityFromSharePreference();
        loadCity(cityList);
        
        ActionBar bar = getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
    }
    
    private void loadCity(List<String> cityList){
    	MatrixCursor cursor = new MatrixCursor(new String[] {"_id", "name"});        
        for (int i = 0; i < cityList.size(); i++) {
            cursor.newRow()
                    .add(i)
                    .add(cityList.get(i));
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
        	mApplication.saveCityChangeToSharePreference(cityList);
        }
        
        
        public void doAfterRemove(int which)  {
        	//do something after remove
        	L.i("remove city " + which);
        	cityList.remove(which);
        	mApplication.saveCityChangeToSharePreference(cityList);
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
		new GetWeatherTask(null, cityList.get(city)).execute();;
		finish();
	}
    
    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  
    {  
    	if (requestCode == 0 && resultCode == RESULT_OK) {
    		String cityname = data.getExtras().getString("city");
    		cityList.add(cityname);
			mApplication.saveCityChangeToSharePreference(cityList);
			loadCity(cityList);
		}
    }  
    
    private void saveCityChangesToServer() {
    	if(!oCityList.equals(cityList)){
			//TODO 与服务器交互通知城市变化
			L.i("与服务器交互通知城市变化");
		}
    }
    
//    @Override
//    public void onDestroy() {
//    	
//    }
}
