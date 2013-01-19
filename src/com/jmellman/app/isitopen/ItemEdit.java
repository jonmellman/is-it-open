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

import java.io.Serializable;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

public class ItemEdit extends Activity {

	//IDs so timepicker's know which button's text to populate
	static final int SO_DIALOG_ID = 0;
	static final int SC_DIALOG_ID = 1;
	static final int MO_DIALOG_ID = 2;
	static final int MC_DIALOG_ID = 3;
	static final int TO_DIALOG_ID = 4;
	static final int TC_DIALOG_ID = 5;
	static final int WO_DIALOG_ID = 6;
	static final int WC_DIALOG_ID = 7;
	static final int THO_DIALOG_ID = 8;
	static final int THC_DIALOG_ID = 9;
	static final int FO_DIALOG_ID = 10;
	static final int FC_DIALOG_ID = 11;
	static final int SATO_DIALOG_ID = 12;
	static final int SATC_DIALOG_ID = 13;
	
	private boolean backButtonFlag = false;

	static final int DIALOG_ALERT = 14;

	private EditText mTitleText;

	//Sunday components
	private LinearLayout mS_InputBar;
	private Button mS_OpenTime;
	private Button mS_CloseTime;
	private CheckBox mS_Check;

	//Monday components
	private LinearLayout mM_InputBar;
	private Button mM_OpenTime;
	private Button mM_CloseTime;
	private CheckBox mM_Check;

	//Tuesday components
	private LinearLayout mT_InputBar;
	private Button mT_OpenTime;
	private Button mT_CloseTime;
	private CheckBox mT_Check;

	//Wednesday components
	private LinearLayout mW_InputBar;
	private Button mW_OpenTime;
	private Button mW_CloseTime;
	private CheckBox mW_Check;

	//Thursday components
	private LinearLayout mTh_InputBar;
	private Button mTh_OpenTime;
	private Button mTh_CloseTime;
	private CheckBox mTh_Check;

	//Friday components
	private LinearLayout mF_InputBar;
	private Button mF_OpenTime;
	private Button mF_CloseTime;
	private CheckBox mF_Check;

	//Saturday components
	private LinearLayout mSat_InputBar;
	private Button mSat_OpenTime;
	private Button mSat_CloseTime;
	private CheckBox mSat_Check;

	//Confirm button
	private Button mConfirmButton;
	//TODO: Refresh button

	private Long mRowId;
	private ItemsDbAdapter mDbHelper;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mDbHelper = new ItemsDbAdapter(this);
		mDbHelper.open();

		setContentView(R.layout.item_edit);
		setTitle(R.string.edit_item);

		initializeComponents();

		mRowId = (savedInstanceState == null) ? null :
			(Long) savedInstanceState.getSerializable(ItemsDbAdapter.KEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(ItemsDbAdapter.KEY_ROWID)
					: null;
		}
		
