package com.example.examproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class NemIDActivity extends AppCompatActivity {

    final String TAG = "NemIDActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nem_id);
        Log.d(TAG, "onCreate: called");

        init();
    }

    private void init() {

    }
}
