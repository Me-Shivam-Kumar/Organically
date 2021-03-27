package com.organically.organically;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.organically.organically.adapter.OfferingsAdapter;
import com.organically.organically.adapter.ReviewAdapter;
import com.organically.organically.model.OfferingsModel;
import com.organically.organically.model.ReviewModelClass;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FarmerProducerDetailsActivity extends AppCompatActivity {
    TextView nameTV,aadharNumTV;
    FirebaseFirestore db;
    private List<OfferingsModel> offeringsModelList;
    private RecyclerView viewAllOfferingsRecyclerView;
    private static OfferingsAdapter offeringsAdapter;
    private CircleImageView profilePicView;
    List<ReviewModelClass> reviewModelClassList;
    RecyclerView reviewRecyclerView;
    LoadingDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_producer_details);

        String name=getIntent().getStringExtra("name");
        String aadharId=getIntent().getStringExtra("aadharId");
        String profilePic=getIntent().getStringExtra("profilePic");

        loadingDialog=new LoadingDialog(FarmerProducerDetailsActivity.this);

        nameTV=findViewById(R.id.farmerProducerName_TV);
        aadharNumTV=findViewById(R.id.aadhar_number_TV);
        profilePicView=findViewById(R.id.profile_pic);
        SharedPreferences pref=getSharedPreferences("CustomerPref", Context.MODE_PRIVATE);
        String aadharNumber=pref.getString("aadharCardNumber",null);

        nameTV.setText(name);
        aadharNumTV.setText(aadharId);
        Glide.with(FarmerProducerDetailsActivity.this).load(profilePic).into(profilePicView);

        reviewModelClassList=new ArrayList<ReviewModelClass>();
        reviewModelClassList.add(new ReviewModelClass("The product he offers is awesome"));
        reviewModelClassList.add(new ReviewModelClass("Punctual"));
        reviewModelClassList.add(new ReviewModelClass("The output is proof of his hardwork"));
        reviewModelClassList.add(new ReviewModelClass("The product he offers is awesome"));

        LinearLayoutManager linearlayoutManager=new LinearLayoutManager(FarmerProducerDetailsActivity.this);
        linearlayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        reviewRecyclerView=findViewById(R.id.review_recyclerView);
        reviewRecyclerView.setLayoutManager(linearlayoutManager);

        ReviewAdapter reviewAdapter=new ReviewAdapter(reviewModelClassList);
        reviewRecyclerView.setAdapter(reviewAdapter);
        reviewAdapter.notifyDataSetChanged();

        offeringsModelList = new ArrayList<OfferingsModel>();

        viewAllOfferingsRecyclerView = findViewById(R.id.offerings_list_farmerDetails);
        LinearLayoutManager layoutManager = new LinearLayoutManager(FarmerProducerDetailsActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewAllOfferingsRecyclerView.setLayoutManager(layoutManager);


        db=FirebaseFirestore.getInstance();
        loadingDialog.showDialog();
        db.collection("Farmers_Producers").document(aadharId).collection("Offerings").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot document:task.getResult()){
                        offeringsModelList.add(new OfferingsModel(document.getString("productName"),document.getString("totalProuctQty"),document.getString("productAvailabilityPeriod"),document.getString("pricePerUnit"),document.getString("startDate"),
                                document.getString("endDate"),document.getString("totalQuantityUnit"),1,aadharId,aadharNumber));


                    }
                    offeringsAdapter = new OfferingsAdapter(offeringsModelList);
                    offeringsAdapter.notifyDataSetChanged();
                    viewAllOfferingsRecyclerView.setAdapter(offeringsAdapter);
                    loadingDialog.hideDialog();
                }

            }
        });

    }
}