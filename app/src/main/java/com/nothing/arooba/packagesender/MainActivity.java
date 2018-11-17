package com.nothing.arooba.packagesender;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private FloatingActionButton newScriptBtn;
    private static final String TAG = "MainActivity";
    private ArrayList<Script> scriptSet = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView scriptList = (ListView) findViewById(R.id.scriptList);

        dbHelper = new DatabaseHelper(this, 1);
        dbHelper.getWritableDatabase();
        initData();
        ScriptAdapter scriptAdapter = new ScriptAdapter(MainActivity.this, R.layout.script_item, scriptSet);
        scriptList.setAdapter(scriptAdapter);

        // newScriptBtn = (FloatingActionButton) findViewById(R.id.newScriptButton);

        newScriptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (int i = 1;;i++) {
            // 查询
            Cursor cursor = db.query("Script", null, "scriptID=?", new String[]{String.valueOf(i)}, null, null, null);
            if (cursor.moveToFirst()) {
                int scriptID = cursor.getInt(cursor.getColumnIndex("scriptID"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String remark = cursor.getString(cursor.getColumnIndex("remark"));
                int executeNum = cursor.getInt(cursor.getColumnIndex("executeNum"));
                cursor.close();
                cursor = db.query("Package", null, "scriptID", new String[]{String.valueOf(i)}, null, null, "packageID");
                if (cursor.moveToFirst()) {
                    ArrayList<Package> packageSet = new ArrayList<>();
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
                    scriptSet.add(new Script(scriptID, name, packageSet, remark, executeNum));
                } else {
                    Log.e(TAG, "第" + String.valueOf(scriptID) + "个脚本没有请求！");
                    break;
                }
            } else {
                Log.d(TAG, "有" + String.valueOf(i) + "个脚本");
                break;
            }
        }
    }
}
