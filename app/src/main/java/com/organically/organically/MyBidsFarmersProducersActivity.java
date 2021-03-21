package com.organically.organically;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyBidsFarmersProducersActivity extends AppCompatActivity {
    RecyclerView myBidsFarmersProducersRecyclerView;
    List<MyBidsFarmersProducersModel> myBidsFarmersProducersModelList;
    FirebaseFirestore myBidsDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bids_farmers_producers);
        myBidsFarmersProducersModelList=new ArrayList<MyBidsFarmersProducersModel>();

        Toolbar toolbar = findViewById(R.id.toolbar_mybids);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Your Bids");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myBidsFarmersProducersRecyclerView=findViewById(R.id.my_bids_farmers_producers_recyclerView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(MyBidsFarmersProducersActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myBidsFarmersProducersRecyclerView.setLayoutManager(layoutManager);
        SharedPreferences pref=getSharedPreferences("FarmerPref", Context.MODE_PRIVATE);
        String aadharId=pref.getString("aadharCardNumber",null );
        if(aadharId !=null){
            myBidsDB=FirebaseFirestore.getInstance();
            myBidsDB.collection("Farmers_Producers").document(aadharId).collection("Bids").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){

                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        for(DocumentSnapshot snapshot:task.getResult()){
                            myBidsFarmersProducersModelList.add(new MyBidsFarmersProducersModel(
                                    snapshot.getString("title"),snapshot.getString("itemName"),snapshot.getString("qty"),snapshot.getString("areaPincode"),
                                    snapshot.getString("durationOfOrder"),snapshot.getString("bidPricePerUnit"),snapshot.getString("description")
                            ,snapshot.getString("customerId"),snapshot.getId()));


                        }

                        MyBidsFarmersProducersAdapter adapter=new MyBidsFarmersProducersAdapter(myBidsFarmersProducersModelList);
                        adapter.notifyDataSetChanged();
                        myBidsFarmersProducersRecyclerView.setAdapter(adapter);
                    }
                    else{
                        Toast.makeText(MyBidsFarmersProducersActivity.this,"Not Successfull",Toast.LENGTH_LONG).show();

                    }
                }
            });
        }

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