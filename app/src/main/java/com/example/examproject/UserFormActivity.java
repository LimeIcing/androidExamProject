package com.example.examproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Spinner;

import java.util.Calendar;

public class UserFormActivity extends AppCompatActivity {

    private final String TAG = "UserFormActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);
        Log.d(TAG, "onCreate: called");

        init();
    }

    private void init() {
        Log.d(TAG, "initializing...");

    }
}
