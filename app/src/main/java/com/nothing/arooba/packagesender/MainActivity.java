package com.nothing.arooba.packagesender;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private FloatingActionButton newScriptBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this, 1);
        dbHelper.getWritableDatabase();

        newScriptBtn = (FloatingActionButton) findViewById(R.id.newScriptButton);

        newScriptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
