package com.example.examproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.examproject.models.User;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "UserProfileActivity";

    // region UI
    private TextView textViewFirstName, textViewLastName, textViewDateOfBirth, textViewEmail;
    private Button buttonChangePassword;
    // endregion

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Log.d(TAG, "onCreate: called");

        init();
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick: change password pressed");

        Intent intent = new Intent(this, ChangePasswordActivity.class);
        intent.putExtra("user", currentUser);
        startActivity(intent);
    }

    private void init() {
        Log.d(TAG, "initializing...");

        currentUser = getIntent().getParcelableExtra("user");
        String textViewText;

        textViewFirstName = findViewById(R.id.textViewFirstName);
        textViewText = "First name: " + currentUser.getFirstName();
        textViewFirstName.setText(textViewText);
        textViewLastName = findViewById(R.id.textViewLastName);
        textViewText = "Last name: " + currentUser.getLastName();
        textViewLastName.setText(textViewText);
        textViewDateOfBirth = findViewById(R.id.textViewDateOfBirth);
        textViewText = "Date of birth: " + currentUser.getDateOfBirth();
        textViewDateOfBirth.setText(textViewText);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewText = "E-mail address: " + currentUser.getEmail();
        textViewEmail.setText(textViewText);
        buttonChangePassword = findViewById(R.id.buttonChangePassword);
        buttonChangePassword.setOnClickListener(this);
    }
}
