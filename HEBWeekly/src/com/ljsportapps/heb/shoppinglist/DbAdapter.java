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

package com.ljsportapps.heb.shoppinglist;

import java.util.Date;

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
public class DbAdapter {

    public static final String KEY_TITLE = "title";
    public static final String KEY_PRICE = "price";
    public static final String KEY_MORE = "more";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_ROWID = "_id";
    
    public static final String KEY_CREATE_DATE = "create_date";
    public static final String KEY_MODIFIED_DATE = "modified_date";
    public static final String KEY_IS_EXPIRED = "is_expired";
    

    private static final String TAG = "DbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
        "create table notes (_id integer primary key autoincrement, "
        + "title text not null, price text, more text, image text, is_expired text, create_date text not null, modified_date text);";

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "notes";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            //context.deleteDatabase(DATABASE_NAME);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public DbAdapter(Context ctx) {
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
    public DbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new note using the title and body provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     * 
     * @param title the title of the list
     * @param body the price of the list
     * @return rowId or -1 if failed
     */
    public long createNote(String title, String price, String more, String imgLink, Integer createDate) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_PRICE, price);
        initialValues.put(KEY_MORE, more);
        initialValues.put(KEY_IMAGE, imgLink);
        initialValues.put(KEY_IS_EXPIRED, "0");
        initialValues.put(KEY_IMAGE, imgLink);
        initialValues.put(KEY_CREATE_DATE, String.valueOf(new Date()));
        
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the note with the given rowId
     * 
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteNote(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all notes in the database
     * 
     * @return Cursor over all notes
     */
    public Cursor fetchAllNotes() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,
                KEY_PRICE, KEY_MORE, KEY_IMAGE, KEY_IS_EXPIRED}, null, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the note that matches the given rowId
     * 
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchNote(long rowId) throws SQLException {
        Cursor mCursor =
            mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                    KEY_TITLE, KEY_PRICE,KEY_MORE, KEY_IMAGE, KEY_IS_EXPIRED}, KEY_ROWID + "=" + rowId, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Update the list
     * We just want to check if it's expired or not
     * @param rowId id of note to update
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateNote(long rowId) {
        ContentValues args = new ContentValues();
        args.put(KEY_IS_EXPIRED, "1");
        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
