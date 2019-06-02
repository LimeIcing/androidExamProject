package com.example.examproject;

import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examproject.models.Account;
import com.example.examproject.models.AccountType;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TransferBetweenOwnActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "TBetweenOwnActivity";

    // region UI
    private ScrollView scrollView;
    private LinearLayout linearLayout;
    private TextView textViewFromAccountName, textViewFromAccountBalance, textViewToAccountName,
            textViewToAccountBalance;
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
        String tag = v.getTag().toString();
        Log.d(TAG, "onClick: element with tag " + tag + " pressed");

        switch (tag) {
            case "From account":
                scrollView.setVisibility(View.VISIBLE);
                isChanginFromAccount = true;
                break;

            case "To account":
                scrollView.setVisibility(View.VISIBLE);
                isChanginFromAccount = false;
                break;

            default:
                scrollView.setVisibility(View.INVISIBLE);
                linearLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

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

        fetchAccounts(getIntent().getStringArrayExtra("accounts"));
    }
}
