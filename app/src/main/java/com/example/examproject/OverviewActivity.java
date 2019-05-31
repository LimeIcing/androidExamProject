package com.example.examproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class OverviewActivity extends AppCompatActivity {

    private final String TAG = "OverviewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        Log.d(TAG, "onCreate() called");
        init();
    }

    private void init(){
        Log.d(TAG, "Initializing...");
    }
}
