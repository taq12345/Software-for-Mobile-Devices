package com.example.khizar_pc.assignment2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    Button rela;
    Button constr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rela = (Button)findViewById(R.id.relative);
        constr = (Button)findViewById(R.id.constraint);

        rela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRelative();

            }
        });

        constr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConstraint();
            }
        });
    }
    public void openRelative()
    {
        Intent intent = new Intent(this, RelativeActivity.class);
        startActivity(intent);
    }

    public void openConstraint()
    {
        Intent intent = new Intent(this, ConstraintActivity.class);
        startActivity(intent);
    }
}
