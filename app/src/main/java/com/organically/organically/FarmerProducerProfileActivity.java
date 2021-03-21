package com.organically.organically;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class FarmerProducerProfileActivity extends AppCompatActivity {
    TextView aadharCardNo,userName,fullName,pincode,address;
    FirebaseFirestore dB;
    StorageReference storageReference;
    CircleImageView profileImage;
    Button addAddressBtn,editAddressBtn,farmerSignOutBtn;
    String addresAdded;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_producer_profile);

        String name=getIntent().getStringExtra("name");
        String profilePic=getIntent().getStringExtra("profilePic");
        String aadharNum=getIntent().getStringExtra("aadharNum");

        aadharCardNo=findViewById(R.id.aadhar_cardNo);
        addAddressBtn=findViewById(R.id.add_address_btn);
        userName=findViewById(R.id.user_name);
        profileImage=findViewById(R.id.profile_image);
        fullName=findViewById(R.id.address_full_name);
        pincode=findViewById(R.id.address_pincode);
        address=findViewById(R.id.address);
        editAddressBtn=findViewById(R.id.edit_address_btn);
        farmerSignOutBtn=findViewById(R.id.sign_out_btn_farmer);

        farmerSignOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPrefs = getSharedPreferences("sp_name", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed;
                ed=sharedPrefs.edit();
                ed.putBoolean("initialized",false);
                ed.apply();
                SharedPreferences pref1=getSharedPreferences("FarmerPref",Context.MODE_PRIVATE);
                SharedPreferences.Editor edit=pref1.edit();
                edit.putString("aadharCardNumber",null);
                edit.apply();
                Intent intent=new Intent(FarmerProducerProfileActivity.this,RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        aadharCardNo.setText(aadharNum);
        userName.setText(name);
        Glide.with(FarmerProducerProfileActivity.this).load(profilePic).into(profileImage);

        dB =FirebaseFirestore.getInstance();
        if(aadharNum!=null){
            dB.collection("Farmers_Producers").document(aadharNum).collection("FarmerProducerProfile").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(!task.getResult().isEmpty()){
                        if(task.isSuccessful()){
                            for(DocumentSnapshot snapshot:task.getResult()){
                                addresAdded=snapshot.getString("addressAdded");
                                if(addresAdded!=null){
                                    if(addresAdded.equals("1")){
                                        addAddressBtn.setVisibility(View.GONE);
                                        editAddressBtn.setVisibility((View.VISIBLE));
                                        pincode.setText(snapshot.getString("pincode"));
                                        address.setText(snapshot.getString("address"));

                                    }
                                }

                            }
                        }
                    }

                }
            });


        }
        addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FarmerProducerProfileActivity.this,AddAddressActivity.class);
                intent.putExtra("title","Add an Address");
                intent.putExtra("requestor","Farmers_Producers");
                intent.putExtra("aadharNum",aadharNum);
                startActivity(intent);
            }
        });
        editAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FarmerProducerProfileActivity.this,AddAddressActivity.class);
                intent.putExtra("title","Change Address");
                intent.putExtra("requestor","Farmers_Producers");
                intent.putExtra("aadharNum",aadharNum);
                startActivity(intent);
            }
        });


    }
}