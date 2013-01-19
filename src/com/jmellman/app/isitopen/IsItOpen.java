/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jmellman.app.isitopen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;

public class IsItOpen extends ListActivity {
	private static final int ACTIVITY_CREATE=0;
	private static final int ACTIVITY_VIEWHOURS=1;
	private static final int ACTIVITY_EDIT=2;

	private static final int INSERT_ID = Menu.FIRST;
	private static final int DELETE_ALL_ID = Menu.FIRST + 1;
	private static final int DELETE_ID = DELETE_ALL_ID + 1;
	private static final int EDIT_ID = DELETE_ID + 1;
	private static final int ADD_TAB_ID = EDIT_ID + 1;
	
	public static final char HEADER_CHAR = '*';


	private ItemsDbAdapter mDbHelper;

	private TextView mOpenText;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_list);
		mDbHelper = new ItemsDbAdapter(this);
		mDbHelper.open();
		mDbHelper.deleteAllItems();
		loadAssetsToDb();
		fillData();
		registerForContextMenu(getListView());
	}

	private void loadAssetsToDb() {
		AssetManager assetManager = getAssets();
		try {
			InputStreamReader in = new InputStreamReader(assetManager.open("Furman Pack.iio"));
			BufferedReader br = new BufferedReader(in);
			parseAsset(br);			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void parseAsset(BufferedReader br) throws IOException {
		String header = br.readLine();
		mDbHelper.createItem(header);
		
		String title;
		
		while ((title = br.readLine()) != null) {
			if (title.charAt(0) == HEADER_CHAR) {
				header = title;
				mDbHelper.createItem(header);
				title = br.readLine(); //will never be a null line after a header
			}

			String days[] = new String[7]; //holds string representing open and close hours
			for (int i = 0; i < 7; i++) {
				days[i] = br.readLine();
				days[i] = (days[i].equals("C")) ? "" : days[i];

			}

			String open[] = new String[7];
			String closed[] = new String[7];

			for (int i = 0; i < 7; i++) {
				if ((days)[i].isEmpty()) { //item is closed on this day
					open[i] = "";
					closed[i] = "";
				} else {
					open[i] = days[i].split(" ")[0];
					closed[i] = days[i].split(" ")[1];
				}
			}

			mDbHelper.createItem(title, open[0], closed[0], open[1], closed[1], open[2], closed[2], 
					open[3], closed[3], open[4], closed[4], open[5], closed[5], open[6], closed[6]);
			
		}
		br.close();

	}

	private void fillData() {

		// Get all of the rows from the database and create the item list
		Cursor itemCursor = mDbHelper.fetchAllItems();
		startManagingCursor(itemCursor);

		CustomCursorAdapter items = new CustomCursorAdapter(this, itemCursor);
		setListAdapter(items);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, INSERT_ID, 0, R.string.menu_insert);
		menu.add(0, DELETE_ALL_ID, 0, R.string.delete_all);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {
		case INSERT_ID:
			createItem();
			return true;
		case DELETE_ALL_ID:
			boolean toReturn = mDbHelper.deleteAllItems();
			fillData();
			return toReturn;
		}

		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, EDIT_ID, 0, R.string.menu_edit);
		menu.add(0, DELETE_ID, 0, R.string.menu_delete);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

		switch(item.getItemId()) {
		case EDIT_ID:
			Intent i = new Intent(this, ItemEdit.class);
			i.putExtra(ItemsDbAdapter.KEY_ROWID, info.id);
			startActivityForResult(i, ACTIVITY_EDIT);
			return true;
		case DELETE_ID:
			mDbHelper.deleteItem(info.id);
			fillData();
			return true;
		}
		return super.onContextItemSelected(item);
	}

	private void createItem() {
		Intent i = new Intent(this, ItemEdit.class);
		startActivityForResult(i, ACTIVITY_CREATE);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		//        Intent i = new Intent(this, ItemEdit.class);
		//        i.putExtra(ItemsDbAdapter.KEY_ROWID, id);
		//        startActivityForResult(i, ACTIVITY_EDIT);
		
		TextView tv = (TextView) v.findViewById(R.id.text2);
		if (tv.getText().toString().isEmpty()) {
			return;
		}
		
		
		Intent i = new Intent(this, ItemHours.class);
		i.putExtra(ItemsDbAdapter.KEY_ROWID, id);
		startActivityForResult(i, ACTIVITY_VIEWHOURS);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		fillData();
	}
}
