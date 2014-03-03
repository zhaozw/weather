package com.young.view;

import com.young.sort.list.DragSortListView;
import com.young.sort.list.SimpleDragSortCursorAdapter;

import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.database.Cursor;
import android.widget.Toast;

public class CityManageActivity extends FragmentActivity {

    private SimpleDragSortCursorAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_manage);

        String[] cols = {"name"};
        int[] ids = {R.id.text};
        adapter = new MAdapter(this,
                R.layout.city_item_click_remove, null, cols, ids, 0);

        DragSortListView dslv = (DragSortListView) findViewById(android.R.id.list);
        dslv.setAdapter(adapter);

        MatrixCursor cursor = new MatrixCursor(new String[] {"_id", "name"});
        String[] artistNames = getResources().getStringArray(R.array.city_names);
        for (int i = 0; i < artistNames.length; i++) {
            cursor.newRow()
                    .add(i)
                    .add(artistNames[i]);
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
            View tv = v.findViewById(R.id.text);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "city clicked", Toast.LENGTH_SHORT).show();
                }
            });
            return v;
        }
    }
}
