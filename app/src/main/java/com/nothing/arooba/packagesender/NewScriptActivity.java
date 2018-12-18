package com.nothing.arooba.packagesender;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class NewScriptActivity extends Activity {
    private static final String TAG = "NewScriptActivity";
    private final int WRONG_NO = 1;
    private final int WRONG_NAME = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_script);

        final Data data = (Data) getApplication();
        final EditText scriptNoText = findViewById(R.id.scriptNoText);
        final EditText scriptNameText = findViewById(R.id.scriptNameText);
        final EditText scriptRemarkText = findViewById(R.id.scriptRemarkText);

        final int index = getIntent().getIntExtra("index", -1);
        Log.d(TAG, "index: " + index);
        if (index != -1) {  // 代表这个页面是点击编辑后出来的
            Script script = data.getScriptSet().get(index);
            scriptNoText.setText(String.valueOf(index + 1));
            scriptNameText.setText(script.getName());
            scriptRemarkText.setText(script.getRemark());
        }

        Button saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int scriptNo = data.getSize();
                if (!scriptNoText.getText().toString().isEmpty()) {
                    scriptNo = Integer.parseInt(scriptNoText.getText().toString());
                    if (scriptNo <= 0 || scriptNo > data.getSize()+1) {
                        showAlert(WRONG_NO);
                        return;
                    }
                    scriptNo--;
                }
                Log.d(TAG, "scriptNo: " + scriptNo);
                String scriptName = scriptNameText.getText().toString();
                Log.d(TAG, "scriptName = " + scriptName);
                if (scriptName.isEmpty()) {
                    showAlert(WRONG_NAME);
                    return;
                }
                String scriptRemark = scriptRemarkText.getText().toString();
                Script script1 = new Script(scriptName, new ArrayList<Package>(), scriptRemark, 0);
                if (index != -1) {  // 更新一条已有的信息
                    if (scriptNo == data.getSize()) {
                        scriptNo--;
                    }
                    Intent intent = new Intent();
                    intent.putExtra("index_return", scriptNo);
                    setResult(RESULT_OK, intent);
                    Script script2 = data.getScriptSet().get(index);
                    if (scriptNo == index && script2.equals(script1)) {
                        Log.d(TAG, "没有修改，直接退出。");
                    } else {
                        data.updateScript(index, scriptNo, script1);
                        Toast.makeText(NewScriptActivity.this, scriptName + " 修改成功！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    data.addScript(scriptNo, script1);
                    Toast.makeText(NewScriptActivity.this, scriptName + " 创建成功！", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }

    private void showAlert(int flag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewScriptActivity.this);
        builder.setTitle("错误");
        if (flag == WRONG_NAME) {
            builder.setMessage("请输入脚本名称！");
        } else if (flag == WRONG_NO) {
            builder.setMessage("请输入合法的序号！\n不输入默认为追加！");
        }
        builder.setCancelable(false);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
