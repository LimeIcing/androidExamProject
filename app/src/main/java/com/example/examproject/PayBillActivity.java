package com.example.examproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class PayBillActivity extends AppCompatActivity {

    private final String TAG = "PayBillActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_bill);
        Log.d(TAG, "onCreate: called");
        init();
    }

    private void init() {
        Log.d(TAG, "initializing...");
    }
}
