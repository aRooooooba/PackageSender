package com.nothing.arooba.packagesender;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;

public class Data extends Application {
    private static final String TAG = "Data";

    private ArrayList<Script> scriptSet = new ArrayList<>();

    public ArrayList<Script> getScriptSet() {
        return scriptSet;
    }

    public ArrayList<Package> getPackageSet(int scriptIndex) {
        return scriptSet.get(scriptIndex).getPackageSet();
    }

    public int getScriptSetSize() {
        return scriptSet.size();
    }

    public int getPackageSetSize(int scriptIndex) {
        return getPackageSet(scriptIndex).size();
    }

    public void addScript(int index, Script script) {
        DatabaseHelper dbHelper = new DatabaseHelper(this, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (index != getScriptSetSize()) {
            // 需要有插入操作
            String sql = String.format(Locale.getDefault(),
                    "update Script set scriptID = scriptID + %d where scriptID >= %d", getScriptSetSize() + 1, index);
            Log.v(TAG, "sql: " + sql);
            db.execSQL(sql);
            sql = String.format(Locale.getDefault(),
                    "update Script set scriptID = scriptID - %d where scriptID > %d", getScriptSetSize(), index);
            Log.v(TAG, "sql: " + sql);
            db.execSQL(sql);
        }
        ContentValues values = new ContentValues();
        values.put("scriptID", index);
        values.put("name", script.getName());
        values.put("remark", script.getRemark());
        db.insert("Script", null, values);  // save the data
        int i = 0;
        for (Package pck : script.getPackageSet()) {
            addPackage(index, i, pck);
            i++;
        }
        Log.d(TAG, "insert succeeded, id: " + getScriptSetSize() + ", name: " + script.getName());
        scriptSet.add(index, script);
        db.close();
    }

    public void addPackage(int scriptIndex, int packageIndex, Package pck) {
        Log.d(TAG, "scriptIndex: " + scriptIndex);
        Log.d(TAG, "packageIndex: " + packageIndex);
        DatabaseHelper dbHelper = new DatabaseHelper(this, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Script script = scriptSet.get(scriptIndex);
        if (packageIndex != script.getPackageSet().size()) {
            // 插入
            String sql = String.format(Locale.getDefault(),
                    "update Package set packageID = packageID + %d where scriptID = %d and packageID >= %d",
                    script.getPackageSet().size() + 1, scriptIndex, packageIndex);
            Log.v(TAG, "sql: " + sql);
            db.execSQL(sql);
            sql = String.format(Locale.getDefault(),
                    "update Package set packageID = packageID - %d where scriptID = %d and packageID > %d",
                    script.getPackageSet().size(), scriptIndex, packageIndex);
            Log.v(TAG, "sql: " + sql);
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
        script.getPackageSet().add(packageIndex, pck);
        db.close();
    }

    public void deleteScript(int index) {
        DatabaseHelper dbHelper = new DatabaseHelper(this, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("Package", "scriptID=?", new String[]{String.valueOf(index)});
        db.delete("Script", "scriptID=?", new String[]{String.valueOf(index)});
        if (index != getScriptSetSize()) {
            String sql = "update Package set scriptID = scriptID - 1 where scriptID > " + index;
            Log.v(TAG, "sql: " + sql);
            db.execSQL(sql);
            sql = "update Script set scriptID = scriptID - 1 where scriptID > " + index;
            Log.v(TAG, "sql: " + sql);
            db.execSQL(sql);
        }
        scriptSet.remove(index);
        db.close();
    }

    public void deletePackage(int scriptIndex, int packageIndex) {
        DatabaseHelper dbHelper = new DatabaseHelper(this, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = String.format(Locale.getDefault(),
                "delete from Package where scriptID = %d and packageID = %d", scriptIndex, packageIndex);
        Log.v(TAG, "sql: " + sql);
        db.execSQL(sql);
        if (packageIndex != getPackageSetSize(scriptIndex)) {
            sql = String.format(Locale.getDefault(),
                    "update Package set packageID = packageID - 1 where scriptID = %d and packageID > %d", scriptIndex, packageIndex);
            Log.v(TAG, "sql: " + sql);
            db.execSQL(sql);
        }
        getPackageSet(scriptIndex).remove(packageIndex);
        db.close();
    }

    public void updateScript(int oldIndex, int newIndex, Script script) {
        deleteScript(oldIndex);
        addScript(newIndex, script);
    }

    public void updatePackage(int scriptIndex, int oldPackageIndex, int newPackageIndex, Package pck) {
        deletePackage(scriptIndex, oldPackageIndex);
        addPackage(scriptIndex, newPackageIndex, pck);
    }

    public void refreshExecuteTime(int index) {
        DatabaseHelper dbHelper = new DatabaseHelper(this, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = String.format(Locale.getDefault(),
                "update Script set executeNum = %d where scriptID = %d", scriptSet.get(index).getExecuteNum(), index);
        Log.v(TAG, "sql: " + sql);
        db.execSQL(sql);
        db.close();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadData();
    }

    private void loadData() {
        DatabaseHelper dbHelper = new DatabaseHelper(this, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // Test whether the database is accessible. If not, init database.
        for (int i = 0; ; i++) {
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
                        packageSet.add(new Package(url, type, params, headers, body));
                    } while (cursor.moveToNext());
                    cursor.close();
                } else {
                    Log.e(TAG, "第" + scriptID + "个脚本没有请求！");
                }
                scriptSet.add(new Script(name, packageSet, remark, executeNum));
            } else {
                Log.d(TAG, "有" + i + "个脚本");
                break;
            }
        }
        db.close();
    }
}
