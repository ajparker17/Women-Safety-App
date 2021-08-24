package com.example.womensafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText mFullName,mEmail,mPhone,mPassword,Number1,Number2,Number3;
    TextView mLoginButton;
    FirebaseAuth firebaseAuth;
    ProgressBar mProgressBar;
    FirebaseFirestore fStore;
    String userID;
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
        setContentView(R.layout.activity_register);

        mFullName = findViewById(R.id.fullname);
        mEmail = findViewById(R.id.email);
        mPhone = findViewById(R.id.phone);
        mPassword = findViewById(R.id.password);
        mProgressBar = findViewById(R.id.progressBar_reg);
        mLoginButton = findViewById(R.id.login_again);
        Number1 = findViewById(R.id.emg_u_n1);
        Number2 = findViewById(R.id.emg_u_2);
        Number3 = findViewById(R.id.emg_u_n3);

        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();



        Button mRegisterButton = findViewById(R.id.registerButton);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = mFullName.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String phone = mPhone.getText().toString().trim();
                String EmergencyNumberOne = Number1.getText().toString().trim();
                String EmergencyNumberTwo = Number2.getText().toString().trim();
                String EmergencyNumberThree = Number3.getText().toString().trim();

                if(TextUtils.isEmpty(name)){
                    mFullName.setError("Please enter your name");
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Please enter email id");
                    return;
                }

                if(TextUtils.isEmpty(phone)){
                    mPhone.setError("Enter your phone number");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("You cant leave password empty");
                    return;
                }

                if(password.length() < 8){
                    mPassword.setError("Password must be at least 8 characters");
                    return;
                }


                if(TextUtils.isEmpty(EmergencyNumberOne)){
                    Number1.setError("Please enter Emergency Number");
                    return;
                }
                if(TextUtils.isEmpty(EmergencyNumberTwo)){
                    Number2.setError("Please enter Emergency Number");
                    return;
                }
                if(TextUtils.isEmpty(EmergencyNumberThree)){
                    Number3.setError("Please enter Emergency Number");
                    return;
                }


                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(Register.this, "user created", Toast.LENGTH_SHORT).show();
                            userID = firebaseAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("FullName",name);
                            user.put("EmailID",email);
                            user.put("Password",password);
                            user.put("Phone",phone);
                            user.put("Number1",EmergencyNumberOne);
                            user.put("Number2",EmergencyNumberTwo);
                            user.put("Number3",EmergencyNumberThree);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG,"onSucess: user profile created for" + userID);
                                }
                            });

                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }else{
                            Toast.makeText(Register.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

    }
}