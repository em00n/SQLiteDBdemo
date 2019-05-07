package com.emon.sqlitedbdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper{
    //Database Version
    private static final int DATABASE_VERSION = 2;
    //Database Name
    private static final String DATABASE_NAME = "Test";
    //Table Name
    private static final String TABLE_TEST = "TestTable";
    //Column Name
    private static final String KEY_ID = "ID";
    private static final String KEY_NAME = "name";
    private static final String KEY_AGE = "age";

    private static FavoriteList favoriteList;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Create Table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_TEST + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_AGE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEST);
        onCreate(db);
    }

    //Insert Value
    public void adddata(Context context,String movieId,String songId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, movieId);
        values.put(KEY_AGE, songId);
        db.insert(TABLE_TEST, null, values);
        db.close();
    }

    //Get Row Count
    public int getCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TEST;
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if(cursor != null && !cursor.isClosed()){
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    //Delete Query
    public void removeFav(int id) {
        String countQuery = "DELETE FROM " + TABLE_TEST + " where " + KEY_ID + "= " + id ;
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(countQuery);
    }

//    //Update Query
//    public static boolean updateItem(Context context,String dishName, String dishPrice) {
//
//        DatabaseHandler dbHelper = new DatabaseHandler(context);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        ContentValues values = new ContentValues();
//      //  values.put(dbHelper.KEY_ID, itemId);
//        values.put(dbHelper.KEY_NAME, dishName);
//        values.put(dbHelper.KEY_AGE, dishPrice);
//        //values.put(DBHelper.CART_DISH_QTY, dishQty);
//
//
//        try {
//
//            String[] args = new String[]{dishName};
//           // db.update(dbHelper.KEY_NAME, values, dbHelper.KEY_AGE + "=?", args);
//            db.update(TABLE_TEST, values, "ID = ?", new String[] {String.valueOf(favoriteList.getId())});
//            db.close();
//
//
//            return true;
//        } catch (SQLiteException e) {
//            db.close();
//
//            return false;
//        }
//
//
//    }

    public boolean updateDate(String id, String name, String age){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_AGE, age);
        contentValues.put(KEY_ID, id);

        db.update(TABLE_TEST,contentValues,KEY_ID +" = ?", new String[]{id});


        return true;
    }


    //Get FavList
    public List<FavoriteList> getFavList(){
        String selectQuery = "SELECT  * FROM " + TABLE_TEST;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<FavoriteList> FavList = new ArrayList<FavoriteList>();
        if (cursor.moveToFirst()) {
            do {
                FavoriteList list = new FavoriteList();
                list.setId(Integer.parseInt(cursor.getString(0)));
                list.setName(cursor.getString(1));
                list.setAge(cursor.getString(2));
                FavList.add(list);
            } while (cursor.moveToNext());
        }
        return FavList;
    }

}