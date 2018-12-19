package com.nothing.arooba.packagesender;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshLayout();

        FloatingActionButton newScriptFAB = findViewById(R.id.newScriptFAB);
        newScriptFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewScriptActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshLayout();
    }

    private void refreshLayout() {
        final Data data = (Data) getApplication();
        ListView scriptList = findViewById(R.id.scriptList);
        final ArrayList<Script> scriptSet = data.getScriptSet();
        ScriptAdapter scriptAdapter = new ScriptAdapter(MainActivity.this, R.layout.script_item, scriptSet);
        scriptList.setAdapter(scriptAdapter);
        scriptList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, PackageListActivity.class);
                intent.putExtra("index", position);
                Log.d(TAG, "index: " + position);
                startActivity(intent);
                return true;
            }
        });
        scriptList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        if (scriptSet.get(position).executeScript()) {
                            Toast.makeText(MainActivity.this, "执行成功！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "发生错误，请检查网络！", Toast.LENGTH_SHORT).show();
                        }
                        Looper.loop();
                    }
                }).start();
                try {
                    Thread.sleep(500);
                    refreshLayout();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
