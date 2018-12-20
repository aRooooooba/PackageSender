package com.nothing.arooba.packagesender;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;

public class NewPackageActivity extends AppCompatActivity {
    private static final String TAG = "NewPackageActivity";
    private final int WRONG_NO = 1;
    private final int WRONG_URL = 2;

    private final int TYPE_GET = 0;
    private final int TYPE_POST = 1;

    private String spinSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_package);

        final Data data = (Data) getApplication();
        final EditText packageNoText = findViewById(R.id.packageNoText);
        final EditText urlText = findViewById(R.id.urlText);
        final Spinner typeSpin = findViewById(R.id.typeSpin);
        typeSpin.setSelection(TYPE_GET, true);
        final EditText paramsText = findViewById(R.id.paramsText);
        final EditText headersText = findViewById(R.id.headersText);
        final EditText bodyText = findViewById(R.id.bodyText);

        final int scriptIndex = getIntent().getIntExtra("scriptIndex", -1);
        final int packageIndex = getIntent().getIntExtra("packageIndex", -1);
        Log.d(TAG, "scriptIndex: " + scriptIndex);
        if (packageIndex != -1) {   // 这个页面是长按后出来的
            Package pck = data.getPackageSet(scriptIndex).get(packageIndex);
            packageNoText.setText(String.valueOf(packageIndex + 1));
            urlText.setText(pck.getUrl());
            if (pck.getType().equals("GET")) {
                typeSpin.setSelection(TYPE_GET, true);
            } else if (pck.getType().equals("POST")) {
                typeSpin.setSelection(TYPE_POST, true);
            }
            paramsText.setText(pck.getParams());
            headersText.setText(pck.getHeaders());
            bodyText.setText(pck.getBody());
            Button deleteBtn = findViewById(R.id.deleteBtn);
            deleteBtn.setVisibility(View.VISIBLE);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewPackageActivity.this);
                    builder.setTitle("信息");
                    builder.setMessage("确定删除吗？");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            data.deletePackage(scriptIndex, packageIndex);
                            Toast.makeText(NewPackageActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.show();
                }
            });
        }

        typeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == TYPE_GET) {
                    spinSelection = "GET";
                } else if (position == TYPE_POST) {
                    spinSelection = "POST";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int packageNo = data.getPackageSetSize(scriptIndex);
                if (!packageNoText.getText().toString().isEmpty()) {
                    packageNo = Integer.parseInt(packageNoText.getText().toString());
                    if (packageNo <= 0 || packageNo > data.getPackageSetSize(scriptIndex) + 1) {
                        showAlert(WRONG_NO);
                        return;
                    }
                    packageNo--;
                }
                Log.d(TAG, "packageNo: " + packageNo);
                String url = urlText.getText().toString();
                if (url.isEmpty()) {
                    showAlert(WRONG_URL);
                    return;
                }
                String params = paramsText.getText().toString();
                String headers = headersText.getText().toString();
                String body = bodyText.getText().toString();
                Package pck1 = new Package(url, spinSelection, params, headers, body);
                if (packageIndex != -1) {
                    if (packageNo == data.getPackageSetSize(scriptIndex)) {
                        packageNo--;
                    }
                    Package pck2 = data.getPackageSet(scriptIndex).get(packageIndex);
                    if (packageNo == packageIndex && pck1.equals(pck2)) {
                        Log.d(TAG, "没有修改，直接退出。");
                    } else {
                        data.updatePackage(scriptIndex, packageIndex, packageNo, pck1);
                        Toast.makeText(NewPackageActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    data.addPackage(scriptIndex, packageNo, pck1);
                    Toast.makeText(NewPackageActivity.this, "添加成功！", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }

    private void showAlert(int flag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewPackageActivity.this);
        builder.setTitle("错误");
        if (flag == WRONG_NO) {
            builder.setMessage("请输入合法的序号！\n不输入默认为追加！");
        } else if (flag == WRONG_URL) {
            builder.setMessage("请输入目标网址！");
        }
        builder.setPositiveButton("确认", null);
        builder.show();
    }
}