		populateFields();
	}

	private void initializeComponents() {
		//INITIALIZE COMPONENTS
		mTitleText = (EditText) findViewById(R.id.title);

		//Sunday components
		mS_InputBar = (LinearLayout) findViewById(R.id.s_InputBar);

		mS_OpenTime = (Button) findViewById(R.id.s_OpenTime);
		mS_CloseTime = (Button) findViewById(R.id.s_CloseTime);
		mS_OpenTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(SO_DIALOG_ID);
			}
		});
		mS_CloseTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(SC_DIALOG_ID);
			}
		});

		mS_Check = (CheckBox) findViewById(R.id.s_Check);


		mS_Check.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (isChecked)
				{
					mS_InputBar.setVisibility(View.VISIBLE);
				} else {
					mS_InputBar.setVisibility(View.GONE);
				}

			}
		});

		//Monday components
		mM_InputBar = (LinearLayout) findViewById(R.id.m_InputBar);

		mM_OpenTime = (Button) findViewById(R.id.m_OpenTime);
		mM_CloseTime = (Button) findViewById(R.id.m_CloseTime);	
		mM_OpenTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(MO_DIALOG_ID);
			}
		});
		mM_CloseTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(MC_DIALOG_ID);
			}
		});

		mM_Check = (CheckBox) findViewById(R.id.m_Check);		
		mM_Check.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (isChecked)
				{
					mM_InputBar.setVisibility(View.VISIBLE);
				} else {
					mM_InputBar.setVisibility(View.GONE);
				}

			}
		});

		//Tuesday components
		mT_InputBar = (LinearLayout) findViewById(R.id.t_InputBar);

		mT_OpenTime = (Button) findViewById(R.id.t_OpenTime);
		mT_CloseTime = (Button) findViewById(R.id.t_CloseTime);
		mT_OpenTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(TO_DIALOG_ID);
			}
		});
		mT_CloseTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(TC_DIALOG_ID);
			}
		});

		mT_Check = (CheckBox) findViewById(R.id.t_Check);


		mT_Check.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (isChecked)
				{
					mT_InputBar.setVisibility(View.VISIBLE);
				} else {
					mT_InputBar.setVisibility(View.GONE);
				}

			}
		});

		//Wednesday components
		mW_InputBar = (LinearLayout) findViewById(R.id.w_InputBar);

		mW_OpenTime = (Button) findViewById(R.id.w_OpenTime);
		mW_CloseTime = (Button) findViewById(R.id.w_CloseTime);
		mW_OpenTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(WO_DIALOG_ID);
			}
		});
		mW_CloseTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(WC_DIALOG_ID);
			}
		});

		mW_Check = (CheckBox) findViewById(R.id.w_Check);


		mW_Check.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (isChecked)
				{
					mW_InputBar.setVisibility(View.VISIBLE);
				} else {
					mW_InputBar.setVisibility(View.GONE);
				}

			}
		});

		//Thursday components
		mTh_InputBar = (LinearLayout) findViewById(R.id.th_InputBar);

		mTh_OpenTime = (Button) findViewById(R.id.th_OpenTime);
		mTh_CloseTime = (Button) findViewById(R.id.th_CloseTime);
		mTh_OpenTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(THO_DIALOG_ID);
			}
		});
		mTh_CloseTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(THC_DIALOG_ID);
			}
		});

		mTh_Check = (CheckBox) findViewById(R.id.th_Check);


		mTh_Check.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (isChecked)
				{
					mTh_InputBar.setVisibility(View.VISIBLE);
				} else {
					mTh_InputBar.setVisibility(View.GONE);
				}

			}
		});

		//Friday components
		mF_InputBar = (LinearLayout) findViewById(R.id.f_InputBar);

		mF_OpenTime = (Button) findViewById(R.id.f_OpenTime);
		mF_CloseTime = (Button) findViewById(R.id.f_CloseTime);
		mF_OpenTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(FO_DIALOG_ID);
			}
		});
		mF_CloseTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(FC_DIALOG_ID);
			}
		});

		mF_Check = (CheckBox) findViewById(R.id.f_Check);


		mF_Check.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (isChecked)
				{
					mF_InputBar.setVisibility(View.VISIBLE);
				} else {
					mF_InputBar.setVisibility(View.GONE);
				}

			}
		});

		//Saturday components
		mSat_InputBar = (LinearLayout) findViewById(R.id.sat_InputBar);

		mSat_OpenTime = (Button) findViewById(R.id.sat_OpenTime);
		mSat_CloseTime = (Button) findViewById(R.id.sat_CloseTime);
		mSat_OpenTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(SATO_DIALOG_ID);
			}
		});
		mSat_CloseTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(SATC_DIALOG_ID);
			}
		});

		mSat_Check = (CheckBox) findViewById(R.id.sat_Check);


		mSat_Check.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (isChecked)
				{
					mSat_InputBar.setVisibility(View.VISIBLE);
				} else {
					mSat_InputBar.setVisibility(View.GONE);
				}

			}
		});

		//confirm button
		mConfirmButton = (Button) findViewById(R.id.confirm);
		mConfirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Bundle alertMessage = buildAlertBundle();
				if (!alertMessage.isEmpty()) {
					showDialog(DIALOG_ALERT, alertMessage);
				} else {
					setResult(RESULT_OK);
					finish();
				}
			}
		});
		
	}


	private static String padTime(int time) {
		if (time >= 10)
			return String.valueOf(time);
		else
			return "0" + String.valueOf(time);
	}


	/** Populates TextFields, or hides them if they're blank
	 * 
	 */
	private void populateFields() {
		if (mRowId != null) {
			Cursor item = mDbHelper.fetchItem(mRowId);
			startManagingCursor(item);
			mTitleText.setText(item.getString(
					item.getColumnIndexOrThrow(ItemsDbAdapter.KEY_TITLE)));
			mS_OpenTime.setText(item.getString(item.getColumnIndexOrThrow(ItemsDbAdapter.KEY_S_OPENS)));
			mS_CloseTime.setText(item.getString(item.getColumnIndexOrThrow(ItemsDbAdapter.KEY_S_CLOSES)));
			mM_OpenTime.setText(item.getString(item.getColumnIndexOrThrow(ItemsDbAdapter.KEY_M_OPENS)));
			mM_CloseTime.setText(item.getString(item.getColumnIndexOrThrow(ItemsDbAdapter.KEY_M_CLOSES)));
			mT_OpenTime.setText(item.getString(item.getColumnIndexOrThrow(ItemsDbAdapter.KEY_T_OPENS)));
			mT_CloseTime.setText(item.getString(item.getColumnIndexOrThrow(ItemsDbAdapter.KEY_T_CLOSES)));
			mW_OpenTime.setText(item.getString(item.getColumnIndexOrThrow(ItemsDbAdapter.KEY_W_OPENS)));
			mW_CloseTime.setText(item.getString(item.getColumnIndexOrThrow(ItemsDbAdapter.KEY_W_CLOSES)));
			mTh_OpenTime.setText(item.getString(item.getColumnIndexOrThrow(ItemsDbAdapter.KEY_TH_OPENS)));
			mTh_CloseTime.setText(item.getString(item.getColumnIndexOrThrow(ItemsDbAdapter.KEY_TH_CLOSES)));
			mF_OpenTime.setText(item.getString(item.getColumnIndexOrThrow(ItemsDbAdapter.KEY_F_OPENS)));
			mF_CloseTime.setText(item.getString(item.getColumnIndexOrThrow(ItemsDbAdapter.KEY_F_CLOSES)));
			mSat_OpenTime.setText(item.getString(item.getColumnIndexOrThrow(ItemsDbAdapter.KEY_SAT_OPENS)));
			mSat_CloseTime.setText(item.getString(item.getColumnIndexOrThrow(ItemsDbAdapter.KEY_SAT_CLOSES)));
		}

		if (!mS_OpenTime.getText().toString().isEmpty()) {
			mS_Check.setChecked(true);
		}

		if (!mM_OpenTime.getText().toString().isEmpty()) {
			mM_Check.setChecked(true);
		}

		if (!mT_OpenTime.getText().toString().isEmpty()) {
			mT_Check.setChecked(true);
		}
		if (!mW_OpenTime.getText().toString().isEmpty()) {
			mW_Check.setChecked(true);
		}
		if (!mTh_OpenTime.getText().toString().isEmpty()) {
			mTh_Check.setChecked(true);
		}
		if (!mF_OpenTime.getText().toString().isEmpty()) {
			mF_Check.setChecked(true);
		}
		if (!mSat_OpenTime.getText().toString().isEmpty()) {
			mSat_Check.setChecked(true);
		}
	}

	private void saveState() {
		String title = mTitleText.getText().toString();
		String s_opens, s_closes, m_opens, m_closes, t_opens, t_closes, w_opens, w_closes,
		th_opens, th_closes, f_opens, f_closes, sat_opens, sat_closes;


		if (mS_Check.isChecked())  {
			s_opens = mS_OpenTime.getText().toString();
			s_closes = mS_CloseTime.getText().toString();
		} else {
			s_opens = "";
			s_closes = "";
		}

		if (mM_Check.isChecked())  {
			m_opens = mM_OpenTime.getText().toString();
			m_closes = mM_CloseTime.getText().toString();
		} else {
			m_opens = "";
			m_closes = "";
		}

		if (mT_Check.isChecked())  {
			t_opens = mT_OpenTime.getText().toString();
			t_closes = mT_CloseTime.getText().toString();
		} else {
			t_opens = "";
			t_closes = "";
		}

		if (mW_Check.isChecked())  {
			w_opens = mW_OpenTime.getText().toString();
			w_closes = mW_CloseTime.getText().toString();
		} else {
			w_opens = "";
			w_closes = "";
		}

		if (mTh_Check.isChecked())  {
			th_opens = mTh_OpenTime.getText().toString();
			th_closes = mTh_CloseTime.getText().toString();
		} else {
			th_opens = "";
			th_closes = "";
		}
		if (mF_Check.isChecked())  {
			f_opens = mF_OpenTime.getText().toString();
			f_closes = mF_CloseTime.getText().toString();
		} else {
			f_opens = "";
			f_closes = "";
		}
		if (mSat_Check.isChecked())  {
			sat_opens = mSat_OpenTime.getText().toString();
			sat_closes = mSat_CloseTime.getText().toString();
		} else {
			sat_opens = "";
			sat_closes = "";
		}


		if (mRowId == null) {
			long id = mDbHelper.createItem(title, s_opens, s_closes, m_opens, m_closes, t_opens, t_closes, 
					w_opens, w_closes, th_opens, th_closes, f_opens, f_closes, sat_opens, sat_closes);
			if (id > 0) {
				mRowId = id;
			}
		} else {
			mDbHelper.updateNote(mRowId, title, s_opens, s_closes, m_opens, m_closes, t_opens, t_closes, 
					w_opens, w_closes, th_opens, th_closes, f_opens, f_closes, sat_opens, sat_closes);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putSerializable(ItemsDbAdapter.KEY_ROWID, mRowId);
	}

	@Override
	public void onBackPressed() {
		backButtonFlag = true;
		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (!backButtonFlag) {
			saveState();
		} else {
			backButtonFlag = false;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		populateFields();
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle extras) {
		switch (id) {
		case SO_DIALOG_ID:
			return new TimePickerDialog(this,
					m_SOTimeSetListener, 0, 0, false);
		case SC_DIALOG_ID:
			return new TimePickerDialog(this,
					m_SCTimeSetListener, 0, 0, false);
		case MO_DIALOG_ID:
			return new TimePickerDialog(this,
					m_MOTimeSetListener, 0, 0, false);
		case MC_DIALOG_ID:
			return new TimePickerDialog(this,
					m_MCTimeSetListener, 0, 0, false);
		case TO_DIALOG_ID:
			return new TimePickerDialog(this,
					m_TOTimeSetListener, 0, 0, false);
		case TC_DIALOG_ID:
			return new TimePickerDialog(this,
					m_TCTimeSetListener, 0, 0, false);
		case WO_DIALOG_ID:
			return new TimePickerDialog(this,
					m_WOTimeSetListener, 0, 0, false);
		case WC_DIALOG_ID:
			return new TimePickerDialog(this,
					m_WCTimeSetListener, 0, 0, false);
		case THO_DIALOG_ID:
			return new TimePickerDialog(this,
					m_ThOTimeSetListener, 0, 0, false);
		case THC_DIALOG_ID:
			return new TimePickerDialog(this,
					m_ThCTimeSetListener, 0, 0, false);
		case FO_DIALOG_ID:
			return new TimePickerDialog(this,
					m_FOTimeSetListener, 0, 0, false);
		case FC_DIALOG_ID:
			return new TimePickerDialog(this,
					m_FCTimeSetListener, 0, 0, false);
		case SATO_DIALOG_ID:
			return new TimePickerDialog(this,
					m_SatOTimeSetListener, 0, 0, false);
		case SATC_DIALOG_ID:
			return new TimePickerDialog(this,
					m_SatCTimeSetListener, 0, 0, false);
		case DIALOG_ALERT:
			String message = extras.getString("alert");
			Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(message);
			builder.setCancelable(false);
			builder.setNegativeButton("Okay", new CancelOnClickListener());
			AlertDialog dialog = builder.create();
			dialog.show();
		}

		return super.onCreateDialog(id); 
	}

	private Bundle buildAlertBundle() {
		String message = "";

		if (mTitleText.getText().toString().isEmpty()) {
			if (!message.isEmpty()) {
				message += "\n";
			}
			message += "Title cannot be blank!";
		}

		if (mS_Check.isChecked() && (mS_OpenTime.getText().toString().isEmpty()
				|| mS_CloseTime.getText().toString().isEmpty())) {
			if (!message.isEmpty()) {
				message += "\n";
			}
			message += "Times cannot be blank!";
		}

		if (mM_Check.isChecked() && (mM_OpenTime.getText().toString().isEmpty()
				|| mM_CloseTime.getText().toString().isEmpty())) {
			if (!message.isEmpty()) {
				message += "\n";
			}
			message += "Times cannot be blank!";
		}

		if (mT_Check.isChecked() && (mT_OpenTime.getText().toString().isEmpty()
				|| mT_CloseTime.getText().toString().isEmpty())) {
			if (!message.isEmpty()) {
				message += "\n";
			}
			message += "Times cannot be blank!";
		}

		if (mW_Check.isChecked() && (mW_OpenTime.getText().toString().isEmpty()
				|| mW_CloseTime.getText().toString().isEmpty())) {
			if (!message.isEmpty()) {
				message += "\n";
			}
			message += "Times cannot be blank!";
		}

		if (mTh_Check.isChecked() && (mTh_OpenTime.getText().toString().isEmpty()
				|| mTh_CloseTime.getText().toString().isEmpty())) {
			if (!message.isEmpty()) {
				message += "\n";
			}
			message += "Times cannot be blank!";
		}

		if (mF_Check.isChecked() && (mF_OpenTime.getText().toString().isEmpty()
				|| mF_CloseTime.getText().toString().isEmpty())) {
			if (!message.isEmpty()) {
				message += "\n";
			}
			message += "Times cannot be blank!";
		}

		if (mSat_Check.isChecked() && (mSat_OpenTime.getText().toString().isEmpty()
				|| mSat_CloseTime.getText().toString().isEmpty())) {
			if (!message.isEmpty()) {
				message += "\n";
			}
			message += "Times cannot be blank!";
		}

		if (message.isEmpty()) { //if times aren't blank
			if (mS_Check.isChecked() && !isValidTimes(mS_OpenTime.getText().toString(), mS_CloseTime.getText().toString())) {
				message += "Must close AFTER it opens!";
			}
			if (mM_Check.isChecked() && !isValidTimes(mM_OpenTime.getText().toString(), mM_CloseTime.getText().toString())) {
				message += "Must close AFTER it opens!";
			}
			if (mT_Check.isChecked() && !isValidTimes(mT_OpenTime.getText().toString(), mT_CloseTime.getText().toString())) {
				message += "Must close AFTER it opens!";
			}
			if (mW_Check.isChecked() && !isValidTimes(mW_OpenTime.getText().toString(), mW_CloseTime.getText().toString())) {
				message += "Must close AFTER it opens!";
			}
			if (mTh_Check.isChecked() && !isValidTimes(mTh_OpenTime.getText().toString(), mTh_CloseTime.getText().toString())) {
				message += "Must close AFTER it opens!";
			}
			if (mF_Check.isChecked() && !isValidTimes(mF_OpenTime.getText().toString(), mF_CloseTime.getText().toString())) {
				message += "Must close AFTER it opens!";
			}
			if (mSat_Check.isChecked() && !isValidTimes(mSat_OpenTime.getText().toString(), mSat_CloseTime.getText().toString())) {
				message += "Must close AFTER it opens!";
			}
		}

		Bundle toReturn = new Bundle();
		if (!message.isEmpty()) {
			toReturn.putString("alert", message);
		}

		return toReturn;

	}

	private boolean isValidTimes(String open, String close) {
		int openH = Integer.parseInt(open.split(":")[0]);
		int openM = Integer.parseInt(open.split(":")[1]);
		int closeH = Integer.parseInt(close.split(":")[0]);
		int closeM = Integer.parseInt(close.split(":")[1]);

		if (closeH > openH) {
			return true;
		} else {
			if (closeH == openH) {
				if (closeM > openM) {
					return true;
				}
			}
		}
		return false;	
	}

	private final class CancelOnClickListener implements
	DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {

		}
	} 
	
	//TIMEPICKER DIALOGS
	private TimePickerDialog.OnTimeSetListener m_SOTimeSetListener =
			new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mS_OpenTime.setText(new StringBuilder()
			.append(padTime(hourOfDay))
			.append(":")
			.append(padTime(minute)));
		}
	};
	private TimePickerDialog.OnTimeSetListener m_SCTimeSetListener =
			new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mS_CloseTime.setText(new StringBuilder()
			.append(padTime(hourOfDay))
			.append(":")
			.append(padTime(minute)));
		}
	};
	private TimePickerDialog.OnTimeSetListener m_MOTimeSetListener =
			new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mM_OpenTime.setText(new StringBuilder()
			.append(padTime(hourOfDay))
			.append(":")
			.append(padTime(minute)));
		}
	};
	private TimePickerDialog.OnTimeSetListener m_MCTimeSetListener =
			new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mM_CloseTime.setText(new StringBuilder()
			.append(padTime(hourOfDay))
			.append(":")
			.append(padTime(minute)));
		}
	};
	private TimePickerDialog.OnTimeSetListener m_TOTimeSetListener =
			new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mT_OpenTime.setText(new StringBuilder()
			.append(padTime(hourOfDay))
			.append(":")
			.append(padTime(minute)));
		}
	};
	private TimePickerDialog.OnTimeSetListener m_TCTimeSetListener =
			new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mT_CloseTime.setText(new StringBuilder()
			.append(padTime(hourOfDay))
			.append(":")
			.append(padTime(minute)));
		}
	};
	private TimePickerDialog.OnTimeSetListener m_WOTimeSetListener =
			new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mW_OpenTime.setText(new StringBuilder()
			.append(padTime(hourOfDay))
			.append(":")
			.append(padTime(minute)));
		}
	};
	private TimePickerDialog.OnTimeSetListener m_WCTimeSetListener =
			new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mW_CloseTime.setText(new StringBuilder()
			.append(padTime(hourOfDay))
			.append(":")
			.append(padTime(minute)));
		}
	};
	private TimePickerDialog.OnTimeSetListener m_ThOTimeSetListener =
			new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mTh_OpenTime.setText(new StringBuilder()
			.append(padTime(hourOfDay))
			.append(":")
			.append(padTime(minute)));
		}
	};
	private TimePickerDialog.OnTimeSetListener m_ThCTimeSetListener =
			new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mTh_CloseTime.setText(new StringBuilder()
			.append(padTime(hourOfDay))
			.append(":")
			.append(padTime(minute)));
		}
	};
	private TimePickerDialog.OnTimeSetListener m_FOTimeSetListener =
			new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mF_OpenTime.setText(new StringBuilder()
			.append(padTime(hourOfDay))
			.append(":")
			.append(padTime(minute)));
		}
	};
	private TimePickerDialog.OnTimeSetListener m_FCTimeSetListener =
			new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mF_CloseTime.setText(new StringBuilder()
			.append(padTime(hourOfDay))
			.append(":")
			.append(padTime(minute)));
		}
	};
	private TimePickerDialog.OnTimeSetListener m_SatOTimeSetListener =
			new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mSat_OpenTime.setText(new StringBuilder()
			.append(padTime(hourOfDay))
			.append(":")
			.append(padTime(minute)));
		}
	};
	private TimePickerDialog.OnTimeSetListener m_SatCTimeSetListener =
			new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mSat_CloseTime.setText(new StringBuilder()
			.append(padTime(hourOfDay))
			.append(":")
			.append(padTime(minute)));
		}
	};
}
