package com.example.khizar_pc.assignment2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RelativeActivity extends AppCompatActivity {

    Button register;
    CheckBox chkbox;
    EditText email;
    EditText pass;
    RadioGroup gender;
    RadioButton selectedGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relative);

        register = (Button) findViewById(R.id.register);
        chkbox = (CheckBox) findViewById(R.id.toc);
        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.password);
        gender = (RadioGroup) findViewById(R.id.gender);
        selectedGender = (RadioButton) findViewById(R.id.male);
       // selectedGender.set

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chkbox.isChecked() == false)
                {
                    Toast.makeText(RelativeActivity.this, "Please accept Terms and Conditions",
                            Toast.LENGTH_LONG).show();
                }
                else
                {
                    int selectedId = gender.getCheckedRadioButtonId();
                    selectedGender = (RadioButton) findViewById(selectedId);
                    Toast.makeText(RelativeActivity.this, email.getText() + "\n" + pass.getText() + "\n" + selectedGender.getText() + "\n" + chkbox.isChecked(),
                            Toast.LENGTH_LONG).show();

                }
            }
        });
    }

}
