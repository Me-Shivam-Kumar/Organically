package com.organically.organically;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class RegisterActivity extends AppCompatActivity {
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportFragmentManager().beginTransaction().replace(R.id.register_frame_layout,new SignInFragment()).commit();
        
    }
}