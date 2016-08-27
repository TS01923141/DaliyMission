package com.example.lancer.daliymission;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public MyDBhelper helper;
    private Cursor cursor;
    MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new MyDBhelper(getApplicationContext());//載入資料庫 儲存查詢資料庫內容 如果資料庫內沒有資料則新增預設並重新查詢資料庫內容
        cursor = helper.select();
        if(cursor.getCount()==0) {
            helper.insert("歡迎使用DaliyMission 左右滑動刪除任務 點選上列+新增任務", false);
            cursor.requery();
        }

        myAdapter = new MyAdapter(cursor);//在分配器中放入已查詢之資料庫內容
        RecyclerView mList = (RecyclerView) findViewById(R.id.recyclerView);//載入RecycleView
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);//設定RecycleView的顯示格式
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mList.setLayoutManager(layoutManager);
        mList.setAdapter(myAdapter);//在RecycleView中放入分配器以顯示資料

        //載入API用以設定click事件 API(RecyclerTouchListener)
        mList.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mList, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {//設定按下RecycleView的任意項目之後勾選/取消該項目 並修改資料庫的CheckBox值
                cursor.moveToPosition(position);
                if(cursor.getInt(2)==0) helper.update(cursor.getInt(0),cursor.getString(1),true);
                else helper.update(cursor.getInt(0),cursor.getString(1),false);
                cursor.requery();//重新查詢資料庫並刷新分配器以顯示最新資料
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));




        //Android內建功能 用以實現上下拖曳/左右滑動刪除功能
        ItemTouchHelper.Callback mCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.START|ItemTouchHelper.END) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                myAdapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(getDragDirs(recyclerView, viewHolder), getSwipeDirs(recyclerView, viewHolder));
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                cursor.moveToPosition(position);
                helper.delete(cursor.getInt(0));
                cursor.requery();
                myAdapter.notifyDataSetChanged();
            }
        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(mCallback);
        mItemTouchHelper.attachToRecyclerView(mList);


    }


    @Override//用以載入/新建工具列
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override//設定點選工具列項目後的各項處理
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {//抓取layout的item各元件名字
            case R.id.create_new://當按下create_new功能
                final View newitem = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_new_item, null);//初始化add_new_item這個layout
                new AlertDialog.Builder(MainActivity.this)//設定彈出畫面
                        .setTitle("輸入新增目標")
                        .setView(newitem)
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText editText = (EditText) newitem.findViewById(R.id.editText);//按下確定按鈕後以editText(自行輸入的內容)創建新的資料庫資料
                                if(!editText.getText().toString().equals("")){
                                    helper.insert(editText.getText().toString(),false);
                                    cursor.requery();//刷新資料庫與分配器
                                    myAdapter.notifyDataSetChanged();
                                    editText.setText("");//將editText設為空白 避免下次打開顯示上次輸入的內容
                                }
                            }
                        })
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
