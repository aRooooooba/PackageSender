package com.nothing.arooba.packagesender;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int SEND_SUCCEEDED = 1;
    private static final int SEND_FAILED = 2;
    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case SEND_SUCCEEDED:
                    Toast.makeText(MainActivity.this, "执行成功！", Toast.LENGTH_SHORT).show();
                    refreshLayout();
                    break;
                case SEND_FAILED:
                    Toast.makeText(MainActivity.this, "发生错误，请检查网络！", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, "ca-app-pub-2710237255954645~3915916280");

        refreshLayout();

        FloatingActionButton newScriptFAB = findViewById(R.id.newScriptFAB);
        newScriptFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewScriptActivity.class);
                startActivity(intent);
            }
        });

        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
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
                        Message message = new Message();
                        if (scriptSet.get(position).executeScript()) {
                            data.refreshExecuteTime(position);
                            message.what = SEND_SUCCEEDED;
                        } else {
                            message.what = SEND_FAILED;
                        }
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });
    }
}
