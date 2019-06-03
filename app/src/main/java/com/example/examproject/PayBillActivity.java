package com.example.examproject;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.Constraints;
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

public class PayBillActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "PayBillActivity";

    // region UI
    private ConstraintLayout parentLayout;
    private TextView textViewFromAccountName, textViewFromAccountBalance;
    private EditText editTextPaymentID, editTextCreditorNumber, editTextAmount;
    private Button buttonNext;
    private ScrollView scrollView;
    private LinearLayout linearLayout;
    // endregion

    Map<String, Account> accounts = new HashMap<>();
    Account fromAccount;
    Boolean isAccountListBuilt = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_bill);
        Log.d(TAG, "onCreate: called");
        init();
    }

    @Override
    public void onClick(View v) {
        String tag = v.getTag().toString().toLowerCase();
        Log.d(TAG, "onClick: element with tag " + tag + " pressed");

        switch (tag) {
            case "from account":
                if (!isAccountListBuilt) {
                    buildAccountList();
                }

                buttonNext.setVisibility(View.INVISIBLE);
                scrollView.setVisibility(View.VISIBLE);

                break;

            case "next":
                ConstraintSet constraints = new ConstraintSet();
                constraints.clone(parentLayout);
                constraints.connect(R.id.buttonNext, ConstraintSet.TOP,
                        R.id.editTextAmount, ConstraintSet.BOTTOM);
                constraints.applyTo(parentLayout);
                editTextAmount.setVisibility(View.VISIBLE);
                String payBill = getResources().getString(R.string.pay_bill);
                buttonNext.setTag(payBill);
                buttonNext.setText(payBill);

                break;

            case "pay bill":
                Intent intent = new Intent(this, NemIDActivity.class);
                intent.putExtra("fromAccount", fromAccount);
                String recipient = "+75< " + editTextPaymentID.getText()
                        + " + " + editTextCreditorNumber.getText() + " <";
                intent.putExtra("toAccount", recipient);
                intent.putExtra
                        ("amount", Double.valueOf(editTextAmount.getText().toString()));
                startActivity(intent);
                finish();

                break;

            default:
                scrollView.setVisibility(View.INVISIBLE);
                buttonNext.setVisibility(View.VISIBLE);

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
            collectionReference.document(name).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                Log.d(TAG, "onSuccess: account fetched");

                                Account account = documentSnapshot.toObject(Account.class);

                                try {
                                    String regAndAccNumber = account.getRegistrationNumber()
                                            + ":" + account.getAccountNumber();
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
                    Log.d(TAG, "onFailure: something went wrong, ", e);
                }
            });
        }
    }

    private void buildAccountList() {
        for (HashMap.Entry<String, Account> entry : accounts.entrySet()) {
            Button button = new Button(PayBillActivity.this);
            button.setLayoutParams(new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
            button.setPadding(16, 16, 16, 16);
            button.setBackgroundTintList(ColorStateList
                    .valueOf(getResources().getColor(R.color.colorWhite)));
            button.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            button.setAllCaps(false);
            button.setTextAppearance(PayBillActivity.this,
                    android.R.style.TextAppearance_Medium);
            button.setOnClickListener(PayBillActivity.this);

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

        parentLayout = findViewById(R.id.parentLayout);
        textViewFromAccountName = findViewById(R.id.textViewFromAccountName);
        textViewFromAccountName.setOnClickListener(this);
        textViewFromAccountBalance = findViewById(R.id.textViewFromAccountBalance);
        editTextPaymentID = findViewById(R.id.editTextPaymentID);
        editTextCreditorNumber = findViewById(R.id.editTextCreditorNumber);
        editTextAmount = findViewById(R.id.editTextAmount);
        buttonNext = findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(this);
        scrollView = findViewById(R.id.scrollView);
        linearLayout = findViewById(R.id.linearLayout);

        fetchAccounts(getIntent().getStringArrayExtra("accounts"));
    }
}
