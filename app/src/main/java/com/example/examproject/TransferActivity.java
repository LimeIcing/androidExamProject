package com.example.examproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class TransferActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "TransferActivity";

    private Button buttonTransferBetweenOwnAccounts, buttonTransferToOthers;

    private String[] accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        Log.d(TAG, "onCreate: called");

        init();
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.buttonTransferBetweenOwnAccounts:
                Log.d(TAG, "onClick: transfer between own pressed");

                intent = new Intent(this, TransferBetweenOwnActivity.class);
                intent.putExtra("accounts", accounts);
                startActivity(intent);

                break;

            case R.id.buttonTransferToOthers:
                Log.d(TAG, "onClick: transfer to others pressed");

                intent = new Intent(this, TransferToExternalActivity.class);
                intent.putExtra("accounts", accounts);
                startActivity(intent);

                break;
        }
    }

    private void init() {
        Log.d(TAG, "initializing...");

        accounts = getIntent().getStringArrayExtra("accounts");

        buttonTransferBetweenOwnAccounts = findViewById(R.id.buttonTransferBetweenOwnAccounts);
        buttonTransferBetweenOwnAccounts.setOnClickListener(this);
        buttonTransferToOthers = findViewById(R.id.buttonTransferToOthers);
        buttonTransferToOthers.setOnClickListener(this);
    }
}
