package com.organically.organically;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class RentAVehicleFragment extends Fragment {
    TextInputEditText vehicelType,vehicelCompanyName,vehicelModel,vehicelRent;
    String aadharId;
    FirebaseFirestore dB;
    ImageView vehicleImage;
    Button openForRentBtn;
    static int PReqCode=1;
    static int REQUESCODE=1;
    boolean picked;
    Uri pickedImgUri;
    String imageLink;
    String name;
    LoadingDialog loadingDialog;



    public RentAVehicleFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_rent_a_vehicle, container, false);
        vehicelType=view.findViewById(R.id.vehicle_type);
        vehicelCompanyName=view.findViewById(R.id.vehicle_companyName);
        vehicelModel=view.findViewById(R.id.vehicle_model);
        vehicelRent=view.findViewById(R.id.vehicle_rent);
        vehicleImage=view.findViewById(R.id.vehicle_imageView);
        openForRentBtn=view.findViewById(R.id.openForRent_btn);

        loadingDialog=new LoadingDialog(getActivity());

        vehicleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=22){
                    checkAndRequestForPermission();
                }else{
                    openGallery();
                }
            }
        });

        SharedPreferences pref=getActivity().getSharedPreferences("FarmerPref", Context.MODE_PRIVATE);
        aadharId=pref.getString("aadharCardNumber",null );
        name=pref.getString("farmerName",null);
        dB=FirebaseFirestore.getInstance();
        if(!aadharId.equals(null)){
            openForRentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadingDialog.showDialog();
                    String ProfileImageName=aadharId+" "+vehicelType.getText()+" "+vehicelCompanyName.getText()+" "+vehicelModel.getText()+"."+getFileExtension(pickedImgUri);
                    Map<String,Object> vehicelToRent=new HashMap<>();
                    if(picked){

                        StorageReference cStorage= FirebaseStorage.getInstance().getReference("farmerImage");
                        StorageReference fileReference=cStorage.child(ProfileImageName);
                        fileReference.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                fileReference.getDownloadUrl().addOnSuccessListener(new  OnSuccessListener<Uri>(){
                                    @Override
                                    public void onSuccess(Uri uri) {
                                       imageLink=String.valueOf(uri);
                                        if(!TextUtils.isEmpty(vehicelType.getText())){
                                            vehicelToRent.put("vehicleImage",imageLink);
                                            vehicelToRent.put("vehicleType",vehicelType.getText().toString());
                                            if(!TextUtils.isEmpty(vehicelCompanyName.getText())){
                                                vehicelToRent.put("vehicleCompanyName",vehicelCompanyName.getText().toString());
                                                if(!TextUtils.isEmpty(vehicelModel.getText())){
                                                    vehicelToRent.put("vehicleModel",vehicelModel.getText().toString());
                                                    if(!TextUtils.isEmpty(vehicelRent.getText())){
                                                        vehicelToRent.put("vehicleRent",vehicelRent.getText().toString());
                                                        vehicelToRent.put("farmerId",aadharId);
                                                        vehicelToRent.put("farmerName",name);
                                                        dB.collection("Farmers_Producers").document(aadharId).collection("RentVehicle").document(vehicelType.getText()+" "+vehicelCompanyName.getText()+" "+vehicelModel.getText()).
                                                                set(vehicelToRent, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(getContext(),"Vehicle Opened For Rent",Toast.LENGTH_SHORT).show();
                                                                vehicelType.setText("");
                                                                vehicelCompanyName.setText("");
                                                                vehicelModel.setText("");
                                                                vehicelRent.setText("");
                                                                vehicleImage.setImageResource(R.drawable.ic_baseline_agriculture_24);
                                                                loadingDialog.hideDialog();
                                                            }
                                                        });

                                                    }
                                                    else{
                                                        loadingDialog.hideDialog();
                                                    }
                                                }
                                                else{
                                                    loadingDialog.hideDialog();
                                                    Toast.makeText(view.getContext(),"Vehicle model not entered",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            else {
                                                loadingDialog.hideDialog();
                                                Toast.makeText(view.getContext(),"Vehicle company name not entered",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else{
                                            loadingDialog.hideDialog();
                                            Toast.makeText(view.getContext(),"Vehicle Type Not Entered",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                        });

                    }
                    else{
                        loadingDialog.hideDialog();
                        Toast.makeText(view.getContext(),"Please upload vehicle photo using the above blue icon",Toast.LENGTH_SHORT).show();

                    }

                }
            });

        }

        return view;
    }

    private void openGallery() {
        Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }

    private void checkAndRequestForPermission() {
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(getContext(),"Please Give Permission Of Storage",Toast.LENGTH_SHORT).show();
            }
            else {
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }
        }else {
            openGallery();
        }
    }
    private String getFileExtension(Uri uri){
        if(uri!=null){
            ContentResolver contentResolver=getActivity().getContentResolver();
            MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
            picked=true;
            return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

        }
        else {
            return null;
        }
}

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==REQUESCODE && data != null) {
            pickedImgUri = data.getData();
            vehicleImage.setImageURI(pickedImgUri);
        }
    }
}