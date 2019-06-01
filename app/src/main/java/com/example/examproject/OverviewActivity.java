package com.example.examproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class OverviewActivity extends AppCompatActivity {

    private final String TAG = "OverviewActivity";

    private TextView textViewCurrentUser;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        Log.d(TAG, "onCreate() called");
        init();
    }

    private void init(){
        Log.d(TAG, "Initializing...");

        textViewCurrentUser = findViewById(R.id.textViewCurrentUser);
        currentUser = getIntent().getParcelableExtra("user");
        textViewCurrentUser
                .setText("Hello, " + currentUser.getFirstName() + " " + currentUser.getLastName());
    }
}
