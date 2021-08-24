package com.example.womensafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText mEmail,mPassword;
    ProgressBar mProgressBar;
    FirebaseAuth firebaseAuth;
    private long backPressedTime;


    @Override
    public void onBackPressed() {

        if(backPressedTime + 3000 > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else{
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);



        mEmail = findViewById(R.id.login_id);
        mPassword = findViewById(R.id.password_id);
        mProgressBar = findViewById(R.id.progressBar_reg);
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        Button mRegisterButton = findViewById(R.id.register_button);
        Button mLoginButton = findViewById(R.id.login_button);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Please enter email id");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("You cant leave password empty");
                    return;
                }

                mProgressBar.setVisibility(view.VISIBLE);

                //check database

                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "login sucessfull", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }else{
                            Toast.makeText(Login.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            mProgressBar.setVisibility(view.GONE);
                        }

                    }
                });

            }
        });



    }
}