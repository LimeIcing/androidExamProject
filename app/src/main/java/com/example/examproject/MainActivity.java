package com.example.examproject;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "MainActivity";
    private final String EMAIL = "email";

    // region UI
    private Button buttonSignIn, buttonNewUser;
    private EditText editTextEmail, editTextPassword;
    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: called");
        init();

        if (savedInstanceState != null) {
            editTextEmail.setText(savedInstanceState.getString(EMAIL));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSignIn:
                Log.d(TAG, "onClick: sign in pressed");

                if (editTextEmail.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this,
                            "Please fill out the fields", Toast.LENGTH_LONG).show();
                } else {
                    checkUser();
                }

                break;

            case R.id.buttonNewUser:
                Log.d(TAG, "onClick: new user pressed");
                Intent intent = new Intent(this, UserFormActivity.class);
                startActivity(intent);

                // fill in username in password

                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: called");

        outState.putString(EMAIL, editTextEmail.getText().toString());
    }

    private void checkUser() {
        Log.d(TAG, "checkUser: called");

        DocumentReference documentReference = FirebaseFirestore.getInstance()
                .document("users/" + editTextEmail.getText().toString());

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (editTextEmail.getText().toString()
                            .equals(documentSnapshot.getString("email"))
                            && editTextPassword.getText().toString()
                            .equals(documentSnapshot.getString("password"))) {
                        Log.d(TAG, "onSuccess: username and password valid");

                        User currentUser = documentSnapshot.toObject(User.class);

                        editTextEmail.getText().clear();
                        editTextPassword.getText().clear();

                        Intent intent = new Intent
                                (MainActivity.this, OverviewActivity.class);
                        intent.putExtra("user", currentUser);
                        startActivity(intent);
                    } else {
                        Log.d(TAG, "onSuccess: password invalid");

                        Toast.makeText(MainActivity.this,
                                "E-mail or password incorrect", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d(TAG, "onSuccess: e-mail invalid");

                    Toast.makeText(MainActivity.this,
                            "E-mail or password incorrect", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: something went wrong, ", e);
            }
        });
    }

    private void init(){
        Log.d(TAG, "Initializing...");

        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonSignIn.setOnClickListener(this);
        buttonNewUser = findViewById(R.id.buttonNewUser);
        buttonNewUser.setOnClickListener(this);
        editTextEmail = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);

        // region DELETE THIS WHEN DONE TESTING
        String tempUN = "f@l.me";
        String tempPW = "pwfl1";
        editTextEmail.setText(tempUN);
        editTextPassword.setText(tempPW);
        // endregion
    }
}
