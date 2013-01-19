package com.jmellman.app.isitopen;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class ItemHours extends Activity {
	private ItemsDbAdapter mDbHelper;
	private Long mRowId;
	
	private TextView mTitleText;
	private TextView mSunday;
	private TextView mMonday;
	private TextView mTuesday;
	private TextView mWednesday;
	private TextView mThursday;
	private TextView mFriday;
	private TextView mSaturday;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mDbHelper = new ItemsDbAdapter(this);
		mDbHelper.open();

		setContentView(R.layout.item_hours);
		setTitle("View Item!");

		mRowId = (savedInstanceState == null) ? null :
			(Long) savedInstanceState.getSerializable(ItemsDbAdapter.KEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(ItemsDbAdapter.KEY_ROWID)
					: null;
		}
		
		mTitleText = (TextView) findViewById(R.id.hours_text_title);
		mSunday = (TextView) findViewById(R.id.sunday);
		mMonday = (TextView) findViewById(R.id.monday);
		mTuesday = (TextView) findViewById(R.id.tuesday);
		mWednesday = (TextView) findViewById(R.id.wednesday);
		mThursday = (TextView) findViewById(R.id.thursday);
		mFriday = (TextView) findViewById(R.id.friday);
		mSaturday = (TextView) findViewById(R.id.saturday);
		
		String opens;
		
		Cursor item = mDbHelper.fetchItem(mRowId);
		startManagingCursor(item);
		mTitleText.setText(item.getString(
				item.getColumnIndexOrThrow(ItemsDbAdapter.KEY_TITLE)));
		
		//Sunday
		opens = item.getString(item.getColumnIndexOrThrow(ItemsDbAdapter.KEY_S_OPENS));
		if (opens.isEmpty()) {
			mSunday.setText("S: CLOSED");
		} else {
			mSunday.setText("S: " + opens + " - " + 
					item.getString(item.getColumnIndexOrThrow(ItemsDbAdapter.KEY_S_CLOSES)));
		}
		
		//Monday
		opens = item.getString(item.getColumnIndexOrThrow(ItemsDbAdapter.KEY_M_OPENS));
		if (opens.isEmpty()) {
			mMonday.setText("M: CLOSED");
		} else {
			mMonday.setText("M: " +  opens + " - " + 
					item.getString(item.getColumnIndexOrThrow(ItemsDbAdapter.KEY_M_CLOSES)));
		}
		
		//Tuesday
		opens = item.getString(item.getColumnIndexOrThrow(ItemsDbAdapter.KEY_T_OPENS));
		if (opens.isEmpty()) {
			mTuesday.setText("T: CLOSED");
		} else {
			mTuesday.setText("T: " +  opens + " - " + 
					item.getString(item.getColumnIndexOrThrow(ItemsDbAdapter.KEY_T_CLOSES)));
		}
		
		//Wednesday
		opens = item.getString(item.getColumnIndexOrThrow(ItemsDbAdapter.KEY_W_OPENS));
		if (opens.isEmpty()) {
			mWednesday.setText("W: CLOSED");
		} else {
			mWednesday.setText("W: " +  opens + " - " + 
					item.getString(item.getColumnIndexOrThrow(ItemsDbAdapter.KEY_W_CLOSES)));
		}
		
		//Thursday
		opens = item.getString(item.getColumnIndexOrThrow(ItemsDbAdapter.KEY_TH_OPENS));
		if (opens.isEmpty()) {
			mThursday.setText("TH: CLOSED");
		} else {
			mThursday.setText("Th: " +  opens + " - " + 
					item.getString(item.getColumnIndexOrThrow(ItemsDbAdapter.KEY_TH_CLOSES)));
		}
		
		//Friday
		opens = item.getString(item.getColumnIndexOrThrow(ItemsDbAdapter.KEY_F_OPENS));
		if (opens.isEmpty()) {
			mFriday.setText("F: CLOSED");
		} else {
			mFriday.setText("F: " +  opens + " - " + 
					item.getString(item.getColumnIndexOrThrow(ItemsDbAdapter.KEY_F_CLOSES)));
		}
		
		//Saturday
		opens = item.getString(item.getColumnIndexOrThrow(ItemsDbAdapter.KEY_SAT_OPENS));
		if (opens.isEmpty()) {
			mSaturday.setText("SAT: CLOSED");
		} else {
			mSaturday.setText("SAT: " +  opens + " - " + 
					item.getString(item.getColumnIndexOrThrow(ItemsDbAdapter.KEY_SAT_CLOSES)));
		}
		
	}

}
