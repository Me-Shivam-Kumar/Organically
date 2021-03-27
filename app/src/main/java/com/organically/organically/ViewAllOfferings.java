package com.organically.organically;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.organically.organically.adapter.OfferingsAdapter;
import com.organically.organically.model.OfferingsModel;

import java.util.ArrayList;
import java.util.List;

public class ViewAllOfferings extends AppCompatActivity {
    private List<OfferingsModel> offeringsModelList;
    private RecyclerView viewAllOfferingsRecyclerView;
    private static OfferingsAdapter offeringsAdapter;
    FirebaseFirestore allOfferingsDB;
    String aadharNumber;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_offerings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Offerings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

          offeringsModelList = new ArrayList<OfferingsModel>();

        viewAllOfferingsRecyclerView = findViewById(R.id.view_allOffering_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ViewAllOfferings.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewAllOfferingsRecyclerView.setLayoutManager(layoutManager);

        loadingDialog=new LoadingDialog(ViewAllOfferings.this);
        allOfferingsDB = FirebaseFirestore.getInstance();
        SharedPreferences pref=getSharedPreferences("FarmerPref", Context.MODE_PRIVATE);
        aadharNumber=pref.getString("aadharCardNumber",null);
        if(aadharNumber != null){
            loadingDialog.showDialog();
            allOfferingsDB.collection("Farmers_Producers").document(aadharNumber).collection("Offerings").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        loadingDialog.hideDialog();
                        for(DocumentSnapshot document : task.getResult()){
                            offeringsModelList.add(new OfferingsModel(document.getString("productName"),document.getString("totalProuctQty"),document.getString("productAvailabilityPeriod"),document.getString("pricePerUnit"),document.getString("startDate"),
                                    document.getString("endDate"),document.getString("totalQuantityUnit"),0,aadharNumber,""));

                        }
                        offeringsAdapter = new OfferingsAdapter(offeringsModelList);
                        offeringsAdapter.notifyDataSetChanged();
                        viewAllOfferingsRecyclerView.setAdapter(offeringsAdapter);

                    }

                }


            });
            allOfferingsDB.collection("Farmers_Producers").document(aadharNumber).collection("offerings").
                    addSnapshotListener(ViewAllOfferings.this, new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(error!=null){
                                Toast.makeText(ViewAllOfferings.this,"Error Loading Changes",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            for(QueryDocumentSnapshot snapshot:value){
                                offeringsModelList.add(new OfferingsModel(snapshot.getString("productName"),snapshot.getString("totalProuctQty"),snapshot.getString("productAvailabilityPeriod"),snapshot.getString("pricePerUnit"),snapshot.getString("startDate"),
                                        snapshot.getString("endDate"),snapshot.getString("totalQuantityUnit"),0,aadharNumber,""));
                            }
                            offeringsAdapter = new OfferingsAdapter(offeringsModelList);
                            offeringsAdapter.notifyDataSetChanged();
                            viewAllOfferingsRecyclerView.setAdapter(offeringsAdapter);

                        }
                    });



        }


    }
    public static void refreshItem(int deselect,int select){
        offeringsAdapter.notifyItemChanged(deselect);
        offeringsAdapter.notifyItemChanged(select);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}