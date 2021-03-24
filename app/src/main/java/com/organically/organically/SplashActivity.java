package com.organically.organically;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {
    Thread timer;
    LottieAnimationView farmerLottieAnimation;
    String aadharNumber;
    String aadharId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        farmerLottieAnimation=findViewById(R.id.lottie_farmer_animation);
        farmerLottieAnimation.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                SharedPreferences sharedPrefs = getSharedPreferences("sp_name", MODE_PRIVATE);
                SharedPreferences.Editor ed;
                if(!sharedPrefs.getBoolean("initialized", false)){
                    ed=sharedPrefs.edit();
                    ed.putBoolean("initialized", false);
                    ed.apply();
                    SharedPreferences pref=getSharedPreferences("CustomerPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=pref.edit();
                    editor.putString("aadharCardNumber",null);
                    editor.apply();
                    SharedPreferences pref1=getSharedPreferences("FarmerPref",Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit=pref1.edit();
                    edit.putString("aadharCardNumber",null);
                    edit.apply();
                    Intent i=new Intent(SplashActivity.this,RegisterActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    SharedPreferences pref=getSharedPreferences("CustomerPref", Context.MODE_PRIVATE);
                    aadharNumber=pref.getString("aadharCardNumber",null);
                    SharedPreferences pref1=getSharedPreferences("FarmerPref",Context.MODE_PRIVATE);
                    aadharId=pref1.getString("aadharCardNumber",null );
                    if(aadharNumber!=null){
                        Intent i=new Intent(SplashActivity.this,CustomerMainActivity.class);
                        startActivity(i);
                        finish();
                    }else if(aadharId!=null){
                        Intent i=new Intent(SplashActivity.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }

                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }
}