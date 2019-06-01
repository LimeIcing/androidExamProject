package com.example.examproject;

import android.app.DatePickerDialog;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examproject.models.AccountType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserFormActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "UserFormActivity";

    // region UI
    private EditText editTextFirstName, editTextLastName, editTextEmail, editTextPassword;
    private TextView textViewDateOfBirth;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private Button buttonSubmit, buttonCancel;
    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);
        Log.d(TAG, "onCreate: called");

        init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewDateOfBirth:
                Log.d(TAG, "onClick: Date of Birth pressed");

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(UserFormActivity.this,
                        dateSetListener,
                        year, month, day);

                dialog.getWindow().setBackgroundDrawable
                        (new ColorDrawable(getResources().getColor(R.color.colorWhite)));
                dialog.show();

                break;

            case R.id.buttonSubmitApplication:
                Log.d(TAG, "onClick: Submit pressed");
                Toast.makeText(this, "Communicating with server...", Toast.LENGTH_LONG)
                        .show();

                saveToFirestore();

                break;

            case R.id.buttonCancelUserForm:
                Log.d(TAG, "onClick: Cancel pressed");

                finish();
                break;
        }
    }

    private void saveToFirestore() {
        Log.d(TAG, "saveToFirestore: saving...");

        DocumentReference documentReference;
        List<String> accounts = new LinkedList<>();
        Map<String, Object> data = new HashMap<>();
        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        String dateOfBirth = textViewDateOfBirth.getText().toString();
        final String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        StringBuilder stringBuilder = new StringBuilder();

        if (firstName.isEmpty() || lastName.isEmpty() || dateOfBirth.isEmpty() || email.isEmpty()
                || password.isEmpty()) {
            Toast.makeText(this,
                    "Please fill out all fields", Toast.LENGTH_LONG)
                    .show();

            return;
        }

        for (int i = 0; i < 28; i++) {
            stringBuilder.append(((int)(Math.random() * 10)));
        }

        createAccount(AccountType.DEFAULT, stringBuilder.substring(0, 14));
        createAccount(AccountType.BUDGET, stringBuilder.substring(14, 28));
        accounts.add(email + ":" + stringBuilder.substring(4, 14));
        accounts.add(email + ":" + stringBuilder.substring(18, 28));

        documentReference = FirebaseFirestore.getInstance().document("users/" + email);
        data.put("firstName", firstName);
        data.put("lastName", lastName);
        data.put("dateOfBirth", dateOfBirth);
        data.put("email", email);
        data.put("password", password);
        data.put("accounts", accounts);

        documentReference.set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: document " + email + " saved to Firestore");

                    Toast.makeText(UserFormActivity.this,
                            "User successfully created", Toast.LENGTH_LONG).show();

                    finish();
                } else {
                    Log.d(TAG, "onComplete: something went wrong while trying to save" +
                            " document to Firestore", task.getException());

                    Toast.makeText(UserFormActivity.this,
                            "Something went wrong. Please try again later", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

    }

    public void createAccount(final AccountType accountType, String numberString) {
        final String accountNumber, registrationNumber;
        DocumentReference documentReference;
        Map<String, Object> data = new HashMap<>();
        String email = editTextEmail.getText().toString();

        accountNumber = numberString.substring(4, 14);
        registrationNumber = numberString.substring(0, 4);

        Log.d(TAG, "saveToFirestore: generated registration and account numbers for " +
                accountType + ": " + registrationNumber + " " + accountNumber);

        documentReference = FirebaseFirestore
                .getInstance().document("accounts/" + email + ":" + accountNumber);
        data.clear();
        data.put("balance", 0);
        data.put("type", accountType);
        data.put("accountNumber", accountNumber);
        data.put("registrationNumber", registrationNumber);
        data.put("owner", email);

        documentReference.set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: " + accountType + " account saved to Firestore");
                } else {
                    Log.d(TAG, "onComplete: something went wrong while trying to save" +
                            accountType + " to Firestore", task.getException());

                    Toast.makeText(UserFormActivity.this,
                            "Something went wrong. Please try again later", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    private void init() {
        Log.d(TAG, "initializing...");

        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        textViewDateOfBirth = findViewById(R.id.textViewDateOfBirth);
        textViewDateOfBirth.setOnClickListener(this);
        editTextEmail = findViewById(R.id.editTextEmailForm);
        editTextPassword = findViewById(R.id.editTextPasswordForm);
        buttonSubmit = findViewById(R.id.buttonSubmitApplication);
        buttonSubmit.setOnClickListener(this);
        buttonCancel = findViewById(R.id.buttonCancelUserForm);
        buttonCancel.setOnClickListener(this);

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                String date = dayOfMonth + "/" + month + "/" + year;
                textViewDateOfBirth.setText(date);
            }
        };
    }
}
