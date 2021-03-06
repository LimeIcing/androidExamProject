package com.example.examproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.examproject.models.Account;

public class NemIDActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "NemIDActivity";

    // region UI
    private TextView textViewFromAccount, textViewToAccount, textViewAmount;
    private Button buttonNext, buttonCancel;
    // endregion

    private Account fromAccount;
    private double amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nem_id);
        Log.d(TAG, "onCreate: called");
        init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonNext:
                Log.d(TAG, "onClick: next pressed");

                Intent intent = new Intent(this, NemIDCodeCardActivity.class);
                intent.putExtra("account", fromAccount);
                intent.putExtra("amount", amount);
                startActivity(intent);
                finish();

                break;

            case R.id.buttonCancel:
                Log.d(TAG, "onClick: cancel pressed");

                finish();

                break;
        }
    }

    private void init() {
        Log.d(TAG, "initializing...");

        Bundle extras = getIntent().getExtras();

        fromAccount = extras.getParcelable("fromAccount");
        String toAccount = extras.getString("toAccount");
        amount = extras.getDouble("amount");

        textViewFromAccount = findViewById(R.id.textViewFromAccount);
        String textViewText = "From:\n" + fromAccount.getRegistrationNumber()
                + " " + fromAccount.getAccountNumber();
        textViewFromAccount.setText(textViewText);

        textViewToAccount = findViewById(R.id.textViewToAccount);
        textViewText = "To:\n" + toAccount;
        textViewToAccount.setText(textViewText);

        textViewAmount = findViewById(R.id.textViewAmount);
        textViewText = "Amount to be transferred:\n" + amount;
        textViewAmount.setText(textViewText);

        buttonNext = findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(this);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(this);
    }
}
