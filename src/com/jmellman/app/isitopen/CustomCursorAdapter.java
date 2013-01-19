package com.jmellman.app.isitopen;

import java.util.Calendar;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class CustomCursorAdapter extends CursorAdapter {
	private Cursor mCursor;
	private Context mContext;

	public static final String SUNDAY_PREFIX = "s";
	public static final String MONDAY_PREFIX = "m";
	public static final String TUESDAY_PREFIX = "t";
	public static final String WEDNESDAY_PREFIX = "w";
	public static final String THURSDAY_PREFIX = "th";
	public static final String FRIDAY_PREFIX = "f";
	public static final String SATURDAY_PREFIX = "sat";

	private final LayoutInflater mInflater;


	public CustomCursorAdapter(Context context, Cursor c) {
		super(context, c);
		mInflater = LayoutInflater.from(context);
		mContext = context;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView textTitle = (TextView) view.findViewById(R.id.text1);		
		textTitle.setText("\t" + cursor.getString(cursor.getColumnIndex(ItemsDbAdapter.KEY_TITLE)));

		TextView textOpen = (TextView) view.findViewById(R.id.text2);

		String title = cursor.getString(cursor.getColumnIndex(ItemsDbAdapter.KEY_TITLE));
		if (isHeader(title)) {
			textTitle.setText(title.substring(1));
			textTitle.setTextColor(Color.LTGRAY);
			textTitle.setTextSize(22);
		} else {
			if (isOpen(cursor)) {
				textOpen.setText("Open!");
				textOpen.setTextColor(Color.GREEN);
			} else {
				textOpen.setText("Closed");
				textOpen.setTextColor(Color.RED);
			}
		}
	}

	private boolean isHeader(String title) {
		return (title.charAt(0) == IsItOpen.HEADER_CHAR);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final View view = mInflater.inflate(R.layout.item_row, parent, false);
		return view;
	}

	private boolean isOpen(Cursor cursor) {
		Calendar cal = Calendar.getInstance();
		int currentD = cal.get(Calendar.DAY_OF_WEEK);
		String prefix = "";

		switch (currentD) {
		case Calendar.SUNDAY: //saturday?
			prefix = SUNDAY_PREFIX;
			break;
		case Calendar.MONDAY:
			prefix = MONDAY_PREFIX;
			break;
		case Calendar.TUESDAY:
			prefix = TUESDAY_PREFIX;
			break;
		case Calendar.WEDNESDAY:
			prefix = WEDNESDAY_PREFIX;
			break;
		case Calendar.THURSDAY:
			prefix = THURSDAY_PREFIX;
			break;
		case Calendar.FRIDAY:
			prefix = FRIDAY_PREFIX;
			break;
		case Calendar.SATURDAY:
			prefix = SATURDAY_PREFIX;
			break;
		}

		String openString = cursor.getString(cursor.getColumnIndex(prefix + "_opens"));
		String closeString = cursor.getString(cursor.getColumnIndex(prefix + "_closes"));

		if (openString.isEmpty() || closeString.isEmpty()) { //SHOULD never fire
			return false;
		}

		int openH = Integer.parseInt(openString.split(":")[0]);
		int openM = Integer.parseInt(openString.split(":")[1]);
		int closeH = Integer.parseInt(closeString.split(":")[0]);
		int closeM = Integer.parseInt(closeString.split(":")[1]);

		int currentH = cal.get(Calendar.HOUR_OF_DAY);
		int currentM = cal.get(Calendar.MINUTE);

		if ((openH <= currentH) && (currentH <= closeH)) {
			if ((openH == currentH) || (closeH == currentH)) {
				if ((openM > currentM) || (currentM > closeM)) {
					return false;
				}
			}
			return true;
		}

		return false;
	}

}
