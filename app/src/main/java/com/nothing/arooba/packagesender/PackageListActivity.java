package com.nothing.arooba.packagesender;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class PackageListActivity extends AppCompatActivity {

    private static final String TAG = "PackageListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_list);
        Data data = (Data) getApplication();

        Intent intent = getIntent();
        int index = intent.getIntExtra("index", -1);
        Log.d(TAG, "index: " + index);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        ListView packageList = findViewById(R.id.packageList);
        ArrayList<Script> scriptSet = data.getScriptSet();
        PackageAdapter packageAdapter = new PackageAdapter(PackageListActivity.this,
                R.layout.package_item, scriptSet.get(index).getPackageSet());
        packageList.setAdapter(packageAdapter);

        Button editBtn = findViewById(R.id.editBtn);
        TextView titleText = findViewById(R.id.titleText);
        Button deleteBtn = findViewById(R.id.deleteBtn);

    }
}
