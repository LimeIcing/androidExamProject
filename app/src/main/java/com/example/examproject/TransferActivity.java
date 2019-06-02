package com.example.examproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class TransferActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "TransferActivity";

    private Button buttonTransferBetweenOwnAccounts, buttonTransferToOthers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        Log.d(TAG, "onCreate: called");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonTransferBetweenOwnAccounts:
                Log.d(TAG, "onClick: transfer between own pressed");

                break;

            case R.id.buttonTransferToOthers:
                Log.d(TAG, "onClick: transfer to others pressed");

                break;
        }
    }

    private void init() {
        Log.d(TAG, "initializing...");

        buttonTransferBetweenOwnAccounts = findViewById(R.id.buttonTransferBetweenOwnAccounts);
        buttonTransferBetweenOwnAccounts.setOnClickListener(this);
        buttonTransferToOthers = findViewById(R.id.buttonTransferToOthers);
        buttonTransferToOthers.setOnClickListener(this);
    }
}
