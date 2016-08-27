package com.example.lancer.daliymission;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

/**
 * Created by Lancer on 2016/8/22.
 */
public  class MyReceiver extends BroadcastReceiver {//接收廣播
    private MyDBhelper helper;
    private Cursor cursor;
    @Override
    public void onReceive(Context context, Intent intent) {//當收到廣播時把CheckBox的資料全部改為false
        helper = new MyDBhelper(context.getApplicationContext());
        cursor = helper.select();
        for (int i = 0;i<cursor.getCount();i++) {
            cursor.moveToPosition(i);
            helper.update(cursor.getInt(0),cursor.getString(1),false);
        }
    }
}

