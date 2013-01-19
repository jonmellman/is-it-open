/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.jmellman.app.isitopen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 * 
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class ItemsDbAdapter {

	public static final String KEY_TITLE = "title";

	public static final String KEY_S_OPENS = "s_opens";
	public static final String KEY_S_CLOSES = "s_closes";
	public static final String KEY_M_OPENS = "m_opens";
	public static final String KEY_M_CLOSES = "m_closes";
	public static final String KEY_T_OPENS = "t_opens";
	public static final String KEY_T_CLOSES = "t_closes";
	public static final String KEY_W_OPENS = "w_opens";
	public static final String KEY_W_CLOSES = "w_closes";
	public static final String KEY_TH_OPENS = "th_opens";
	public static final String KEY_TH_CLOSES = "th_closes";
	public static final String KEY_F_OPENS = "f_opens";
	public static final String KEY_F_CLOSES = "f_closes";
	public static final String KEY_SAT_OPENS = "sat_opens";
	public static final String KEY_SAT_CLOSES = "sat_closes";

	public static final String KEY_ROWID = "_id";

	private static final String TAG = "NotesDbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static final String DATABASE_NAME = "IsItOpen02";
	private static final String DATABASE_TABLE = "items02";
	private static final int DATABASE_VERSION = 2;

	/**
	 * Database creation sql statement
	 */
	private static final String DATABASE_CREATE =
			"CREATE TABLE " + DATABASE_TABLE + " (" +
					KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					KEY_TITLE + " TEXT NOT NULL, " + 
					KEY_S_OPENS + " TEXT NOT NULL, " +
					KEY_S_CLOSES + " TEXT NOT NULL, " +
					KEY_M_OPENS + " TEXT NOT NULL, " +
					KEY_M_CLOSES + " TEXT NOT NULL, " + 
					KEY_T_OPENS + " TEXT NOT NULL, " +
					KEY_T_CLOSES + " TEXT NOT NULL, " + 
					KEY_W_OPENS + " TEXT NOT NULL, " +
					KEY_W_CLOSES + " TEXT NOT NULL, " + 
					KEY_TH_OPENS + " TEXT NOT NULL, " +
					KEY_TH_CLOSES + " TEXT NOT NULL, " + 
					KEY_F_OPENS + " TEXT NOT NULL, " +
					KEY_F_CLOSES + " TEXT NOT NULL, " + 
					KEY_SAT_OPENS + " TEXT NOT NULL, " +
					KEY_SAT_CLOSES + " TEXT NOT NULL" + 
					");";

	private final Context mCtx;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
			onCreate(db);
		}
	}

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param ctx the Context within which to work
	 */
	public ItemsDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	/**
	 * Open the notes database. If it cannot be opened, try to create a new
	 * instance of the database. If it cannot be created, throw an exception to
	 * signal the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException if the database could be neither opened or created
	 */
	public ItemsDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}


	/**
	 * Create a new item using the title and hours provided. If the item is
	 * successfully created return the new rowId for that item, otherwise return
	 * a -1 to indicate failure.
	 * 
	 * @param title the title of the item
	 * @param body the body of the item
	 * @return rowId or -1 if failed
	 */
	public long createItem(String title, String s_opens, String s_closes, String m_opens, String m_closes,
			String t_opens, String t_closes, String w_opens, String w_closes, String th_opens, String th_closes,
			String f_opens, String f_closes, String sat_opens, String sat_closes) {
		
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TITLE, title);
		initialValues.put(KEY_S_OPENS, s_opens);
		initialValues.put(KEY_S_CLOSES, s_closes);
		initialValues.put(KEY_M_OPENS, m_opens);
		initialValues.put(KEY_M_CLOSES, m_closes);
		initialValues.put(KEY_T_OPENS, t_opens);
		initialValues.put(KEY_T_CLOSES, t_closes);
		initialValues.put(KEY_W_OPENS, w_opens);
		initialValues.put(KEY_W_CLOSES, w_closes);
		initialValues.put(KEY_TH_OPENS, th_opens);
		initialValues.put(KEY_TH_CLOSES, th_closes);
		initialValues.put(KEY_F_OPENS, f_opens);
		initialValues.put(KEY_F_CLOSES, f_closes);
		initialValues.put(KEY_SAT_OPENS, sat_opens);
		initialValues.put(KEY_SAT_CLOSES, sat_closes);

		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	public long createItem(String header) {
		ContentValues initialValues = new ContentValues();
		
		initialValues.put(KEY_TITLE, header);
		initialValues.put(KEY_S_OPENS, "");
		initialValues.put(KEY_S_CLOSES, "");
		initialValues.put(KEY_M_OPENS, "");
		initialValues.put(KEY_M_CLOSES, "");
		initialValues.put(KEY_T_OPENS, "");
		initialValues.put(KEY_T_CLOSES, "");
		initialValues.put(KEY_W_OPENS, "");
		initialValues.put(KEY_W_CLOSES, "");
		initialValues.put(KEY_TH_OPENS, "");
		initialValues.put(KEY_TH_CLOSES, "");
		initialValues.put(KEY_F_OPENS, "");
		initialValues.put(KEY_F_CLOSES, "");
		initialValues.put(KEY_SAT_OPENS, "");
		initialValues.put(KEY_SAT_CLOSES, "");

		
		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	/**
	 * Delete the note with the given rowId
	 * 
	 * @param rowId id of note to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteItem(long rowId) {

		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public boolean deleteAllItems() {
		return mDb.delete(DATABASE_TABLE, null, null) > 0;
	}

	/**
	 * Return a Cursor over the list of all notes in the database
	 * 
	 * @return Cursor over all notes
	 */
	public Cursor fetchAllItems() {

		return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE, KEY_S_OPENS, KEY_S_CLOSES,
				KEY_M_OPENS, KEY_M_CLOSES, KEY_T_OPENS, KEY_T_CLOSES, KEY_W_OPENS, KEY_W_CLOSES, 
				KEY_TH_OPENS, KEY_TH_CLOSES, KEY_F_OPENS, KEY_F_CLOSES, KEY_SAT_OPENS, KEY_SAT_CLOSES}, null, null, null, null, null);
	}

	/**
	 * Return a Cursor positioned at the item that matches the given rowId
	 * 
	 * @param rowId id of item to retrieve
	 * @return Cursor positioned to matching item, if found
	 * @throws SQLException if item could not be found/retrieved
	 */
	public Cursor fetchItem(long rowId) throws SQLException {

		Cursor mCursor =

				mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE, KEY_S_OPENS, KEY_S_CLOSES,
						KEY_M_OPENS, KEY_M_CLOSES, KEY_T_OPENS, KEY_T_CLOSES, KEY_W_OPENS, KEY_W_CLOSES, 
						KEY_TH_OPENS, KEY_TH_CLOSES, KEY_F_OPENS, KEY_F_CLOSES, KEY_SAT_OPENS, KEY_SAT_CLOSES}, KEY_ROWID + "=" + rowId, null,
						null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	/**
	 * Update the note using the details provided. The note to be updated is
	 * specified using the rowId, and it is altered to use the title and body
	 * values passed in
	 * 
	 * @param rowId id of note to update
	 * @param title value to set note title to
	 * @param body value to set note body to
	 * @return true if the note was successfully updated, false otherwise
	 */
	public boolean updateNote(long rowId, String title, String s_opens, String s_closes, String m_opens, String m_closes,
			String t_opens, String t_closes, String w_opens, String w_closes, String th_opens, String th_closes,
			String f_opens, String f_closes, String sat_opens, String sat_closes) {
		ContentValues args = new ContentValues();
		args.put(KEY_TITLE, title);
		args.put(KEY_S_OPENS, s_opens);
		args.put(KEY_S_CLOSES, s_closes);
		args.put(KEY_M_OPENS, m_opens);
		args.put(KEY_M_CLOSES, m_closes);
		args.put(KEY_T_OPENS, t_opens);
		args.put(KEY_T_CLOSES, t_closes);
		args.put(KEY_W_OPENS, w_opens);
		args.put(KEY_W_CLOSES, w_closes);
		args.put(KEY_TH_OPENS, th_opens);
		args.put(KEY_TH_CLOSES, th_closes);
		args.put(KEY_F_OPENS, f_opens);
		args.put(KEY_F_CLOSES, f_closes);
		args.put(KEY_SAT_OPENS, sat_opens);
		args.put(KEY_SAT_CLOSES, sat_closes);

		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
}
