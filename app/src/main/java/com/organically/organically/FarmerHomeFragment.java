package com.organically.organically;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.

 */
public class FarmerHomeFragment extends Fragment {
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
    ImageView weatherIcon;
    LocationRequest locationRequest;
    public static final int REQUSET_CHECK_SETTING=1001;



    public FarmerHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_farmer_home, container, false);

        orders_frameLayout=view.findViewById(R.id.ordersfragment_frame_layout);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ordersfragment_frame_layout,new OrdersFragment()).commit();

        farmerProducerProfilePic=view.findViewById(R.id.farmer_profiel_pic);
        farmerProducerName=view.findViewById(R.id.farmer_producer_name);
        farmerProducerAadharNo=view.findViewById(R.id.farmer_producer_aadharNumber);

        offeringNameMain=view.findViewById(R.id.offering_name_main_activity);
        pricePerUnitMain=view.findViewById(R.id.price_per_unit_main);
        durationMain=view.findViewById(R.id.duration_main_acti);
        totalQtyMain=view.findViewById(R.id.qty_main_acti);

        itemNameTv=view.findViewById(R.id.offering_name_tv);
        priceTv=view.findViewById(R.id.price_tv);
        durationTv=view.findViewById(R.id.duration_tv);
        qtyTv=view.findViewById(R.id.qty_tv);
        noOfferingsTv=view.findViewById(R.id.no_offerings_tv);

        weatherIcon=view.findViewById(R.id.weather_icon);
        weatherIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationRequest=LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                locationRequest.setInterval(5000);
                locationRequest.setFastestInterval(2000);
                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().
                        addLocationRequest(locationRequest);
                builder.setAlwaysShow(true);

                Task<LocationSettingsResponse> result= LocationServices.getSettingsClient(view.getContext()).
                        checkLocationSettings(builder.build());

                result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                        try {
                            LocationSettingsResponse response=task.getResult(ApiException.class);
                            Intent intent=new Intent(view.getContext(),WeatherActivity.class);
                            startActivity(intent);
                        } catch (ApiException e) {
                            e.printStackTrace();
                            switch(e.getStatusCode()){
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                                    try {
                                        ResolvableApiException resolvableApiException=(ResolvableApiException)e;
                                        resolvableApiException.startResolutionForResult((Activity) view.getContext(),REQUSET_CHECK_SETTING);
                                    } catch (IntentSender.SendIntentException sendIntentException) {

                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    break;

                            }
                        }

                    }
                });


            }
        });

        itemNameTv.setVisibility(View.GONE);
        priceTv.setVisibility(View.GONE);
        durationTv.setVisibility(View.GONE);
        qtyTv.setVisibility(View.GONE);
        offeringNameMain.setVisibility(View.GONE);
        pricePerUnitMain.setVisibility(View.GONE);
        durationMain.setVisibility(View.GONE);
        totalQtyMain.setVisibility(View.GONE);
        noOfferingsTv.setVisibility(View.VISIBLE);


        loadingDialog=new LoadingDialog(getActivity());

        SharedPreferences pref=getActivity().getSharedPreferences("FarmerPref", Context.MODE_PRIVATE);
        dB=FirebaseFirestore.getInstance();
        aadharId=pref.getString("aadharCardNumber",null );
        if(aadharId!=null){
            loadingDialog.showDialog();
            farmerProducerAadharNo.setText(aadharId);
            dB.collection("Farmers_Producers").document(aadharId).collection("FarmerProducerProfile").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        for(DocumentSnapshot snapshot:task.getResult()){
                            imageName=snapshot.getString("profilePic");
                            Glide.with(getActivity()).load(imageName).into(farmerProducerProfilePic);
                            name=snapshot.getString("name");
                            SharedPreferences.Editor editor =  pref.edit();
                            farmerProducerName.setText(name);
                            editor.putString("farmerName",name);

                            dB.collection("Farmers_Producers").document(aadharId).collection("Offerings").orderBy("productName").limitToLast(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        for(DocumentSnapshot snapshot:task.getResult()){
                                            noOfferingsTv.setVisibility(View.GONE);
                                            itemNameTv.setVisibility(View.VISIBLE);
                                            priceTv.setVisibility(View.VISIBLE);
                                            durationTv.setVisibility(View.VISIBLE);
                                            qtyTv.setVisibility(View.VISIBLE);
                                            offeringNameMain.setVisibility(View.VISIBLE);
                                            pricePerUnitMain.setVisibility(View.VISIBLE);
                                            durationMain.setVisibility(View.VISIBLE);
                                            totalQtyMain.setVisibility(View.VISIBLE);
                                            offeringNameMain.setText(snapshot.getString("productName"));
                                            pricePerUnitMain.setText(snapshot.getString("pricePerUnit")+" Per "+snapshot.getString("totalQuantityUnit"));
                                            durationMain.setText(snapshot.getString("startDate")+" to "+snapshot.getString("endDate"));
                                            totalQtyMain.setText(snapshot.getString("totalProuctQty")+" "+snapshot.getString("totalQuantityUnit"));
                                            loadingDialog.hideDialog();
                                        }

                                    }
                                }
                            });
                        }
                    }else{
                        loadingDialog.hideDialog();
                    }
                }
            });



        }else{
            Toast.makeText(getActivity(),"Null",Toast.LENGTH_LONG).show();
        }

        addNewOfferingBtn= (TextView)view.findViewById(R.id.add_new_offering_btn);
        final Dialog addNewOfferingDialog = new Dialog(getActivity());
        addNewOfferingDialog.setContentView(R.layout.add_new_offering);
        addNewOfferingDialog.setCancelable(false);
        addNewOfferingDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addNewOfferingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewOfferingDialog.show();
            }
        });
        addNewOfferingDialog.findViewById(R.id.cancel_textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewOfferingDialog.dismiss();
            }
        });


        unitSpinner=addNewOfferingDialog.findViewById(R.id.unit_spinner_postOrder);
        totalQuantityUnit=addNewOfferingDialog.findViewById(R.id.total_quatity_unit);
        pricePerUnitUnit=addNewOfferingDialog.findViewById(R.id.price_per_unit_unit);
        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                totalQuantityUnit.setText(unitSpinner.getSelectedItem().toString());
                pricePerUnitUnit.setText("Per "+unitSpinner.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        viewAllOfferingBtn=view.findViewById(R.id.view_allofferings_btn);
        viewAllOfferingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),ViewAllOfferings.class);
                startActivity(intent);
            }
        });

        Calendar calendar= Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);

        startDateTV=addNewOfferingDialog.findViewById(R.id.start_date_textView);
        endDateTV=addNewOfferingDialog.findViewById(R.id.end_date_textView);

        startDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        startDateTV.setText(date);

                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        endDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        endDateTV.setText(date);

                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        findOrders=view.findViewById(R.id.find_orders);
        findOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(),FindOrdersActivity.class);
                startActivity(intent);
            }
        });

        myBids=view.findViewById(R.id.my_bids);
        myBids.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),MyBidsFarmersProducersActivity.class);
                startActivity(intent);
            }
        });


        dB=FirebaseFirestore.getInstance();
        productName=addNewOfferingDialog.findViewById(R.id.product_name);
        totalProductQty=addNewOfferingDialog.findViewById(R.id.total_product_qty);
        pricePerUnit=addNewOfferingDialog.findViewById(R.id.price_per_unit);
        addNewOfferingDialog.findViewById(R.id.add_textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String,Object> offering=new HashMap<>();
                if(TextUtils.isEmpty(productName.getText().toString())){
                    Toast.makeText(getActivity(),"Empty Product Name Field not Allowed",Toast.LENGTH_SHORT).show();
                }else{
                    String product_name=productName.getText().toString();
                    offering.put("productName",product_name);
                    if(TextUtils.isEmpty(totalProductQty.getText().toString())){
                        Toast.makeText(getActivity(),"Please Enter Toatl product Quantity",Toast.LENGTH_SHORT).show();
                    }else {
                        String totalProuctQty=totalProductQty.getText().toString();
                        offering.put("totalProuctQty",totalProuctQty);
                        String unit=totalQuantityUnit.getText().toString();
                        offering.put("totalQuantityUnit",unit);
                        if(TextUtils.isEmpty(pricePerUnit.getText().toString())){
                            Toast.makeText(getActivity(),"Please Enter price of  1"+pricePerUnitUnit.getText()+" "+ productName.getText(),Toast.LENGTH_SHORT).show();
                        }else{
                            String pricePer1Unit=pricePerUnit.getText().toString();
                            offering.put("pricePerUnit",pricePer1Unit);
                            String startDate=startDateTV.getText().toString();
                            offering.put("startDate",startDate);
                            String endDate=endDateTV.getText().toString();
                            offering.put("endDate",endDate);
                            dB.collection("Farmers_Producers").document(aadharId).collection("Offerings").document(productName.getText().toString()).set(offering).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        addNewOfferingDialog.dismiss();
                                        CollectionReference offeringsRef=dB.collection("Farmers_Producers").document(aadharId).collection("Offerings");
                                        offeringsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>(){

                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                for(QueryDocumentSnapshot snapshot:queryDocumentSnapshots){
                                                    String id=snapshot.getId();
                                                    offeringNames.add(id);

                                                }
                                                for(int y=0;y<offeringNames.size();y++){
                                                    if(y!=offeringNames.size()-1){
                                                        names+=offeringNames.get(y)+" ,";
                                                    }else{
                                                        names+=offeringNames.get(y);
                                                    }
                                                }
                                                Map<String,Object> offering_names=new HashMap<>();
                                                offering_names.put("offerings",names);
                                                dB.collection("Farmers_Producers").document(aadharId).collection("FarmerProducerProfile").document("profile").update(offering_names).addOnCompleteListener(new OnCompleteListener<Void>(){
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(getActivity(),"Added",Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                            }
                                        });

                                    }
                                }
                            });
                        }
                    }
                }



            }
        });






        settingsIcon=view.findViewById(R.id.setting_icon);
        settingsIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),FarmerProducerProfileActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("profilePic",imageName);
                intent.putExtra("aadharNum",aadharId);
                startActivity(intent);
            }
        });


        return view;
    }

}