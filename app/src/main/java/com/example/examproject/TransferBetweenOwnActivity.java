package com.example.examproject;

import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examproject.models.Account;
import com.example.examproject.models.AccountType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.Map;

public class TransferBetweenOwnActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "TBetweenOwnActivity";

    // region UI
    private ScrollView scrollView;
    private LinearLayout linearLayout;
    private TextView textViewFromAccountName, textViewFromAccountBalance, textViewToAccountName,
            textViewToAccountBalance;
    private EditText editTextAmount;
    private Button buttonTransfer;
    // endregion

    private Map<String, Account> accounts = new HashMap<>();
    private Account fromAccount;
    private Account toAccount;
    private Boolean isChanginFromAccount = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_between_own);
        Log.d(TAG, "onCreate: called");

        init();
    }

    @Override
    public void onClick(View v) {
        String tag = v.getTag().toString().toLowerCase();
        Log.d(TAG, "onClick: element with tag " + tag + " pressed");

        switch (tag) {
            case "from account":
                scrollView.setVisibility(View.VISIBLE);
                isChanginFromAccount = true;
                break;

            case "to account":
                scrollView.setVisibility(View.VISIBLE);
                isChanginFromAccount = false;
                break;

            case "transfer money":
                transfer();
                break;

            default:
                scrollView.setVisibility(View.INVISIBLE);
                linearLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                try {
                    if (isChanginFromAccount) {
                        fromAccount = accounts.get(tag);
                        textViewFromAccountName.setText(fromAccount.getType().toString());
                        String tVFABText = "Balance: " + fromAccount.getBalance();
                        textViewFromAccountBalance.setText(tVFABText);
                    } else {
                        toAccount = accounts.get(tag);
                        textViewToAccountName.setText(toAccount.getType().toString());
                        textViewToAccountName.setVisibility(View.VISIBLE);
                        String tVTABText = "Balance: " + toAccount.getBalance();
                        textViewToAccountBalance.setText(tVTABText);
                        textViewToAccountBalance.setVisibility(View.VISIBLE);
                        editTextAmount.setVisibility(View.VISIBLE);
                        buttonTransfer.setVisibility(View.VISIBLE);
                    }
                } catch (NullPointerException eNP) {
                    eNP.printStackTrace();
                }

                break;
        }
    }

    private void fetchAccounts(final String[] accountNames) {
        Log.d(TAG, "fetchAccounts: fetching accounts...");

        CollectionReference collectionReference =
                FirebaseFirestore.getInstance().collection("accounts");

        for (String name : accountNames) {
            collectionReference.document(name).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        Log.d(TAG, "onSuccess: account fetched");

                        Account account = documentSnapshot.toObject(Account.class);

                        Button button = new Button(TransferBetweenOwnActivity.this);
                        button.setLayoutParams(new LinearLayout.LayoutParams
                                (LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT));
                        button.setPadding(16, 16, 16, 16);
                        button.setBackgroundTintList(ColorStateList
                                .valueOf(getResources().getColor(R.color.colorWhite)));
                        button.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        button.setAllCaps(false);
                        button.setTextAppearance(TransferBetweenOwnActivity.this,
                                android.R.style.TextAppearance_Medium);
                        button.setOnClickListener(TransferBetweenOwnActivity.this);

                        try {
                            String bText = account.getType() + " - balance: " + account.getBalance();
                            button.setText(bText);
                            String regAndAccNumber = account.getRegistrationNumber() + ":" + account.getAccountNumber();
                            button.setTag(regAndAccNumber);
                            linearLayout.addView(button);

                            accounts.put(regAndAccNumber, account);

                            if (account.getType().equals(AccountType.DEFAULT)) {
                                fromAccount = account;
                                textViewFromAccountName
                                        .setText(account.getType().toString());
                                String tVFABText = "Balance: " + account.getBalance();
                                textViewFromAccountBalance
                                        .setText(tVFABText);
                            }
                        } catch (NullPointerException eNP) {
                            eNP.printStackTrace();
                            Log.d(TAG, "onSuccess: couldn't fetch account type");
                        }
                    } else {
                        Log.d(TAG, "onSuccess: document doesn't exist");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: something went wrong, " + e.toString());
                }
            });
        }
    }

    private void transfer() {
        Log.d(TAG, "transfer: transferring money...");

        final String fromAccountName = fromAccount.getOwner() + ":" + fromAccount.getAccountNumber();
        final String toAccountName = toAccount.getOwner() + ":" + toAccount.getAccountNumber();
        WriteBatch batch = FirebaseFirestore.getInstance().batch();
        CollectionReference collectionReference =
                FirebaseFirestore.getInstance().collection("accounts");
        batch.update(collectionReference.document(fromAccountName),
                "balance",
                FieldValue.increment((Double.valueOf(editTextAmount.getText().toString()) * -1)));
        batch.update(collectionReference.document(toAccountName),
                "balance",
                FieldValue.increment(Double.valueOf(editTextAmount.getText().toString())));

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: successfully transferred "
                            + editTextAmount.getText() +
                            " from " + fromAccountName + " to " + toAccountName);

                    Toast.makeText(TransferBetweenOwnActivity.this,
                            "Transfer complete", Toast.LENGTH_LONG).show();

                    finish();
                } else {
                    Log.d(TAG, "onComplete: transaction failed " + task.getException());

                    Toast.makeText(TransferBetweenOwnActivity.this,
                            "Transfer failed. Try again later", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void init() {
        Log.d(TAG, "initializing...");

        textViewFromAccountName = findViewById(R.id.textViewFromAccountName);
        textViewFromAccountName.setOnClickListener(this);
        textViewFromAccountBalance = findViewById(R.id.textViewFromAccountBalance);
        textViewToAccountName = findViewById(R.id.textViewToAccountName);
        textViewToAccountName.setOnClickListener(this);
        textViewToAccountBalance = findViewById(R.id.textViewToAccountBalance);
        scrollView = findViewById(R.id.scrollViewTransferBetweenOwn);
        linearLayout = findViewById(R.id.linearLayoutTransferBetweenOwn);
        editTextAmount = findViewById(R.id.editTextAmount);
        buttonTransfer = findViewById(R.id.buttonTransferMoneyToOwn);
        buttonTransfer.setOnClickListener(this);

        fetchAccounts(getIntent().getStringArrayExtra("accounts"));
    }
}
