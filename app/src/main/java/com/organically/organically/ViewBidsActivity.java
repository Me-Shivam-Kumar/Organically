package com.organically.organically;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewBidsActivity extends AppCompatActivity {
RecyclerView recyclerView;
List<ViewBidsModel> viewBidsModelList;
FirebaseFirestore bidsDB;
String aadharNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bids);
        recyclerView=findViewById(R.id.viewBids_recyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(ViewBidsActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        viewBidsModelList=new ArrayList<ViewBidsModel>();

        String orderId=getIntent().getStringExtra("orderId");
        String itemName=getIntent().getStringExtra("itemName");
        String qtyOrderdByCustomer=getIntent().getStringExtra("qtyOrderdByCustomer");
        String unit=getIntent().getStringExtra("unit");
        String duration=getIntent().getStringExtra("duration");
        SharedPreferences pref=getSharedPreferences("CustomerPref", Context.MODE_PRIVATE);
        aadharNumber=pref.getString("aadharCardNumber",null);
        bidsDB=FirebaseFirestore.getInstance();
        if(aadharNumber!=null){
            bidsDB.collection("Customers").document(aadharNumber).collection("Orders").document(orderId).collection("Bids").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                                                                                                                                                            @Override
                                                                                                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                                                                                                if(task.isSuccessful()){
                                                                                                                                                                    for(DocumentSnapshot snapshot:task.getResult()){
                                                                                                                                                                        viewBidsModelList.add(new ViewBidsModel(snapshot.getString("bidPricePerUnit"),snapshot.getString("description"),snapshot.getString("farmerProducerId"),orderId,itemName,qtyOrderdByCustomer,unit,duration));
                                                                                                                                                                    }
                                                                                                                                                                    ViewBidsAdapter viewBidsAdapter=new ViewBidsAdapter(viewBidsModelList);
                                                                                                                                                                    viewBidsAdapter.notifyDataSetChanged();
                                                                                                                                                                    recyclerView.setAdapter(viewBidsAdapter);
                                                                                                                                                                }
                                                                                                                                                            }
                                                                                                                                                        }
            );
        }


    }


}