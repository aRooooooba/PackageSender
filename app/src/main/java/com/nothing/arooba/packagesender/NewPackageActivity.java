package com.nothing.arooba.packagesender;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewPackageActivity extends AppCompatActivity {
    private static final String TAG = "NewPackageActivity";
    private final int WRONG_NO = 1;
    private final int WRONG_URL = 2;
    private final int WRONG_TYPE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_package);

        final Data data = (Data) getApplication();
        final EditText packageNoText = findViewById(R.id.packageNoText);
        final EditText urlText = findViewById(R.id.urlText);
        final EditText typeText = findViewById(R.id.typeText);
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
            typeText.setText(pck.getType());
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
                String type = typeText.getText().toString();
                if (type.isEmpty()) {
                    showAlert(WRONG_TYPE);
                    return;
                }
                String params = paramsText.getText().toString();
                String headers = headersText.getText().toString();
                String body = bodyText.getText().toString();
                Package pck1 = new Package(url, type, params, headers, body);
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
        } else if (flag == WRONG_TYPE) {
            builder.setMessage("请输入请求类型！");
        }
        builder.setPositiveButton("确认", null);
        builder.show();
    }
}
