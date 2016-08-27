package com.example.lancer.daliymission;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by Lancer on 2016/8/20.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Cursor cursor;
    public class ViewHolder extends RecyclerView.ViewHolder {//讀入要顯示在RecycleView的項目
        public TextView mTextView;
        public CheckBox mChceckBox;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.info_text);
            mChceckBox = (CheckBox) v.findViewById(R.id.checkBox);
        }
    }

    public MyAdapter(Cursor mCursor) {
        cursor = mCursor;//讀入傳入的資料庫資料
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {//初始化要顯示在RecycleView的頁面
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        ViewHolder vh = new ViewHolder(v);//將頁面放入ViewHolder
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {//設定要放入RecycleView的項目
        cursor.moveToPosition(position);
        holder.mTextView.setText(cursor.getString(1));
        if((cursor.getInt(2)==0))holder.mChceckBox.setChecked(false);
        else holder.mChceckBox.setChecked(true);
    }

    @Override
    public int getItemCount() {//取得項目總數
        if (cursor != null) return cursor.getCount();
        else return 0;
    }
}
