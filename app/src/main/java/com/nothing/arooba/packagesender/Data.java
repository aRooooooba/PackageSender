package com.nothing.arooba.packagesender;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class Data extends Application {
    private static final String TAG = "Data";

    private ArrayList<Script> scriptSet = new ArrayList<>();

    public ArrayList<Script> getScriptSet() {
        return scriptSet;
    }

    public void addScript(Script script) {
        DatabaseHelper dbHelper = new DatabaseHelper(this, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("scriptID", scriptSet.size());
        values.put("name", script.getName());
        values.put("remark", script.getRemark());
        db.insert("Script", null, values);  // save the data
        Log.d(TAG, "insert succeeded, id: " + scriptSet.size() + ", name: " + script.getName());
        scriptSet.add(script);
    }

    public void addPackage(int scriptIndex, Package pck, int packageIndex) {
        Log.d(TAG, "scriptIndex: " + scriptIndex);
        Log.d(TAG, "packageIndex: " + packageIndex);
        DatabaseHelper dbHelper = new DatabaseHelper(this, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Script script = scriptSet.get(scriptIndex);
        if (packageIndex == -1) {
            script.getPackageSet().add(pck);
            packageIndex = script.getPackageSet().size();
        } else {
            script.getPackageSet().add(packageIndex, pck);
            String sql = "update Package set packageID = packageID + 1 where packageID >= " + packageIndex;
            Log.d(TAG, "sql: " + sql);
            db.execSQL(sql);
        }
        ContentValues values = new ContentValues();
        values.put("scriptID", scriptIndex);
        values.put("packageID", packageIndex);
        values.put("url", pck.getUrl());
        values.put("type", pck.getType());
        values.put("params", pck.getParams());
        values.put("headers", pck.getHeaders());
        values.put("body", pck.getBody());
        db.insert("Package", null, values);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadData();
    }

    private void loadData() {
        DatabaseHelper dbHelper = new DatabaseHelper(this, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // Test whether the database is accessible. If not, init database.
        for (int i = 0;;i++) {
            // 查询
            Cursor cursor = db.query("Script", null, "scriptID=?", new String[]{String.valueOf(i)}, null, null, "scriptID");
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
                scriptSet.add(new Script(name, packageSet, remark, executeNum));
            } else {
                Log.d(TAG, "有" + i + "个脚本");
                break;
            }
        }
        for (Script script : scriptSet) {
            Log.d(TAG, script.getName());
        }
    }
}
