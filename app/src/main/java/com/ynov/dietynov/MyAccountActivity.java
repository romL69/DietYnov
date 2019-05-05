package com.ynov.dietynov;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class MyAccountActivity extends AppCompatActivity {

    SharedPreferences prefs;
    private EditText edit_weight;
    private EditText edit_date;
    private EditText edit_size;
    private RadioButton radiobutton;
    public String gender;
    Button updateBut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account_activity);

        updateBut = findViewById(R.id.updatebutton);
        updateBut.setOnClickListener(updateListener);

    }
    private void userInfo() {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("user_weight",edit_weight.getText().toString());
        edit.putString("user_birthdate",edit_date.getText().toString());
        edit.putString("user_size",edit_size.getText().toString());
        edit.putString("user_gender",gender);

        edit.apply();
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_man:
                if (checked)
                    gender = "man";
                    break;
            case R.id.radio_women:
                if (checked)
                    gender = "female";
                    break;
        }
    }

    private View.OnClickListener updateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            userInfo();
        }
    };
}
