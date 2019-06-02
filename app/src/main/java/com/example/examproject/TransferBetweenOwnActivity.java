package com.example.examproject;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.example.examproject.models.Account;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.LinkedList;
import java.util.List;

public class TransferBetweenOwnActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "TBetweenOwnActivity";

    // region UI
    private ScrollView scrollView;
    private Button button;
    // endregion

    private List<Account> accounts = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_between_own);
        Log.d(TAG, "onCreate: called");

        init();
    }

    @Override
    public void onClick(View v) {
        scrollView.setVisibility(View.INVISIBLE);
    }

    private void fetchAccounts(String[] accountNames) {
        Log.d(TAG, "fetchAccounts: fetching accounts...");

        CollectionReference collectionReference =
                FirebaseFirestore.getInstance().collection("accounts");

        for (String name : accountNames) {
            collectionReference.document(name).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        Log.d(TAG, "onSuccess: account fetched");

                        accounts.add(documentSnapshot.toObject(Account.class));
                    } else {
                        Log.d(TAG, "onSuccess: document doesn't exist");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: something went wrong");
                }
            });
        }
        Log.d(TAG, "fetchAccounts: " + accounts.get(0).getType());
    }

    private void init() {
        Log.d(TAG, "initializing...");

        fetchAccounts(getIntent().getStringArrayExtra("accounts"));

        scrollView = findViewById(R.id.scrollViewTransferBetweenOwn);
        button = findViewById(R.id.buttonToAccount);
        button.setOnClickListener(this);
    }
}
