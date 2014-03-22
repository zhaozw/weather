package com.young.view;

import java.util.List;

import com.young.app.Application;
import com.young.sort.list.DragSortListView;
import com.young.sort.list.SimpleDragSortCursorAdapter;

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
import android.widget.Toast;

public class CityManageActivity extends FragmentActivity {

    private SimpleDragSortCursorAdapter adapter;
    private Application mApplication;
    
    private List<String> cityList;

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
                    Toast.makeText(mContext, cityName + "city clicked", Toast.LENGTH_SHORT).show();
                }
            });
            return v;
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
}
