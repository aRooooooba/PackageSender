package com.nothing.arooba.packagesender;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private FloatingActionButton newScriptFAB;
    private ListView scriptList;
    private static final String TAG = "MainActivity";
    private ArrayList<Script> scriptSet = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scriptList = findViewById(R.id.scriptList);

        dbHelper = new DatabaseHelper(this, 1);
        dbHelper.getWritableDatabase();
        refreshLayout();

        scriptList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });

        newScriptFAB = findViewById(R.id.newScriptFAB);

        newScriptFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewScriptActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String scriptName = data.getStringExtra("scriptName");
                    String scriptRemark = data.getStringExtra("scriptRemark");
                    Log.d(TAG, "scriptName: " + scriptName);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    // 组装数据
                    values.put("scriptID", scriptSet.size() + 1);
                    values.put("name", scriptName);
                    values.put("remark", scriptRemark);
                    db.insert("Script", null, values);
                    refreshLayout();
                }
        }
    }

    private void refreshLayout() {
        initData();
        ScriptAdapter scriptAdapter = new ScriptAdapter(MainActivity.this, R.layout.script_item, scriptSet);
        scriptList.setAdapter(scriptAdapter);
    }

    private void initData() {
        scriptSet.clear();  // remove all elements
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (int i = 1;;i++) {
            // 查询
            Cursor cursor = db.query("Script", null, "scriptID=?", new String[]{String.valueOf(i)}, null, null, null);
            if (cursor.moveToFirst()) {
                int scriptID = cursor.getInt(cursor.getColumnIndex("scriptID"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String remark = cursor.getString(cursor.getColumnIndex("remark"));
                int executeNum = cursor.getInt(cursor.getColumnIndex("executeNum"));
                ArrayList<Package> packageSet = new ArrayList<>();
                cursor.close();
                cursor = db.query("Package", null, "scriptID=?", new String[]{String.valueOf(i)}, null, null, "packageID");
                if (cursor.moveToFirst()) {
                    do {
                        // 遍历Package
                        String url = cursor.getString(cursor.getColumnIndex("url"));
                        String type = cursor.getString(cursor.getColumnIndex("type"));
                        String params = cursor.getString(cursor.getColumnIndex("params"));
                        String headers = cursor.getString(cursor.getColumnIndex("headers"));
                        String body = cursor.getString(cursor.getColumnIndex("body"));
                        cursor.close();
                        packageSet.add(new Package(url, type, params, headers, body));
                    } while (cursor.moveToNext());
                } else {
                    Log.e(TAG, "第" + scriptID + "个脚本没有请求！");
                }
                scriptSet.add(new Script(scriptID, name, packageSet, remark, executeNum));
            } else {
                Log.d(TAG, "有" + (i-1) + "个脚本");
                break;
            }
        }
        for (Script script : scriptSet) {
            Log.d(TAG, script.getName());
        }
    }
}
