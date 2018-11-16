package com.nothing.arooba.packagesender;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    // 创建Script表
    private static final String CREATE_SCRIPT = "create table Script\n" +
            "(\n" +
            "  scriptID   integer not null\n" +
            "    primary key\n" +
            "                     autoincrement,\n" +
            "  name       text    not null,\n" +
            "  remark     text,\n" +
            "  executeNum integer default 0 not null\n" +
            ");";
    // 创建Package表
    private static final String CREATE_PACKAGE = "create table Package\n" +
            "(\n" +
            "  scriptID  integer not null\n" +
            "    constraint Package_Script_scriptID_fk\n" +
            "    references Script,\n" +
            "  packageID integer not null,\n" +
            "  url       text    not null,\n" +
            "  type      text    not null,\n" +
            "  params    text    not null,\n" +
            "  headers   text    not null,\n" +
            "  body      text    not null,\n" +
            "  constraint Package_pk\n" +
            "  primary key (scriptID, packageID)\n" +
            ");";
    // 环境
    private Context mContext;

    public DatabaseHelper(Context mContext, int version) {
        super(mContext, "PackageSender.bd", null, version);
        this.mContext = mContext;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SCRIPT);
        Log.d(TAG, "Script table created.");
        db.execSQL(CREATE_PACKAGE);
        Log.d(TAG, "Package table created.");
        Toast.makeText(mContext, "初始化成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Package");
        db.execSQL("drop table if exists Script");
        onCreate(db);
    }
}
