package com.example.womensafety;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity {


    ImageView about_us_btn,map_btn,tips_btn,danger_btn;
    TextView Name,Mob,LogOut;
    FirebaseAuth firebaseAuth;
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
        setContentView(R.layout.activity_main);


        about_us_btn = findViewById(R.id.about_us);
        map_btn = findViewById(R.id.map_btn);
        tips_btn = findViewById(R.id.tips_page);
        danger_btn = findViewById(R.id.danger_page);
        Name = findViewById(R.id.name_id);
        Mob = findViewById(R.id.mob_id);
        LogOut = findViewById(R.id.log_out);

        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //retreve data if already exist
        userID = firebaseAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Name.setText(value.getString("FullName"));
                Mob.setText(value.getString("Phone"));

            }
        });




        about_us_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AboutDevs.class));
            }
        });

        map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), toMap.class));
            }
        });

        tips_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), toTips.class));
            }
        });

        danger_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ToDanger.class));
            }
        });

        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

    }


}