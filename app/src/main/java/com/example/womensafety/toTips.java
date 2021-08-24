package com.example.womensafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class toTips extends AppCompatActivity {

    TextView checkSurroundings,eyeContact,keepEmergencyThings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_tips);

        checkSurroundings = findViewById(R.id.checkSurroundins);
        eyeContact = findViewById(R.id.makeEyeContact);
        keepEmergencyThings = findViewById(R.id.keepEmergencyThings);

        checkSurroundings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),CheckSurroundings.class));
            }
        });

        eyeContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ToEyeContact.class));
            }
        });

        keepEmergencyThings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ToKeepThings.class));
            }
        });




    }
}