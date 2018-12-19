package com.nothing.arooba.packagesender;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

public class PackageListActivity extends AppCompatActivity {

    private static final String TAG = "PackageListActivity";
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_list);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        index = getIntent().getIntExtra("index", -1);
        Log.d(TAG, "index: " + index);
        refreshLayout();

        Button editBtn = findViewById(R.id.editBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PackageListActivity.this, NewScriptActivity.class);
                intent.putExtra("index", index);
                startActivityForResult(intent, 1);
            }
        });

        Button deleteBtn = findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PackageListActivity.this);
                builder.setTitle("信息");
                builder.setMessage("确认删除吗？");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Data data = (Data) getApplication();
                        data.deleteScript(index);
                        Toast.makeText(PackageListActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
            }
        });

        FloatingActionButton newPackageFAB = findViewById(R.id.newPackageFAB);
        newPackageFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PackageListActivity.this, NewPackageActivity.class);
                intent.putExtra("scriptIndex", index);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            index = data.getIntExtra("index_return", index);
            Log.d(TAG, "index_return: " + index);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshLayout();
    }

     private void refreshLayout() {
         Data data = (Data) getApplication();
         ArrayList<Script> scriptSet = data.getScriptSet();
         Script script = scriptSet.get(index);

         TextView titleText = findViewById(R.id.titleText);
         titleText.setText(script.getName());

         ListView packageList = findViewById(R.id.packageList);
         PackageAdapter packageAdapter = new PackageAdapter(PackageListActivity.this,
                 R.layout.package_item, script.getPackageSet());
         packageList.setAdapter(packageAdapter);
         packageList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
             @Override
             public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                 Intent intent = new Intent(PackageListActivity.this, NewPackageActivity.class);
                 intent.putExtra("scriptIndex", index);
                 intent.putExtra("packageIndex", position);
                 startActivity(intent);
                 return true;
             }
         });
     }
}
