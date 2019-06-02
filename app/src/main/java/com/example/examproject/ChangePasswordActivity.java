package com.example.examproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "ChangePasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Log.d(TAG, "onCreate: called");

        init();
    }

    @Override
    public void onClick(View v) {

    }

    private void init() {
        Log.d(TAG, "initializing...");
    }
}
