package com.example.womensafety;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.audiofx.Virtualizer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ToDanger extends AppCompatActivity {



    public static final String TAG = "TAG";
    TextView Number1,Number2,Number3;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;
    String userID,EmgN1,EmgN2;
    Button help_btn;
    String strAdd ="",toCall;
    double lat,longi;


    private static final int REQUEST_LOCATION = 1;
    private LocationManager locationManager;

    FusedLocationProviderClient fusedLocationProviderClient;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_danger);

        Number1 = findViewById(R.id.emg_n1);
        Number2 = findViewById(R.id.emg_n2);
        Number3 = findViewById(R.id.emg_n3);
        help_btn = findViewById(R.id.help_button);

        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //retreve data if already exist
        userID = firebaseAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable  DocumentSnapshot value, @Nullable  FirebaseFirestoreException error) {
              Number1.setText(value.getString("Number1"));
              Number2.setText(value.getString("Number2"));
              Number3.setText(value.getString("Number3"));
              EmgN1 = value.getString("Number1");
              EmgN2 = value.getString("Number2");
              toCall = "tel:" + EmgN1;

            }
        });

        //initialize fused location
       // fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }else{
            getLocation();
        }

        help_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


              if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                  if(checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                      sendSMS();
                  }else {
                      Toast.makeText(ToDanger.this, "Please enable permission to send SMS", Toast.LENGTH_SHORT).show();
                      requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);
                  }
              }

                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(toCall));
                startActivity(intent);


            }
        });

    }

    private void getLocation() {
        if(ActivityCompat.checkSelfPermission(ToDanger.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(ToDanger.this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(ToDanger.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }else {
            Location locationGPS = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
            if(locationGPS != null){
                 lat = locationGPS.getLatitude();
                 longi = locationGPS.getLongitude();

                TextView Latitude =(TextView) findViewById(R.id.lat);
                TextView Longitude = (TextView) findViewById(R.id.longi);
                TextView address = (TextView)findViewById(R.id.address);

                Latitude.setText(String.valueOf(lat));
                Longitude.setText(String.valueOf(longi));
                String addressLine=getCompleteAddressString(lat,longi);
                address.setText(String.valueOf(addressLine));
            }else {
                Toast.makeText(this, "Unable to find location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {

        Geocoder geocoder =  new Geocoder(this, Locale.getDefault());
        try{
            List<Address> addresses = geocoder.getFromLocation(LATITUDE,LONGITUDE,1);
            if(addresses != null){
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for(int i = 0; i<= returnedAddress.getMaxAddressLineIndex();i++){
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd =strReturnedAddress.toString();
            }else {

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  strAdd;
    }

    private void sendSMS(){
        String SMS = "Im in danger please help me, im at location "+  strAdd +" Latitude: "+ lat+"  Longitude: "+longi+"" ;

        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(EmgN1,null,SMS,null,null);
            Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();
            smsManager.sendTextMessage(EmgN2,null,SMS,null,null);
            Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Failed to send msg", Toast.LENGTH_SHORT).show();
        }

    }
}