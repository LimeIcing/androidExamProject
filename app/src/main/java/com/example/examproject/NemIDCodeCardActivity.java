package com.example.examproject;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.examproject.models.Account;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class NemIDCodeCardActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "NemIDCodeCardActivity";

    private Button buttonNext;

    private Account account;
    private double amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nem_idcode_card);
        Log.d(TAG, "onCreate: called");
        init();
    }

    @Override
    public void onClick(View v) {
        DocumentReference documentReference = FirebaseFirestore.getInstance().document
                ("accounts/" + account.getOwner() + ":" + account.getAccountNumber());
        documentReference.update("balance", FieldValue.increment(-amount))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: Transaction complete");

                    Toast.makeText(NemIDCodeCardActivity.this,
                            "Transaction complete", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Log.d(TAG, "onComplete: something went wrong: " + task.getException());

                    Toast.makeText(NemIDCodeCardActivity.this,
                            "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void init() {
        Log.d(TAG, "initializing...");

        Bundle extras = getIntent().getExtras();
        account = extras.getParcelable("account");
        amount = extras.getDouble("amount");
        buttonNext = findViewById(R.id.buttonNemIDNext);
        buttonNext.setOnClickListener(this);
    }
}
