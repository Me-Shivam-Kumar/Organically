package com.organically.organically;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity {
    EditText townOrCity,localityOrStreet,flatorHouseNo,pincode,state,landmark,mobileNumber,alternateMobileNo;
    Button saveBtn;
    FirebaseFirestore db;
    DocumentReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        String title=getIntent().getStringExtra("title");
        Toolbar toolbar=findViewById(R.id.address_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String requestor=getIntent().getStringExtra("requestor");
        String aadharNumber=getIntent().getStringExtra("aadharNum");

        townOrCity=findViewById(R.id.city);
        localityOrStreet=findViewById(R.id.locality);
        flatorHouseNo=findViewById(R.id.flate_no_house_no);
        pincode=findViewById(R.id.pincode);
        landmark=findViewById(R.id.landmark);
        mobileNumber=findViewById(R.id.mobile_number);
        alternateMobileNo=findViewById(R.id.alternate_mobile_number);

        saveBtn=findViewById(R.id.save_btn);

        db=FirebaseFirestore.getInstance();
        if(requestor.equals("Farmers_Producers")){
             ref=db.collection("Farmers_Producers").document(aadharNumber).collection("FarmerProducerProfile").document("profile");
        }else{
             ref=db.collection("Customers").document(aadharNumber).collection("Address").document("Address");
        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> addresses=new HashMap<>();
                if(!TextUtils.isEmpty(mobileNumber.getText())){
                    String mobNum=mobileNumber.getText().toString().trim();
                    addresses.put("mobileNum",mobNum);
                    if(!TextUtils.isEmpty(pincode.getText())){
                        String pin=pincode.getText().toString().trim();
                        addresses.put("pincode",pin);
                        if(!TextUtils.isEmpty(townOrCity.getText())){
                            String town=townOrCity.getText().toString().trim();
                            if(!TextUtils.isEmpty(flatorHouseNo.getText())){
                                String flat=flatorHouseNo.getText().toString().trim();
                                if(!TextUtils.isEmpty(localityOrStreet.getText())){
                                    String locality=localityOrStreet.getText().toString().trim();
                                    String land_mark=landmark.getText().toString().trim();
                                    String full_address=flat+", "+locality+", "+land_mark+", "+town ;
                                    addresses.put("address",full_address);
                                    String alternateMobileNum=alternateMobileNo.getText().toString().trim();
                                    addresses.put("alternateMobileNumber",alternateMobileNum);
                                    addresses.put("addressAdded","1");
                                    ref.set(addresses,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(AddAddressActivity.this,"Address added Succesfully",Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                                }else{
                                    Toast.makeText(AddAddressActivity.this,"Please add Mobile Number",Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(AddAddressActivity.this,"Please add Pincode",Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(AddAddressActivity.this,"Please add Town or City Name",Toast.LENGTH_SHORT).show();

                        }
                    } else{
                        Toast.makeText(AddAddressActivity.this,"Please add Flat,House no.,Building,Company,Apartment Name",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddAddressActivity.this,"Please add Locality or Street Name",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item){
        int id = item.getItemId();
        if(id==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

}