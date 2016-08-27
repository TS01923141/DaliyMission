package com.example.lancer.daliymission;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Lancer on 2016/8/24.
 */
public class MyDBhelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "mission_database";
    private final static int DATABASE_VERSION = 1;
    private final static String TABLE_NAME = "mission_table";
    private final static String FEILD_ID = "_id";
    private final static String FEILD_TEXT = "item_text";
    private final static String FEILD_Boolean = "item_boolean";
    private String sql =//設定資料庫的項目 PRIMARY KEY為主key 用以查詢/避免重複資料...
            "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"("+
                    FEILD_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    FEILD_TEXT+" TEXT,"+
                    FEILD_Boolean+" INTEGER"+
                    ")";
    private SQLiteDatabase database;

    public MyDBhelper(Context context) {//設定資料庫內容
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sql);
    }//如果目前沒有資料庫就建立資料庫

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }//更新資料庫

    public Cursor select(){//查詢資料庫資料
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }

    public void insert(String itemText,Boolean itemBoolean){//新增資料庫資料
        ContentValues values = new ContentValues();
        values.put(FEILD_TEXT, itemText);
        values.put(FEILD_Boolean, itemBoolean);
        database.insert(TABLE_NAME, null, values);
    }

    public void delete(int id){//查詢ID刪除該筆資料
        database.delete(TABLE_NAME, FEILD_ID + "=" + Integer.toString(id), null);
    }

    public void update(int id, String itemText,Boolean itemBoolean){//查詢ID修改該筆資料
        ContentValues values = new ContentValues();
        values.put(FEILD_TEXT, itemText);
        values.put(FEILD_Boolean,itemBoolean);
        database.update(TABLE_NAME, values, FEILD_ID + "=" + Integer.toString(id), null);
    }

    public void close(){
        database.close();
    }//關閉資料庫
}
