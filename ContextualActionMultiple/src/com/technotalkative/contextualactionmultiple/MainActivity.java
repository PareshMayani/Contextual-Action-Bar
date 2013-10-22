package com.technotalkative.contextualactionmultiple;

import java.util.HashMap;
import java.util.Set;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity {
 
    private String[] data = {"One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine","Ten"};
     
    private SelectionAdapter mAdapter;
     
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         
        mAdapter = new SelectionAdapter(this,
                    R.layout.row_list_item, R.id.textView1, data);
        setListAdapter(mAdapter);
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
         
        getListView().setMultiChoiceModeListener(new MultiChoiceModeListener() {
             
            private int nr = 0;
             
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                return false;
            }
             
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // TODO Auto-generated method stub
                 mAdapter.clearSelection();
            }
             
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                 
                nr = 0;
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.contextual_menu, menu);
                return true;
            }
             
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // TODO Auto-generated method stub
                switch (item.getItemId()) {
                 
                    case R.id.item_delete:
                        nr = 0;
                        mAdapter.clearSelection();
                        mode.finish();
                }
				return false;
            }
             
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                    long id, boolean checked) {
                // TODO Auto-generated method stub
                 if (checked) {
                        nr++;
                        mAdapter.setNewSelection(position, checked);                    
                    } else {
                        nr--;
                        mAdapter.removeSelection(position);                 
                    }
                    mode.setTitle(nr + " selected");
                 
            }
        });
         
        getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
 
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                    int position, long arg3) {
                // TODO Auto-generated method stub
                 
                getListView().setItemChecked(position, !mAdapter.isPositionChecked(position));
                return false;
            }
        });
    }
     
    private class SelectionAdapter extends ArrayAdapter<String> {
 
        private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();
 
        public SelectionAdapter(Context context, int resource,
                int textViewResourceId, String[] objects) {
            super(context, resource, textViewResourceId, objects);
        }
 
        public void setNewSelection(int position, boolean value) {
            mSelection.put(position, value);
            notifyDataSetChanged();
        }
 
        public boolean isPositionChecked(int position) {
            Boolean result = mSelection.get(position);
            return result == null ? false : result;
        }
 
        public Set<Integer> getCurrentCheckedPosition() {
            return mSelection.keySet();
        }
 
        public void removeSelection(int position) {
            mSelection.remove(position);
            notifyDataSetChanged();
        }
 
        public void clearSelection() {
            mSelection = new HashMap<Integer, Boolean>();
            notifyDataSetChanged();
        }
 
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);//let the adapter handle setting up the row views
            v.setBackgroundColor(getResources().getColor(android.R.color.background_light)); //default color
             
            if (mSelection.get(position) != null) {
                v.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));// this is a selected position so make it red
            }
            return v;
        }
    }
}