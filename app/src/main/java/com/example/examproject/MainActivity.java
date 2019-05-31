package com.example.examproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "MainActivity";
    private Button buttonSignIn, buttonNewUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: called");
        init();
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.buttonSignIn:
                Log.d(TAG, "onClick: sign in pressed");
                intent = new Intent(this, OverviewActivity.class);
                startActivity(intent);
                break;

            case R.id.buttonNewUser:
                Log.d(TAG, "onClick: new user pressed");
                intent = new Intent(this, UserFormActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void init(){
        Log.d(TAG, "Initializing...");

        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonSignIn.setOnClickListener(this);
        buttonNewUser = findViewById(R.id.buttonNewUser);
        buttonNewUser.setOnClickListener(this);
    }
}
