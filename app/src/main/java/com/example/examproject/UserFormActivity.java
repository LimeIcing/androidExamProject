package com.example.examproject;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class UserFormActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "UserFormActivity";

    private EditText editTextFirstName, editTextLastName;
    private TextView textViewDateOfBirth;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private Button buttonSubmit, buttonCancel;

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
                        android.R.style.Theme_DeviceDefault_Dialog_MinWidth, dateSetListener,
                        year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
                dialog.show();

                break;

            case R.id.buttonSubmitApplication:
                Log.d(TAG, "onClick: Submit pressed");

                // Throw shit to Firestore, make a toast and finish()
                break;

            case R.id.buttonCancelUserForm:
                Log.d(TAG, "onClick: Cancel pressed");

                finish();
                break;
        }
    }

    private void init() {
        Log.d(TAG, "initializing...");

        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        textViewDateOfBirth = findViewById(R.id.textViewDateOfBirth);
        textViewDateOfBirth.setOnClickListener(this);
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
