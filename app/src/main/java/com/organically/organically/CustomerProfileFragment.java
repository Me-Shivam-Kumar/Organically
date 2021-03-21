package com.organically.organically;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerProfileFragment extends Fragment {
    TextView aadharCardNo,userName,fullName,pincode,address;
    FirebaseFirestore dB;
    StorageReference storageReference;
    CircleImageView profileImage;
    Button addAddressBtn,editAddressBtn,customerSignOutBtn;
    String addresAdded;
    LoadingDialog loadingDialog;

    public CustomerProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_customer_profile, container, false);

        aadharCardNo=view.findViewById(R.id.aadhar_cardNo);
        addAddressBtn=view.findViewById(R.id.add_address_btn);
        userName=view.findViewById(R.id.user_name);
        profileImage=view.findViewById(R.id.profile_image);
        fullName=view.findViewById(R.id.address_full_name);
        pincode=view.findViewById(R.id.address_pincode);
        address=view.findViewById(R.id.address);
        editAddressBtn=view.findViewById(R.id.edit_address_btn);
        customerSignOutBtn=view.findViewById(R.id.sign_out_btn_customer);


        loadingDialog=new LoadingDialog(getActivity());
        dB =FirebaseFirestore.getInstance();
        SharedPreferences pref=getActivity().getSharedPreferences("CustomerPref", Context.MODE_PRIVATE);
        String aadharNumber=pref.getString("aadharCardNumber",null);

        customerSignOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=pref.edit();
                editor.putString("aadharCardNumber",null);
                editor.apply();
                SharedPreferences sharedPrefs = getActivity().getSharedPreferences("sp_name", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed;
                ed=sharedPrefs.edit();
                ed.putBoolean("initialized",false);
                ed.apply();
                Intent intent=new Intent(view.getContext(),RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                getActivity().finish();
            }
        });
        if(aadharNumber!=null){
            loadingDialog.showDialog();
            aadharCardNo.setText(aadharNumber);
            dB.collection("Customers").document(aadharNumber).collection("CustomerProfile").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(DocumentSnapshot snapshot:task.getResult()){
                            String name=snapshot.getString("name");
                            String imageName=snapshot.getString("profilePic");
                            userName.setText(name);
                            fullName.setText(name);
                            Glide.with(view.getContext()).load(imageName).centerCrop().into(profileImage);
                            loadingDialog.hideDialog();
                            dB.collection("Customers").document(aadharNumber).collection("Address").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for(DocumentSnapshot snapshot:task.getResult()){
                                            addresAdded=snapshot.getString("addressAdded");
                                            if(addresAdded.equals("1")){
                                                addAddressBtn.setVisibility(View.GONE);
                                                editAddressBtn.setVisibility((View.VISIBLE));
                                                pincode.setText(snapshot.getString("pincode"));
                                                address.setText(snapshot.getString("address"));

                                            }
                                        }
                                    }
                                }
                            });


                        }
                    }
                }
            });

        }
        addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),AddAddressActivity.class);
                intent.putExtra("title","Add an Address");
                intent.putExtra("aadharNum",aadharNumber);
                startActivity(intent);
            }
        });
        editAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),AddAddressActivity.class);
                intent.putExtra("title","Change Address");
                intent.putExtra("aadharNum",aadharNumber);
                intent.putExtra("requestor","Consumer");
                startActivity(intent);
            }
        });

        return view;
    }

}