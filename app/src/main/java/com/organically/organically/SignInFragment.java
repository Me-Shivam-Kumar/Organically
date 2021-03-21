package com.organically.organically;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A simple {@link Fragment} subclass.

 */
public class SignInFragment extends Fragment {
    private TextView dontHaveAnAccount;
    private TextInputEditText aadharNumber,password;
    Button logInBtn;
    FirebaseFirestore dB,idConfirmDB;
    RadioGroup radioGroup;
    RadioButton radioButton;
    String userType;
    LoadingDialog loadingDialog;
    boolean choosed=false;
    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_sign_in, container, false);
        dontHaveAnAccount=view.findViewById(R.id.do_not_have_an_account);
        aadharNumber=view.findViewById(R.id.aadhar_number_signIn);
        password=view.findViewById(R.id.password_signIn);
        logInBtn=view.findViewById(R.id.logInBtn);
        radioGroup=view.findViewById(R.id.radioGroup);
        dB=FirebaseFirestore.getInstance();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton=view.findViewById(checkedId);
                logInBtn.setText("Log In as "+radioButton.getText().toString());
                userType=radioButton.getText().toString().trim();
                choosed=true;
            }
        });

        loadingDialog=new LoadingDialog(getActivity());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dontHaveAnAccount.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.register_frame_layout,new SignUpFragment()).commit();
            }
        });
        aadharNumber.addTextChangedListener(new TextWatcher() {
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
        password.addTextChangedListener(new TextWatcher(){

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
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmLogin();
            }
        });


    }

    private void confirmLogin() {

        String aadharCardNumber=aadharNumber.getText().toString().trim();
        String passwordNo=password.getText().toString().trim();

        if(choosed){
            loadingDialog.showDialog();
            if(userType.equals("Consumer")){
                dB.collection("Customers").document(aadharCardNumber).collection("CustomerProfile").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(!task.getResult().isEmpty()){
                            if(task.isSuccessful()){
                                for(DocumentSnapshot snapshot:task.getResult()){
                                    String passWordStored=snapshot.getString("password");
                                    if(passWordStored.equals(passwordNo)){
                                        loadingDialog.hideDialog();
                                        SharedPreferences pref=getActivity().getSharedPreferences("CustomerPref",Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor=pref.edit();
                                        editor.putString("aadharCardNumber",aadharCardNumber);
                                        editor.apply();
                                        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("sp_name", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor ed;
                                        ed=sharedPrefs.edit();
                                        ed.putBoolean("initialized", true);
                                        ed.apply();
                                        SharedPreferences pref1=getActivity().getSharedPreferences("FarmerPref", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor1=pref1.edit();
                                        editor1.putString("aadharCardNumber",null);
                                        editor1.apply();
                                        Intent intent=new Intent(getContext(),CustomerMainActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }else {
                                        loadingDialog.hideDialog();
                                        Toast.makeText(getContext(),"Wrong Password Entered",Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }

                        }else{
                            loadingDialog.hideDialog();
                            Toast.makeText(getContext(),"User Not Registered",Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }
            else{

                dB.collection("Farmers_Producers").document(aadharCardNumber).collection("FarmerProducerProfile").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(!task.getResult().isEmpty()){
                            if(task.isSuccessful()){
                                for(DocumentSnapshot snapshot:task.getResult()){
                                    String passWordStored=snapshot.getString("password");
                                    if(passWordStored.equals(passwordNo)){
                                        loadingDialog.hideDialog();
                                        SharedPreferences pref=getActivity().getSharedPreferences("FarmerPref",Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor=pref.edit();
                                        editor.putString("aadharCardNumber",aadharCardNumber);
                                        editor.apply();
                                        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("sp_name", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor ed;
                                        ed=sharedPrefs.edit();
                                        ed.putBoolean("initialized", true);
                                        ed.apply();
                                        SharedPreferences pref1=getActivity().getSharedPreferences("CustomerPref", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor1=pref1.edit();
                                        editor1.putString("aadharCardNumber",null);
                                        editor1.apply();
                                        Intent intent=new Intent(getContext(),MainActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }else {
                                        loadingDialog.hideDialog();
                                        Toast.makeText(getContext(),"Wrong Password Entered",Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                        }

                        else{
                            loadingDialog.hideDialog();
                            Toast.makeText(getContext(),"User Not Registered",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }

        }else{
            Toast.makeText(getContext(),"Please Choose Category",Toast.LENGTH_SHORT).show();
        }

    }

    private void checkInputs(){
        if(!TextUtils.isEmpty(aadharNumber.getText())){
            if(!TextUtils.isEmpty(password.getText())){
                logInBtn.setEnabled(true);
                logInBtn.setTextColor(getResources().getColor(R.color.white));

            }else{
                logInBtn.setEnabled(false);
                logInBtn.setTextColor(Color.argb(50,255,255,255));
            }
        } else {
            logInBtn.setEnabled(false);
            logInBtn.setTextColor(Color.argb(50,255,255,255));
        }

        }
    }
