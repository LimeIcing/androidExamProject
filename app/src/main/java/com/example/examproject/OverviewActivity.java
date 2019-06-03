package com.example.examproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.examproject.models.User;

public class OverviewActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "OverviewActivity";

    // region UI
    private TextView textViewCurrentUser;
    private Button buttonManageAccounts, buttonPayBill, buttonTransferMoney, buttonUserProfile;
    // endregion

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        Log.d(TAG, "onCreate() called");
        init();
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.buttonManageAccounts:
                Log.d(TAG, "onClick: manage accounts pressed");
                break;

            case R.id.buttonTransferMoney:
                Log.d(TAG, "onClick: transfer money pressed");

                intent = new Intent(this, TransferActivity.class);
                intent.putExtra("accounts", currentUser.getAccounts().toArray(new String[0]));
                startActivity(intent);

                break;

            case R.id.buttonPayBill:
                Log.d(TAG, "onClick: pay bill pressed");

                intent = new Intent(this, PayBillActivity.class);
                intent.putExtra("accounts", currentUser.getAccounts().toArray(new String[0]));
                startActivity(intent);

                break;

            case R.id.buttonUserProfile:
                Log.d(TAG, "onClick: user profile pressed");

                intent = new Intent(this, UserProfileActivity.class);
                intent.putExtra("user", currentUser);
                startActivity(intent);

                break;
        }
    }

    private void init(){
        Log.d(TAG, "Initializing...");

        textViewCurrentUser = findViewById(R.id.textViewCurrentUser);
        currentUser = getIntent().getParcelableExtra("user");
        String greeting = "Hello, " + currentUser.getFirstName() + " " + currentUser.getLastName();
        textViewCurrentUser.setText(greeting);

        buttonManageAccounts = findViewById(R.id.buttonManageAccounts);
        buttonManageAccounts.setOnClickListener(this);
        buttonPayBill = findViewById(R.id.buttonPayBill);
        buttonPayBill.setOnClickListener(this);
        buttonTransferMoney = findViewById(R.id.buttonTransferMoney);
        buttonTransferMoney.setOnClickListener(this);
        buttonUserProfile = findViewById(R.id.buttonUserProfile);
        buttonUserProfile.setOnClickListener(this);
    }
}
