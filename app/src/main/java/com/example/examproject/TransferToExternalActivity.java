package com.example.examproject;

import android.content.Intent;
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

import com.example.examproject.models.Account;
import com.example.examproject.models.AccountType;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TransferToExternalActivity extends AppCompatActivity implements View.OnClickListener {

    final String TAG = "TToExternalActivity";

    // region UI
    private TextView textViewFromAccountName, textViewFromAccountBalance, textViewToAccountName;
    private EditText editTextRegistrationNumber, editTextAccountNumber, editTextAmount;
    private Button buttonConfirmRecipient, buttonTransfer;
    private ScrollView scrollView;
    private LinearLayout linearLayout;
    // endregion

    Map<String, Account> accounts = new HashMap<>();
    Account fromAccount;
    Boolean isAccountListBuilt = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_to_external);
        Log.d(TAG, "onCreate: called");

        init();
    }

    @Override
    public void onClick(View v) {
        String tag = v.getTag().toString().toLowerCase();
        Log.d(TAG, "onClick: element with tag " + tag + " pressed");

        switch (tag) {
            case "confirm recipient":
                String regAndAccountNumber = editTextRegistrationNumber.getText()
                        + " " + editTextAccountNumber.getText();
                textViewToAccountName.setText(regAndAccountNumber);
                textViewToAccountName.setVisibility(View.VISIBLE);
                editTextRegistrationNumber.setVisibility(View.INVISIBLE);
                editTextAccountNumber.setVisibility(View.INVISIBLE);
                buttonConfirmRecipient.setVisibility(View.INVISIBLE);
                editTextAmount.setVisibility(View.VISIBLE);
                buttonTransfer.setVisibility(View.VISIBLE);

                break;

            case "from account":
                if (!isAccountListBuilt) {
                    buildAccountList();
                }

                scrollView.setVisibility(View.VISIBLE);

                break;

            case "to account":
                textViewToAccountName.setVisibility(View.INVISIBLE);
                editTextRegistrationNumber.setVisibility(View.VISIBLE);
                editTextAccountNumber.setVisibility(View.VISIBLE);
                buttonConfirmRecipient.setVisibility(View.VISIBLE);
                editTextAmount.setVisibility(View.INVISIBLE);
                buttonTransfer.setVisibility(View.INVISIBLE);

                break;

            case "transfer money":
                Intent intent = new Intent(this, NemIDActivity.class);
                intent.putExtra("fromAccount", fromAccount);
                intent.putExtra("toAccount", textViewToAccountName.getText().toString());
                intent.putExtra
                        ("amount", Double.valueOf(editTextAmount.getText().toString()));
                startActivity(intent);
                finish();

                break;

            default:
                scrollView.setVisibility(View.INVISIBLE);

                try {
                    fromAccount = accounts.get(tag);
                    textViewFromAccountName.setText(fromAccount.getType().toString());
                    String tVFABText = "Balance: " + fromAccount.getBalance();
                    textViewFromAccountBalance.setText(tVFABText);
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

                        try {
                            String regAndAccNumber = account.getRegistrationNumber() + ":" + account.getAccountNumber();
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

    private void buildAccountList() {
        for (HashMap.Entry<String, Account> entry : accounts.entrySet()) {
            Button button = new Button(TransferToExternalActivity.this);
            button.setLayoutParams(new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
            button.setPadding(16, 16, 16, 16);
            button.setBackgroundTintList(ColorStateList
                    .valueOf(getResources().getColor(R.color.colorWhite)));
            button.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            button.setAllCaps(false);
            button.setTextAppearance(TransferToExternalActivity.this,
                    android.R.style.TextAppearance_Medium);
            button.setOnClickListener(TransferToExternalActivity.this);

            try {
                String bText =
                        entry.getValue().getType() + " - balance: " + entry.getValue().getBalance();
                button.setText(bText);
                String regAndAccNumber = entry.getValue().getRegistrationNumber() +
                        ":" + entry.getValue().getAccountNumber();
                button.setTag(regAndAccNumber);
                linearLayout.addView(button);
            } catch (NullPointerException eNP) {
                eNP.printStackTrace();
                Log.d(TAG, "onSuccess: couldn't fetch account type");
            }
        }

        isAccountListBuilt = true;
    }

    private void init() {
        Log.d(TAG, "initializing...");

        textViewFromAccountName = findViewById(R.id.textViewFromAccountName);
        textViewFromAccountName.setOnClickListener(this);
        textViewFromAccountBalance = findViewById(R.id.textViewFromAccountBalance);
        textViewToAccountName = findViewById(R.id.textViewToAccountName);
        textViewToAccountName.setOnClickListener(this);
        editTextRegistrationNumber = findViewById(R.id.editTextRegistrationNumber);
        editTextAccountNumber = findViewById(R.id.editTextAccountNumber);
        editTextAmount = findViewById(R.id.editTextAmountToExternal);
        buttonConfirmRecipient = findViewById(R.id.buttonConfirmRecipient);
        buttonConfirmRecipient.setOnClickListener(this);
        buttonTransfer = findViewById(R.id.buttonTransferMoneyToExternal);
        buttonTransfer.setOnClickListener(this);
        scrollView = findViewById(R.id.scrollViewTransferToExternal);
        linearLayout = findViewById(R.id.linearLayoutTransferToExternal);

        fetchAccounts(getIntent().getStringArrayExtra("accounts"));
    }
}
