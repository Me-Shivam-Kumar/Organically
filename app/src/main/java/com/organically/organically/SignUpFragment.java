package com.organically.organically;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button signUpBtn;
    FirebaseFirestore dB;
    String userType;
    TextInputEditText nameSignUp,aadharNumberSignUp,passwordSignUp,confirmPasswordSignUp;
    CircleImageView profileImageView;
    Uri pickedImgUri;
    static int PReqCode=1;
    static int REQUESCODE=1;
    TextView  alreadyhaveanaccount;
    boolean picked,choosed;
    LoadingDialog loadingDialog;


    public SignUpFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_sign_up, container, false);

        dB=FirebaseFirestore.getInstance();
        radioGroup=view.findViewById(R.id.radioGroup);
        signUpBtn=view.findViewById(R.id.sign_up_btn);
        nameSignUp=view.findViewById(R.id.name_signUp);
        alreadyhaveanaccount=view.findViewById(R.id.already_have_an_account);
        aadharNumberSignUp=view.findViewById(R.id.aadhar_number_signUp);
        passwordSignUp=view.findViewById(R.id.password_signUp);
        confirmPasswordSignUp=view.findViewById(R.id.confirm_password_signUp);
        profileImageView=view.findViewById(R.id.profile_image_view);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=22){
                    checkAndRequestForPermission();
                }else{
                    openGallery();
                }
            }
        });

        loadingDialog=new LoadingDialog(getActivity());
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton=view.findViewById(checkedId);
                signUpBtn.setText("Sign Up as "+radioButton.getText().toString());
                userType=radioButton.getText().toString().trim();
                choosed=true;

            }
        });
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
                Toast.makeText(getContext(),"Please Accept for requiredper mission",Toast.LENGTH_SHORT).show();
            }
            else {
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }
        }else {
            openGallery();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==REQUESCODE && data != null) {
            pickedImgUri = data.getData();
            profileImageView.setImageURI(pickedImgUri);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         alreadyhaveanaccount.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      getFragmentManager().beginTransaction().replace(R.id.register_frame_layout, new SignInFragment()).commit();
                                                  }
                                              });
        nameSignUp.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        aadharNumberSignUp.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        passwordSignUp.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmPasswordSignUp.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });

    }
    private void checkInputs(){
        if(!TextUtils.isEmpty(nameSignUp.getText())){
            if(!TextUtils.isEmpty(aadharNumberSignUp.getText())){
                if(!TextUtils.isEmpty(passwordSignUp.getText())){
                    if(!TextUtils.isEmpty(confirmPasswordSignUp.getText())){
                        signUpBtn.setEnabled(true);
                        signUpBtn.setTextColor(getResources().getColor(R.color.white));

                    } else {
                        signUpBtn.setEnabled(false);
                        signUpBtn.setTextColor(Color.argb(50,255,255,255));
                    }
                }else {
                    signUpBtn.setEnabled(false);
                    signUpBtn.setTextColor(Color.argb(50,255,255,255));
                }
            } else{
                signUpBtn.setEnabled(false);
                signUpBtn.setTextColor(Color.argb(50,255,255,255));
            }
        } else {
            signUpBtn.setEnabled(false);
            signUpBtn.setTextColor(Color.argb(50,255,255,255));
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
    private void registerNewUser() {
        Map<String,Object> newUserData=new HashMap<>();
        String name=nameSignUp.getText().toString();
        String aadharNumber=aadharNumberSignUp.getText().toString();
        String password=passwordSignUp.getText().toString();
        newUserData.put("name",name);
        newUserData.put("aadharCardNumber",aadharNumber);
        newUserData.put("password",password);
        String ProfileImageName=aadharNumber+"."+getFileExtension(pickedImgUri);
        signUpBtn.setEnabled(false);

        if(choosed){
            if(passwordSignUp.getText().toString().equals(confirmPasswordSignUp.getText().toString() )){
                if( passwordSignUp.length()>=6){
                    if(aadharNumberSignUp.length()==10){
                        if(userType.equals("Consumer")){

                            StorageReference mStorage= FirebaseStorage.getInstance().getReference("customersImage");
                            StorageReference fileReference=mStorage.child(ProfileImageName);
                            if(picked){
                                fileReference.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                newUserData.put("profilePic",String.valueOf(uri));
                                                Toast.makeText(getContext(),"Photo Uploaded Succesfully",Toast.LENGTH_LONG).show();

                                                dB.collection("Customers").document(aadharNumber).collection("CustomerProfile").add(newUserData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                                        Toast.makeText(getContext(),"Profile Created Successfully",Toast.LENGTH_LONG).show();
                                                        getFragmentManager().beginTransaction().replace(R.id.register_frame_layout,new SignInFragment()).commit();
                                                    }
                                                });
                                            }
                                        });

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(),"Photo Uploaded UNSuccesfully",Toast.LENGTH_LONG).show();

                                    }
                                });
                            }
                            else{
                                Toast.makeText(getContext(),"Please pick an profile image",Toast.LENGTH_SHORT).show();
                                signUpBtn.setEnabled(true);
                            }

                        }
                        else {
                            StorageReference cStorage=FirebaseStorage.getInstance().getReference("farmerImage");
                            StorageReference fileReference=cStorage.child(ProfileImageName);
                            if(picked){
                                fileReference.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                newUserData.put("profilePic",String.valueOf(uri));
                                                Toast.makeText(getContext(),"Photo Uploaded Succesfully",Toast.LENGTH_LONG).show();
                                                dB.collection("Farmers_Producers").document(aadharNumber).collection("FarmerProducerProfile").document("profile").set(newUserData,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            getFragmentManager().beginTransaction().replace(R.id.register_frame_layout,new SignInFragment()).commit();
                                                            Map<String,Object> ordersGotMap=new HashMap<>();
                                                            dB.collection("Farmers_Producers").document(aadharNumber).collection("OrdersGot").add(ordersGotMap);
                                                            Toast.makeText(getContext(),"Profile Created Successfully",Toast.LENGTH_LONG).show();
                                                            Intent intent=new Intent(getContext(),RegisterActivity.class);
                                                            startActivity(intent);
                                                            getActivity().finish();
                                                        }

                                                    }
                                                });
                                            }
                                        });


                                    }
                                });
                            }
                            else{
                                Toast.makeText(getContext(),"Please pick an profile image",Toast.LENGTH_SHORT).show();
                                signUpBtn.setEnabled(true);
                            }

                        }
                    }else{
                        signUpBtn.setEnabled(true);
                        Toast.makeText(getContext(),"Wrong Aadhar Card Number ",Toast.LENGTH_SHORT).show();
                    }

                }else {
                    signUpBtn.setEnabled(true);
                    Toast.makeText(getContext(),"Password should be at least 6 character long ",Toast.LENGTH_SHORT).show();
                }


            }
            else {
                signUpBtn.setEnabled(true);
                Toast.makeText(getContext(),"Password value does not match ",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getContext(),"Please Choose Category",Toast.LENGTH_SHORT).show();
            signUpBtn.setEnabled(true);
        }

    }
}