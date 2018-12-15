package com.nothing.arooba.packagesender;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class NewScriptActivity extends Activity {
    private static final String TAG = "NewScriptActivity";

    private EditText scriptNameText;
    private EditText scriptRemarkText;
    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_script);
        final Data data = (Data) getApplication();

        scriptNameText = findViewById(R.id.scriptNameText);
        scriptRemarkText = findViewById(R.id.scriptRemarkText);
        saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String scriptName = scriptNameText.getText().toString();
                Log.d(TAG, "scriptName = " + scriptName);
                if (scriptName.isEmpty()) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(NewScriptActivity.this);
                    builder.setTitle("错误");
                    builder.setMessage("请输入脚本名称！");
                    builder.setCancelable(false);
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
                String scriptRemark = scriptRemarkText.getText().toString();
                Script script = new Script(scriptName, new ArrayList<Package>(), scriptRemark, 0);
                data.addScript(script);
                Toast.makeText(NewScriptActivity.this, scriptName + " 创建成功！", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}
