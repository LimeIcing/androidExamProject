package com.example.examproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.examproject.models.Account;
import com.example.examproject.models.User;

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
