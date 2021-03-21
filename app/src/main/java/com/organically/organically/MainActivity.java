package com.organically.organically;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    FrameLayout orders_frameLayout;
    TextView addNewOfferingBtn,totalQuantityUnit,pricePerUnitUnit;
    Spinner unitSpinner;
    Button viewAllOfferingBtn;
    TextView startDateTV,endDateTV,farmerProducerName,farmerProducerAadharNo,offeringNameMain,pricePerUnitMain,durationMain,totalQtyMain;
    LinearLayout findOrders,myBids;
    FirebaseFirestore dB;
    TextInputEditText productName,totalProductQty,pricePerUnit;
    ImageView settingsIcon;
    String aadharId;
    CircleImageView farmerProducerProfilePic;
    StorageReference firebaseStorage;
    LoadingDialog loadingDialog;
    TextView itemNameTv,priceTv,durationTv,qtyTv,noOfferingsTv;
    List<String> offeringNames=new ArrayList();
    String names="";
    String imageName;
    String name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationListner);

        getSupportFragmentManager().beginTransaction().replace(R.id.farmer_fragmentContainer,new FarmerHomeFragment()).commit();


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationListner=new BottomNavigationView.OnNavigationItemSelectedListener(){

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment=null;
            switch(item.getItemId()){
                case R.id.dashboard_Farmer:
                    selectedFragment= new FarmerHomeFragment();
                    break;
                case R.id.rent_equipment:
                    selectedFragment= new RentVehicleFragment();
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.farmer_fragmentContainer,selectedFragment).commit();
            return true;
        }
    };
}