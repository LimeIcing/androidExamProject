package com.example.examproject;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.examproject.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "ChangePasswordActivity";

    // region UI
    private EditText editTextOldPassword, editTextNewPassword;
    private Button buttonChangePassword;
    // endregion

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Log.d(TAG, "onCreate: called");
        init();
    }

    @Override
    public void onClick(View v) {
        final DocumentReference documentReference = FirebaseFirestore.getInstance()
                .document("users/" + currentUser.getEmail());

        FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Boolean>() {
            @Override
            public Boolean apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(documentReference);

                if (snapshot.get("password").equals(editTextOldPassword.getText().toString())) {
                    transaction.update(documentReference, "password",
                            editTextNewPassword.getText().toString());
                    return true;
                }

                return false;
            }
        }).addOnSuccessListener(new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                Log.d(TAG, "onSuccess: password changed");

                Toast.makeText(ChangePasswordActivity.this,
                        "Password changed", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onSuccess: old password doesn't match the stored password");

                Toast.makeText(ChangePasswordActivity.this,
                        "Old password doesn't match the stored password",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void init() {
        Log.d(TAG, "initializing...");

        currentUser = getIntent().getParcelableExtra("user");

        editTextOldPassword = findViewById(R.id.textViewOldPassword);
        editTextNewPassword = findViewById(R.id.textViewNewPassword);
        buttonChangePassword = findViewById(R.id.buttonChangePassword);
        buttonChangePassword.setOnClickListener(this);
    }
}
